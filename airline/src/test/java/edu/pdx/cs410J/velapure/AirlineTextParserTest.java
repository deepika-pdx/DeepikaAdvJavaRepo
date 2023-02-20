package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link AirlineTextParser} class.
 */
public class AirlineTextParserTest {

    /**
     * This unit test checks if a valid text file having airline info can be parsed correctly.
     */
    @Test
    void validTextFileCanBeParsed() throws ParserException {
        InputStream resource = getClass().getResourceAsStream("valid-airline.txt");
        assertThat(resource, notNullValue());

        AirlineTextParser parser = new AirlineTextParser(new InputStreamReader(resource));
        Airline airline = parser.parse();
        assertThat(airline.getName(), equalTo("Test Airline"));
    }

    /**
     * This unit test checks if an invalid text file throws ParseException.
     */
    @Test
    void invalidTextFileThrowsParserException() {
        InputStream resource = getClass().getResourceAsStream("empty-airline.txt");
        assertThat(resource, notNullValue());

        AirlineTextParser parser = new AirlineTextParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse);
    }

    /**
     * Tests that providing the malformed text file having invalid airline parameter format
     * is passed with '-textFile' option issues error.
     */
    @Test
    void MalformedTextFileWithInvalidAirlineFormatThrowsException() {
        InputStream resource = getClass().getResourceAsStream("malformed_text_flight.txt");
        assertThat(resource, notNullValue());

        AirlineTextParser parser = new AirlineTextParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse);
    }

    /**
     * Tests that providing the malformed text file having invalid source location parameter format
     * is passed with '-textFile' option issues error.
     */
    @Test
    void MalformedTextFileWithInvalidSourceLocationFormatThrowsException() {
        InputStream resource = getClass().getResourceAsStream("malformed_text_flight2.txt");
        assertThat(resource, notNullValue());

        AirlineTextParser parser = new AirlineTextParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse);
    }

    /**
     * Tests that providing the malformed text file having invalid arrival date
     * is passed with '-textFile' option issues error.
     */
    @Test
    void MalformedTextFileWithInvalidArrivalDateThrowsException() {
        InputStream resource = getClass().getResourceAsStream("malformed_text_flight3.txt");
        assertThat(resource, notNullValue());

        AirlineTextParser parser = new AirlineTextParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse);
    }


}
