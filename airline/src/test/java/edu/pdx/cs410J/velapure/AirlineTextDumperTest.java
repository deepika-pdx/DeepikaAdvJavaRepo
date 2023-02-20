package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit tests for the {@link AirlineTextDumper} class.
 */
public class AirlineTextDumperTest {

    /**
     * This unit test checks if the airline name is correctly written to a file.
     */
    @Test
    void airlineNameIsDumpedInTextFormat() {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);

        StringWriter sw = new StringWriter();
        AirlineTextDumper dumper = new AirlineTextDumper(sw);
        dumper.dump(airline);

        String text = sw.toString();
        assertThat(text, containsString(airlineName));
    }

    /**
     * This unit test checks if the airline can be read/parsed correctly.
     *
     * @param tempDir
     *         A temporary directory to store the files created during testing.
     */
    @Test
    void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);

        File textFile = new File(tempDir + "airline.txt");
        AirlineTextDumper dumper = new AirlineTextDumper(new FileWriter(textFile));
        dumper.dump(airline);

        AirlineTextParser parser = new AirlineTextParser(new FileReader(textFile));
        Airline read = parser.parse();
        assertThat(read.getName(), equalTo(airlineName));
    }
}
