package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for the {@link Project3} main class.
 */
class Project3IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project3.class, args);
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
     * Tests that invoking the main method with less than eight arguments issues an error
     * and provides user with necessary information
     */
    @Test
    void testTooFewCommandLineArguments() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments"));
    }

    /**
     * Tests that invoking the main method with more than ten arguments (including optional arguments) issues an error
     * and provides user with necessary information
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "01/22/2023", "14:30", "HYD", "01/22/2023", "16:30", "KOL", "586", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide valid flight and airline information after the '-print' option."));
    }

    /**
     * Tests that providing flight number with value that is not numeric issues an error
     */
    @Test
    void testFlightNumberContainsOnlyNumbers() {
        MainMethodResult result = invokeMain("IndiGo", "789c", "PUN", "01/22/2023", "2:30", "PM", "HYD", "01/22/2023", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Flight number provided should consist of only numbers between 0-9."));
    }

    /**
     * Tests that providing source location with value that is not alphabetic (a-zA-Z) or contains alphabets more or less than 3 issues an error
     */
    @Test
    void testSourceLocationContainsOnlyThreeAlphabets() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN1#", "01/22/2023", "4:30", "AM", "HYD", "01/22/2023", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Source location provided should consist of only three alphabets [a-zA-Z]"));
    }

    /**
     * Tests that providing departure date in format other than mm/dd/yyyy issues an error
     */
    @Test
    void testDepartureDateIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "15/12/2023", "1:30", "AM", "HYD", "01/22/2023", "10:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid departure date. Please provide the departure date in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing departure time in format other than 24-hour time hh:mm issues an error
     */
    @Test
    void testDepartureTimeIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "23:60", "AM", "HYD", "01/22/2023", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid departure time. Please provide the departure time in 12-hour(hh:mm) format"));
    }

    /**
     * Tests that providing destination location with value that is not alphabetic (a-zA-Z) or contains alphabets more or less than 3 issues an error
     */
    @Test
    void testDestinationLocationContainsOnlyThreeAlphabets() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "01/22/2023", "11:30", "am", "HYDBD", "01/22/2023", "9:30", "pm");
        assertThat(result.getTextWrittenToStandardError(), containsString("Destination location provided should consist of only three alphabets [a-zA-Z]"));
    }

    /**
     * Tests that providing arrival date in format other than mm/dd/yyyy issues an error
     */
    @Test
    void testArrivalDateIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/3/2023", "4:30", "PM", "HYD", "1/22/23", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid arrival date. Please provide the arrival date in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing arrival time in format other than 24-hour time hh:mm issues an error
     */
    @Test
    void testArrivalTimeIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "3:59", "PM", "HYD", "01/22/2023", "24:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid arrival time. Please arrival the arrival time in 12-hour(hh:mm) format."));
    }

    /**
     * Tests that providing the optional parameter '-print' with less airline and flight information issues error.
     */
    @Test
    void testOptionalParameterPrintWithLessFlightAndAirlineInformationIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "2:09", "PM", "HYD", "01/22/2023", "5:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the optional parameter '-print' with airline and flight information along with extra parameters issues error.
     */
    @Test
    void testOptionalParameterPrintWithFlightAndAirlineInformationAndExtraParamIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "3:59", "PM", "HYD", "01/22/2023", "9:00", "AM", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments. Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the airline and flight information without any options creates the flight successfully with no error.
     */
    @Test
    void testAirlineAndFlightCreatedSuccessfully() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "12:59", "AM", "HYD", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameter '-print' prints the flight description correctly.
     */
    @Test
    void testOptionalParameterPrintProvidesTheFlightDescription() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "12:59", "AM", "HYD", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PUN at 1/12/2023 12:59 AM arrives HYD at 01/22/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameter '-print' not at the beginning but elsewhere in the command line arguments issues an error.
     */
    @Test
    void testOptionalParameterPrintNotProvidedAtStartIssuesError() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "12:59", "AM", "HYD", "01/22/2023", "1:00", "PM", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide valid flight and airline information after the '-print' option."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-textFile' with insufficient airline and flight details issues error.
     */
    @Test
    void testOptionalParametersPrintAndTextFileWithInSufficientDetailsIssuesError() {
        MainMethodResult result = invokeMain("-print", "-textFile", "/airline/file.txt", "IndiGo", "789", "PUN", "1/12/2023", "11:59", "PM", "HYD", "01/22/2023");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename and valid flight and airline information along with '-textFile'."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-textFile' with insufficient airline and flight details issues error.
     */
    @Test
    void testOptionalParametersPrintAndTextFileWithInSufficientAirlineDetailsIssuesError() {
        MainMethodResult result = invokeMain("-print", "-textFile", "/airline/file.txt", "IndiGo", "789", "PUN", "1/12/2023", "11:59", "PM", "HYD", "01/22/2023", "2:50");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename and valid flight and airline information along with '-print' and '-textFile'."));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and '-textFile' with insufficient airline and flight details issues error.
     */
    @Test
    void testOptionalParametersPrettyAndTextFileWithInSufficientAirlineDetailsIssuesError() {
        MainMethodResult result = invokeMain("-pretty", "-textFile", "/airline/file.txt", "IndiGo", "789", "PUN", "1/12/2023", "11:59", "PM", "HYD", "01/22/2023", "2:50");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'."));
    }

    /**
     * Tests that providing unknown optional parameter issues an error and provides necessary information.
     */
    @Test
    void testUnknownOptionalParameterIssuesError() {
        MainMethodResult result = invokeMain("-trace", "IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "23:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("An unknown option was provided."));
    }

    /**
     * Tests that providing only the optional parameter '-README' provides the details about Project1.
     */
    @Test
    void testOnlyOptionalParameterREADMEProvidesProject1Details() {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an 'Airline' project."));
    }

    /**
     * Tests that providing the optional parameter '-README' provides the details about Project1.
     */
    @Test
    void testOptionalParameterREADMEProvidesProject1Details() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "23:00", "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an 'Airline' project."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-README' prints the details about Project1.
     */
    @Test
    void testOptionalParametersPrintAndREADMEProvidesOnlyProject1Details() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "23:00", "-print", "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an 'Airline' project."));
    }

    /**
     * Tests that providing only the optional parameters '-textFile' issues error and provides necessary information.
     */
    @Test
    void testOnlyOptionalParameterTextFileIssuesError() {
        MainMethodResult result = invokeMain("-textFile");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments."));
    }

    /**
     * Tests that providing the optional parameters '-textFile' and an unknown option issues error and provides necessary information.
     */
    @Test
    void testOptionalParameterTextFileAndUnknownOptionIssuesError() {
        MainMethodResult result = invokeMain("-textFile", "-unknown");
        assertThat(result.getTextWrittenToStandardError(), containsString("An unknown option was provided."));
    }

    /**
     * Tests that providing an unknown option with Airline name issues error and provides necessary information.
     */
    @Test
    void testUnknownOptionalParameterWithAirlineNameIssuesError() {
        MainMethodResult result = invokeMain("-unknown", "Delta Airlines");
        assertThat(result.getTextWrittenToStandardError(), containsString("An unknown option was provided."));
    }

    /**
     * Tests that providing an Airline name before the '-print' option issues error and provides necessary information.
     */
    @Test
    void testAirlineNameBeforePrintIssuesError() {
        MainMethodResult result = invokeMain("Delta Airlines", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments."));
    }

    /**
     * Tests that providing the optional parameter '-print' with only Airline name issues error and provides necessary information.
     */
    @Test
    void testOptionalParameterPrintWithOnlyAirlineNameIssuesError() {
        MainMethodResult result = invokeMain("-print", "Delta Airlines");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments."));
    }

    /**
     * Tests that providing the optional parameters '-print', 'textFile' and '-unknown' issues error and provides necessary information.
     */
    @Test
    void testOptionalParametersPrintTextFileAndUnknownIssuesError() {
        MainMethodResult result = invokeMain("-print", "-textFile", "-unknown");
        assertThat(result.getTextWrittenToStandardError(), containsString("An unknown option was provided."));
    }

    /**
     * Tests that providing the optional parameters '-print', 'textFile' and flight number issues error and provides necessary information.
     */
    @Test
    void testOptionalParametersPrintTextFileAndFlightNumberIssuesError() {
        MainMethodResult result = invokeMain("-print", "-textFile", "789");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments."));
    }

    /**
     * Tests that providing the optional parameters '-print', 'textFile' and '-README' provides project description
     */
    @Test
    void testOptionalParametersPrintTextFileAndReadMeProvidesProjectDescription() {
        MainMethodResult result = invokeMain("-print", "-textFile", "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an 'Airline' project."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-textFile' in incorrect order issues error.
     */
    @Test
    void testOptionalParametersPrintAndTextFileInIncorrectOrderIssuesError() {
        MainMethodResult result = invokeMain("IndiGo", "/airline/file.txt", "-textFile", "789", "PUN", "1/12/2023", "11:59", "PM", "HYD", "01/22/2023", "3:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide text filename and valid flight and airline information after the option '-textFile'."));
    }

    /**
     * Tests that providing the optional parameter'-textFile' and filename with valid airline and flight information creates and writes airline and flight information successfully..
     */
    @Test
    void testOptionalParametersTextFileFilenameAndFlightInfoCreatesAndWritesSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-print', '-textFile' and filename with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintTextFileFilenameAndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-pretty' with no filename/-, '-textFile' and filename with valid airline and flight information issues error.
     */
    @Test
    void testOptionalParametersPrettyWithNoFileTextFileFilenameAndFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/11/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'."));
    }

    /**
     * Tests that providing the optional parameters '-print', '-textFile' and filename with different airline names issues error.
     */
    @Test
    void testOptionalParametersPrintTextFileFilenameAndDifferentAirlineNamesIssuesError(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:50", "AM", "LON", "01/2/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "Frontier", "789", "PUN", "1/12/2023", "11:03", "AM", "HYD", "01/22/2023", "3:00", "PM");
        assertThat(result2.getTextWrittenToStandardError(), containsString("The airline name provided in the input does not match with airline name in the input text file."));
    }

    /**
     * Tests that providing the optional parameters '-textFile' filename  and '-print' with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersTextFileFilenamePrintAndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/filenew.txt", "-print", "IndiGo", "789", "PUN", "1/12/2023", "11:59", "am", "HYD", "01/22/2023", "8:05", "pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PUN at 1/12/2023 11:59 am arrives HYD at 01/22/2023 8:05 pm"));
    }

    /**
     * Tests that providing only the optional parameters '-pretty' issues error and provides necessary information.
     */
    @Test
    void testOnlyOptionalParameterPrettyIssuesError() {
        MainMethodResult result = invokeMain("-pretty");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments."));
    }

    /**
     * Tests that providing the optional parameter'-pretty' and filename with valid airline and flight information creates and pretty prints to file the airline and flight information successfully..
     */
    @Test
    void testOptionalParametersPrettyFilenameAndFlightInfoCreatesAndWritesSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyPrintTest.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyFilenameAndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 1 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder1AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 2 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder2AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 3 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder3AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-print", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 4 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder4AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "-print", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 5 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder5AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "-print", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 6 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder6AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "-print", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs KOL at 1/1/2023 10:59 AM arrives LON at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename with insufficient airline and flight information issues error.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameAndInsufficientFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/11/2023", "10:59", "AM", "LON", "01/2/2023", "2:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'."));
    }

    /**
     * Tests that providing the optional parameter '-print' along with airline and flight information having arrival date and time before departure date and time issues error.
     */
    @Test
    void testOptionalParameterPrintAndEarlyArrivalDateIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "2:59", "PM", "HYD", "01/12/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("The provided arrival date and time should not be before the departure date and time!"));
    }

    /**
     * Tests that providing the optional parameter '-print' with malformed departure time issues error.
     */
    @Test
    void testOptionalParameterPrintWithMalformedTimeIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "Test4", "123", "PDX", "03/03/2023", "12:XX", "PM", "ORD", "03/03/2023", "6:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid departure time. Please provide the departure time in 12-hour(hh:mm) format."));
    }

    /**
     * Tests that providing the optional parameter '-print' with malformed arrival date issues error.
     */
    @Test
    void testOptionalParameterPrintWithMalformedDateIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "Test4", "123", "PDX", "03/03/2023", "12:01", "PM", "ORD", "01/04/20/1", "6:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid arrival date. Please provide the arrival date in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-textFile' and filename in order 1 with valid airline and flight information creates airline and flight successfully.
     */
    @Test
    void testOptionalParametersPrettyAndTextFilenameInOrder1AndFlightInfoCreatesAirlineSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-textFile' and filename in order 2 with valid airline and flight information creates airline and flight successfully.
     */
    @Test
    void testOptionalParametersPrettyAndTextFilenameInOrder2AndFlightInfoCreatesAirlineSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-textFile' and filename in incorrect order 2 issues error.
     */
    @Test
    void testOptionalParametersPrettyAndTextFilenameInIncorrectOrderAndFlightInfoCreatesAirlineSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "-pretty", tempDir + "/prettyfile.txt", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide text filename and/or standard output symbol(-) and valid flight and airline information after the options '-textFile' and '-pretty'."));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename with insufficient airline and flight data issues error.
     */
    @Test
    void testOptionalParametersPrintPrettyAndTextFilenameAndInsufficientFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "LON", "01/2/2023", "2:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'"));
    }

}