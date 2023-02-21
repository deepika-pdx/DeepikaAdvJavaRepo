package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for the {@link Converter} main class.
 */
public class ConverterIT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Converter} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Converter.class, args);
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test
    void testNoCommandLineArguments() {
        MainMethodResult result = invokeMain();
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
    }

    /**
     * Tests that invoking the main method with less than two arguments issues an error
     * and provides user with necessary information
     */
    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("./textFilename.txt");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments"));
    }

    /**
     * Tests that invoking the main method with more than two arguments issues an error
     * and provides user with necessary information
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("./textFilename.txt", "./xmlFilename.xml", "./prettyFilename.xml");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
    }

    /**
     * Tests that invoking the main method with file name with extension other than '.txt' as first argument issues an error
     * and provides user with necessary information
     */
    @Test
    void testFileWithIncorrectExtensionForTextFileIssuesError() {
        MainMethodResult result = invokeMain("./test1.xml", "./test2.xml");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide a file with '.txt' extension as the first argument."));
    }

    /**
     * Tests that invoking the main method with file name with extension other than '.xml' as second argument issues an error
     * and provides user with necessary information
     */
    @Test
    void testFileWithIncorrectExtensionForXmlFileIssuesError() {
        MainMethodResult result = invokeMain("./test1.txt", "./test2.txt");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide a file with '.xml' extension as the second argument."));
    }

    /**
     * Tests that invoking the main method with valid text and xml file name correctly converts the airline details in text file to a xml file
     */
    @Test
    void testFileWithValidTextAndXmlFilenamesConvertsAirlineDataCorrectly(@TempDir File tempDir) throws IOException, ParseException {
        Airline airline = new Airline("Test Airline");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date departureDate = sdf.parse("01/25/2023 3:30 PM");
        Date arrivalDate = sdf.parse("01/26/2023 5:15 PM");
        Flight flight = new Flight(789, "PDX", "01/25/2023 3:30 PM", departureDate, "PHL", "01/26/2023 5:15 PM", arrivalDate);
        airline.addFlight(flight);
        AirlineTextDumper airlineTextDumper = new AirlineTextDumper(new FileWriter(tempDir + "/textFile.txt"));
        airlineTextDumper.dump(airline);

        MainMethodResult result = invokeMain(tempDir + "/textFile.txt", tempDir + "/xmlFile.xml");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that invoking the main method with invalid text file name issues error
     */
    @Test
    void testFileWithInvalidTextFilenameIssuesError(@TempDir File tempDir) throws IOException, ParseException {
        Airline airline = new Airline("Test Airline");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date departureDate = sdf.parse("01/25/2023 13:30 PM");
        Date arrivalDate = sdf.parse("01/26/2023 15:15 PM");
        Flight flight = new Flight(789, "PDX", "01/25/2023 13:30 PM", departureDate, "PHL", "01/26/2023 15:15 PM", arrivalDate);
        airline.addFlight(flight);
        AirlineTextDumper airlineTextDumper = new AirlineTextDumper(new FileWriter(tempDir + "/textFile.txt"));
        airlineTextDumper.dump(airline);

        MainMethodResult result = invokeMain(tempDir + "/textFile.txt", tempDir + "/xmlFile.xml");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid text file. Unable to parse the text file specified in the command line argument."));
    }

    /**
     * Tests that invoking the main method with non-existing text file name issues error
     */
    @Test
    void testFileWithNonExistingTextFilenameIssuesError(@TempDir File tempDir) throws IOException, ParseException {
        MainMethodResult result = invokeMain(tempDir + "/textFile.txt", tempDir + "/xmlFile.xml");
        assertThat(result.getTextWrittenToStandardError(), containsString("The text file specified in the command line argument is not found!"));
    }
}
