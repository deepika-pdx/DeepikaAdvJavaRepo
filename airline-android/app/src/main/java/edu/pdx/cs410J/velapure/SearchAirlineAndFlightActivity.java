package edu.pdx.cs410J.velapure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.regex.Pattern;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

public class SearchAirlineAndFlightActivity extends AppCompatActivity {

    static final String AIRLINE_PRETTY_TEXT_VALUE = "AIRLINE_PRETTY_TEXT";

    private String searchAirlineName = null;

    private String prettyPrintText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_airline_and_flight);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchAirlineAndFlights(View view) {
        EditText searchAirlineText = findViewById(R.id.search_airline_name);
        searchAirlineName = searchAirlineText.getText().toString();

        EditText searchSrcAirportCodeText = findViewById(R.id.search_src_airport_code);
        String searchSrcAirportCode = searchSrcAirportCodeText.getText().toString();

        EditText searchDestAirportCodeText = findViewById(R.id.search_dest_airport_code);
        String searchDestAirportCode = searchDestAirportCodeText.getText().toString();

        if(searchAirlineName == null || searchAirlineName.isEmpty()) {
            Toast.makeText(this, "Please provide airline name.", Toast.LENGTH_LONG).show();
        } else {
            Optional<String> srcAirport = null;
            Optional<String> destAirport = null;
            if(searchSrcAirportCode == null || searchSrcAirportCode.isEmpty()) {
                srcAirport = Optional.empty();
            } else {
                srcAirport = Optional.of(searchSrcAirportCode.toUpperCase());
            }
            if(searchDestAirportCode == null || searchDestAirportCode.isEmpty()) {
                destAirport = Optional.empty();
            } else {
                destAirport = Optional.of(searchDestAirportCode.toUpperCase());
            }
            try {
                validateAndSearchAirlineDetails(searchAirlineName, srcAirport, destAirport);
            } catch (AirlineException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void validateAndSearchAirlineDetails(String airlineName, Optional<String> srcAirportCode, Optional<String> destAirportCode) {
        String srcLocation = null;
        // Validation of the provided source location airport code
        if (srcAirportCode.isPresent()) {
            srcLocation = srcAirportCode.get();
            if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
                throw new AirlineException("Source location provided should consist of only three alphabets [a-zA-Z].");
            } else if (AirportNames.getName(srcLocation) == null) {
                throw new AirlineException("The three-letter source airport code does not correspond to a known airport!");
            }
        }

        String destLocation = null;
        // Validation of the provided destination location
        if (destAirportCode.isPresent()) {
            destLocation = destAirportCode.get();
            if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
                throw new AirlineException("Destination location provided should consist of only three alphabets [a-zA-Z].");
            } else if (AirportNames.getName(destLocation) == null) {
                throw new AirlineException("The three-letter destination airport code does not correspond to a known airport!");
            }
        } else if (srcAirportCode.isPresent()) {
            throw new AirlineException("Please provide destination airport code along with source airport code.");
        }

        if (srcLocation != null && destLocation != null && srcLocation.equals(destLocation)) {
            throw new AirlineException("The source and destination airport code should not be the same.");
        }

        try {
            Airline airline = readAirlineFromFile();
            if (airline == null) {
                throw new AirlineException("The specified airline does not exist.");
            } else {
                AirlineScreenPrettyPrinter prettyPrinter = new AirlineScreenPrettyPrinter();
                prettyPrintText = prettyPrinter.convertToPrettyText(airline, srcAirportCode, destAirportCode);
                if (srcAirportCode.isPresent() && destAirportCode.isPresent()) {
                    String[] prettyPrintArray = prettyPrintText.split("\n");
                    if (prettyPrintArray != null && prettyPrintArray.length == 1) {
                        throw new AirlineException("There are no direct flights for specified source and destination airport codes!");
                    } else {
                        writeAirlineDetailsToPrettyPrintActivity();
                    }
                } else {
                    writeAirlineDetailsToPrettyPrintActivity();
                }
            }
        } catch (IOException e) {
            throw new AirlineException(e.getMessage());
        }
    }

    private void  writeAirlineDetailsToPrettyPrintActivity() {
        Intent prettyIntent = new Intent(this, PrettyPrintAirlineActivity.class);
        prettyIntent.putExtra(AIRLINE_PRETTY_TEXT_VALUE, prettyPrintText);
        startActivity(prettyIntent);
    }

    private Airline readAirlineFromFile() throws IOException {
        Airline readAirline = null;
        File airlineFile = getAirlineFile();
        try {
            AirlineTextParser parser = new AirlineTextParser(new FileReader(airlineFile));
            readAirline = parser.parse();
        } catch (FileNotFoundException e) {
            readAirline = null;
        } catch (IOException | ParserException e) {
            throw new AirlineException("Unable to get airline and flight details!!");
        }
        return readAirline;
    }

    @NonNull
    private File getAirlineFile() {
        File datadir = this.getDataDir();
        String filename = searchAirlineName.toLowerCase() + ".txt";
        return new File(datadir, filename);
    }
}