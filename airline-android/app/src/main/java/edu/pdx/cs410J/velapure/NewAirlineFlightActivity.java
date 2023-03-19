package edu.pdx.cs410J.velapure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

public class NewAirlineFlightActivity extends AppCompatActivity implements View.OnClickListener{

    private String airlineName = null;

    private EditText departDateTxt;
    private Button departDateBtn;
    private EditText departTimeTxt;
    private Button departTimeBtn;

    private EditText arrivalDateTxt;
    private Button arrivalDateBtn;
    private EditText arrivalTimeTxt;
    private Button arrivalTimeBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_airline_flight);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        departDateTxt = findViewById(R.id.departure_date_text);
        departDateBtn = findViewById(R.id.departure_date_btn);
        departTimeTxt = findViewById(R.id.departure_time_text);
        departTimeBtn = findViewById(R.id.departure_time_btn);

        arrivalDateTxt = findViewById(R.id.arrival_date_text);
        arrivalDateBtn = findViewById(R.id.arrival_date_btn);
        arrivalTimeTxt = findViewById(R.id.arrival_time_text);
        arrivalTimeBtn = findViewById(R.id.arrival_time_btn);

        departDateBtn.setOnClickListener(this);
        departTimeBtn.setOnClickListener(this);
        arrivalDateBtn.setOnClickListener(this);
        arrivalTimeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int currentYear, currentMonth, currentDay, currentHour, currentMin;
        if (view == departDateBtn) {
            Calendar cal = Calendar.getInstance();
            currentYear = cal.get(Calendar.YEAR);
            currentMonth = cal.get(Calendar.MONTH);
            currentDay = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    departDateTxt.setText((month + 1) + "/" + day + "/" + year);
                }
            }, currentYear, currentMonth, currentDay);
            datePicker.show();
        } else if (view == departTimeBtn) {
            Calendar cal = Calendar.getInstance();
            currentHour = cal.get(Calendar.HOUR_OF_DAY);
            currentMin = cal.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    String departure_time_indication = null;
                    int updatedHour = 0;
                    if (hour == 0) {
                        updatedHour = 12;
                        departure_time_indication = "AM";
                    } else if (hour < 12) {
                        departure_time_indication = "AM";
                        updatedHour = hour;
                    } else if (hour == 12) {
                        departure_time_indication = "PM";
                        updatedHour = hour;
                    } else {
                        departure_time_indication = "PM";
                        updatedHour = hour - 12;
                    }
                    departTimeTxt.setText(updatedHour + ":" + minute + " " + departure_time_indication);
                }
            }, currentHour, currentMin, false);
            timePicker.show();
        } else if (view == arrivalDateBtn) {
            Calendar cal = Calendar.getInstance();
            currentYear = cal.get(Calendar.YEAR);
            currentMonth = cal.get(Calendar.MONTH);
            currentDay = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    arrivalDateTxt.setText((month + 1) + "/" + day + "/" + year);
                }
            }, currentYear, currentMonth, currentDay);
            datePicker.show();
        } else if (view == arrivalTimeBtn) {
            Calendar cal = Calendar.getInstance();
            currentHour = cal.get(Calendar.HOUR_OF_DAY);
            currentMin = cal.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    String arrival_time_indication = null;
                    int updatedHour = 0;
                    if (hour == 0) {
                        updatedHour = 12;
                        arrival_time_indication = "AM";
                    } else if (hour < 12) {
                        arrival_time_indication = "AM";
                        updatedHour = hour;
                    } else if (hour == 12) {
                        arrival_time_indication = "PM";
                        updatedHour = hour;
                    } else {
                        arrival_time_indication = "PM";
                        updatedHour = hour - 12;
                    }
                    arrivalTimeTxt.setText(updatedHour + ":" + minute + " " + arrival_time_indication);
                }
            }, currentHour, currentMin, false);
            timePicker.show();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAirline(View view) {
        EditText airlineText = findViewById(R.id.airline_name);
        airlineName = airlineText.getText().toString();
        if(airlineName == null || airlineName.isEmpty()) {
            Toast.makeText(this, "Please provide airline name.", Toast.LENGTH_LONG).show();
        } else {
            Airline airline = null;
            try {
                airline = readAirlineFromFile();
                if (airline == null) {
                    airline = new Airline(airlineName);
                }
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            EditText flightNumberText = findViewById(R.id.flight_number);
            String flightNumber = flightNumberText.getText().toString();

            EditText srcAirportCodeText = findViewById(R.id.src_airport_code);
            String srcAirportCode = srcAirportCodeText.getText().toString();

            EditText departureDateText = findViewById(R.id.departure_date_text);
            String departureDate = departureDateText.getText().toString();

            EditText departureTimeText = findViewById(R.id.departure_time_text);
            String departureTime = departureTimeText.getText().toString();

            EditText destAirportCodeText = findViewById(R.id.dest_airport_code);
            String destAirportCode = destAirportCodeText.getText().toString();

            EditText arrivalDateText = findViewById(R.id.arrival_date_text);
            String arrivalDate = arrivalDateText.getText().toString();

            EditText arrivalTimeText = findViewById(R.id.arrival_time_text);
            String arrivalTime = arrivalTimeText.getText().toString();
            try {
                validateAndAddFlight(airline, flightNumber, srcAirportCode, departureDate, departureTime, destAirportCode, arrivalDate, arrivalTime);
                writeAirlineToFile(airline);
            } catch (AirlineException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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

    private void writeAirlineToFile(Airline airline) {
        File airlineFile = getAirlineFile();
        try {
            AirlineTextDumper airlineTextDumper = new AirlineTextDumper(new FileWriter(airlineFile));
            airlineTextDumper.dump(airline);
        } catch (IOException e) {
            throw new AirlineException("Unable to save airline and flight details!!");
        }
    }

    @NonNull
    private File getAirlineFile() {
        File datadir = this.getDataDir();
        String filename = airlineName.toLowerCase() + ".txt";
        return new File(datadir, filename);
    }

    private void validateAndAddFlight(Airline airline, String flightNumberString, String srcLocation, String departureDateString,
                                          String departureTimeString, String destLocation, String arrivalDateString,
                                          String arrivalTimeString) throws AirlineException {
        int flightNumber = 0;
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        Date departureDate = null;
        Date arrivalDate = null;

        // Validation of the provided flight number
        if (flightNumberString == null || flightNumberString.isEmpty()) {
            throw new AirlineException("Please provide flight number.");
        } else {
            try {
                flightNumber = Integer.parseInt(flightNumberString);
            } catch (NumberFormatException e) {
                throw new AirlineException("Flight number provided should consist of only numbers between 0-9.");
            }
        }

        // Validation of the provided source location
        if (srcLocation == null || srcLocation.isEmpty()) {
            throw new AirlineException("Please provide source airport code.");
        } else {
            if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
                throw new AirlineException("Source location provided should consist of only three alphabets [a-zA-Z].");
            } else if (AirportNames.getName(srcLocation.toUpperCase()) == null) {
                throw new AirlineException("The three-letter source location code does not correspond to a known airport!");
            } else {
                srcLocation = srcLocation.toUpperCase();
            }
        }

        // Validation of the provided departure date and time
        try {
            if (departureDateString == null || departureDateString.isEmpty()) {
                throw new AirlineException("Please provide departure date.");
            } else if (departureTimeString == null || departureTimeString.isEmpty()) {
                throw new AirlineException("Please provide departure time.");
            } else {
                String departureDateInString = departureDateString + " " + departureTimeString;
                departureDate = dateTimeFormatter.parse(departureDateInString);
            }
        } catch (ParseException e) {
            throw new AirlineException("Invalid date and/or time.");
        }


        // Validation of the provided destination location
        if (destLocation == null || destLocation.isEmpty()) {
            throw new AirlineException("Please provide destination airport code.");
        } else {
            if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
                throw new AirlineException("Destination location provided should consist of only three alphabets [a-zA-Z].");
            } else if (AirportNames.getName(destLocation.toUpperCase()) == null) {
                throw new AirlineException("The three-letter destination airport code does not correspond to a known airport!");
            } else {
                destLocation = destLocation.toUpperCase();
            }
        }

        // Validation is source and destination airport code are the same
        if (srcLocation.equals(destLocation)) {
            throw new AirlineException("The provided source and destination airport code should not be the same.");
        }

        // Validation of the provided arrival date and time
        try {
            if (arrivalDateString == null || arrivalDateString.isEmpty()) {
                throw new AirlineException("Please provide arrival date.");
            } else if (arrivalTimeString == null || arrivalTimeString.isEmpty()) {
                throw new AirlineException("Please provide arrival time.");
            } else {
                arrivalDate = dateTimeFormatter.parse(arrivalDateString + " " + arrivalTimeString);
            }
        } catch (ParseException e) {
            throw new AirlineException("Invalid date and/or time.");
        }

        // Validation of the provided departure and arrival date and time
        if (arrivalDate.before(departureDate) || arrivalDate.equals(departureDate)) {
            throw new AirlineException("The arrival date/time should be after departure date/time.");
        }

        // Create and add flight to the provided airline
        Flight flight = new Flight(flightNumber, srcLocation, departureDateString + " " + departureTimeString, departureDate, destLocation,
                arrivalDateString + " " + arrivalTimeString, arrivalDate);
        airline.addFlight(flight);
        Toast.makeText(this, "Flight added successfully to the airline!", Toast.LENGTH_LONG).show();
    }
}