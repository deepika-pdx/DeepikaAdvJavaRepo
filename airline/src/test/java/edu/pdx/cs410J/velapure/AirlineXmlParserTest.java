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
 * Unit tests for the {@link AirlineXmlParser} class.
 */
public class AirlineXmlParserTest {

    /**
     * This unit test checks if a valid xml file having airline info can be parsed correctly.
     */
    @Test
    void validXmlFileCanBeParsed() throws ParserException {
        InputStream resource = getClass().getResourceAsStream("valid-airline.xml");
        assertThat(resource, notNullValue());

        AirlineXmlParser parser = new AirlineXmlParser(new InputStreamReader(resource));
        Airline airline = parser.parse();
        assertThat(airline.getName(), equalTo("Valid Airlines"));
    }

    /**
     * This unit test checks if an invalid xml file with missing departure time throws ParseException.
     */
    @Test
    void invalidXmlFileWithMissingDepartureTimeThrowsParserException() {
        InputStream resource = getClass().getResourceAsStream("invalid-airline.xml");
        assertThat(resource, notNullValue());

        AirlineXmlParser parser = new AirlineXmlParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse);
    }

    /**
     * This unit test checks if an invalid xml file with invalid departure year throws ParseException with correct error message.
     */
    @Test
    void invalidXmlFileWithInvalidDepartureYearThrowsParserExceptionWithCorrectErrMsg() {
        InputStream resource = getClass().getResourceAsStream("airline-with-invalid-date.xml");
        assertThat(resource, notNullValue());

        AirlineXmlParser parser = new AirlineXmlParser(new InputStreamReader(resource));
        assertThrows(ParserException.class, parser::parse, "Malformed xml file. Unable to parse year of the given date.");
    }
}
