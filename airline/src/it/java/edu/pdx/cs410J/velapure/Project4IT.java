package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for the {@link Project4} main class.
 */
class Project4IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project4} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project4.class, args);
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
        MainMethodResult result = invokeMain("IndiGo", "789", "ORD");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments"));
    }

    /**
     * Tests that invoking the main method with more than ten arguments (including optional arguments) issues an error
     * and provides user with necessary information
     */
    @Test
    void testTooManyCommandLineArguments() {
        MainMethodResult result = invokeMain("IndiGo", "789", "ORD", "01/22/2023", "14:30", "PDX", "01/22/2023", "16:30", "PHL", "586", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide valid flight and airline information after the '-print' option."));
    }

    /**
     * Tests that providing flight number with value that is not numeric issues an error
     */
    @Test
    void testFlightNumberContainsOnlyNumbers() {
        MainMethodResult result = invokeMain("IndiGo", "789c", "PHL", "01/22/2023", "2:30", "PM", "PDX", "01/22/2023", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Flight number provided should consist of only numbers between 0-9."));
    }

    /**
     * Tests that providing source location with value that is not alphabetic (a-zA-Z) or contains alphabets more or less than 3 issues an error
     */
    @Test
    void testSourceLocationContainsOnlyThreeAlphabets() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PDX1#", "01/22/2023", "4:30", "AM", "ORD", "01/22/2023", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Source location provided should consist of only three alphabets [a-zA-Z]"));
    }

    /**
     * Tests that providing departure date in format other than mm/dd/yyyy issues an error
     */
    @Test
    void testDepartureDateIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "MSN", "15/12/2023", "1:30", "AM", "MSP", "01/22/2023", "10:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid departure date. Please provide the departure date in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing departure time in format other than 24-hour time hh:mm issues an error
     */
    @Test
    void testDepartureTimeIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "LNK", "1/12/2023", "23:60", "AM", "PDX", "01/22/2023", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid departure time. Please provide the departure time in 12-hour(hh:mm) format"));
    }

    /**
     * Tests that providing destination location with value that is not alphabetic (a-zA-Z) or contains alphabets more or less than 3 issues an error
     */
    @Test
    void testDestinationLocationContainsOnlyThreeAlphabets() {
        MainMethodResult result = invokeMain("IndiGo", "789", "LNK", "01/22/2023", "11:30", "am", "PHLD", "01/22/2023", "9:30", "pm");
        assertThat(result.getTextWrittenToStandardError(), containsString("Destination location provided should consist of only three alphabets [a-zA-Z]"));
    }

    /**
     * Tests that providing arrival date in format other than mm/dd/yyyy issues an error
     */
    @Test
    void testArrivalDateIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PIT", "1/3/2023", "4:30", "PM", "MSP", "1/22/23", "6:30", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid arrival date. Please provide the arrival date in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing arrival time in format other than 24-hour time hh:mm issues an error
     */
    @Test
    void testArrivalTimeIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PDX", "1/12/2023", "3:59", "PM", "LNK", "01/22/2023", "24:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Invalid arrival time. Please arrival the arrival time in 12-hour(hh:mm) format."));
    }

    /**
     * Tests that providing the optional parameter '-print' with less airline and flight information issues error.
     */
    @Test
    void testOptionalParameterPrintWithLessFlightAndAirlineInformationIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PHL", "1/12/2023", "2:09", "PM", "MSP", "01/22/2023", "5:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the optional parameter '-print' with airline and flight information along with extra parameters issues error.
     */
    @Test
    void testOptionalParameterPrintWithFlightAndAirlineInformationAndExtraParamIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "MSP", "1/12/2023", "3:59", "PM", "PDX", "01/22/2023", "9:00", "AM", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments. Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the airline and flight information without any options creates the flight successfully with no error.
     */
    @Test
    void testAirlineAndFlightCreatedSuccessfully() {
        MainMethodResult result = invokeMain("IndiGo", "789", "ORD", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameter '-print' prints the flight description correctly.
     */
    @Test
    void testOptionalParameterPrintProvidesTheFlightDescription() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PHL at 1/12/2023 12:59 AM arrives MSP at 01/22/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameter '-print' not at the beginning but elsewhere in the command line arguments issues an error.
     */
    @Test
    void testOptionalParameterPrintNotProvidedAtStartIssuesError() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PDX", "1/12/2023", "12:59", "AM", "ORD", "01/22/2023", "1:00", "PM", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide valid flight and airline information after the '-print' option."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-textFile' with insufficient airline and flight details issues error.
     */
    @Test
    void testOptionalParametersPrintAndTextFileWithInSufficientDetailsIssuesError() {
        MainMethodResult result = invokeMain("-print", "-textFile", "/airline/file.txt", "IndiGo", "789", "ORD", "1/12/2023", "11:59", "PM", "PDX", "01/22/2023");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename and valid flight and airline information along with '-textFile'."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-textFile' with insufficient airline and flight details issues error.
     */
    @Test
    void testOptionalParametersPrintAndTextFileWithInSufficientAirlineDetailsIssuesError() {
        MainMethodResult result = invokeMain("-print", "-textFile", "/airline/file.txt", "IndiGo", "789", "PHL", "1/12/2023", "11:59", "PM", "LNK", "01/22/2023", "2:50");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename and valid flight and airline information along with '-print' and '-textFile'."));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and '-textFile' with insufficient airline and flight details issues error.
     */
    @Test
    void testOptionalParametersPrettyAndTextFileWithInSufficientAirlineDetailsIssuesError() {
        MainMethodResult result = invokeMain("-pretty", "-textFile", "/airline/file.txt", "IndiGo", "789", "PIT", "1/12/2023", "11:59", "PM", "PHL", "01/22/2023", "2:50");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'."));
    }

    /**
     * Tests that providing unknown optional parameter issues an error and provides necessary information.
     */
    @Test
    void testUnknownOptionalParameterIssuesError() {
        MainMethodResult result = invokeMain("-trace", "IndiGo", "789", "PDX", "1/12/2023", "23:59", "LNK", "01/22/2023", "23:00");
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
        MainMethodResult result = invokeMain("IndiGo", "789", "LNK", "1/12/2023", "23:59", "PIT", "01/22/2023", "23:00", "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("This is an 'Airline' project."));
    }

    /**
     * Tests that providing the optional parameters '-print' and '-README' prints the details about Project1.
     */
    @Test
    void testOptionalParametersPrintAndREADMEProvidesOnlyProject1Details() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PIT", "1/12/2023", "23:59", "LNK", "01/22/2023", "23:00", "-print", "-README");
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
        MainMethodResult result = invokeMain("IndiGo", "/airline/file.txt", "-textFile", "789", "PDX", "1/12/2023", "11:59", "PM", "MSP", "01/22/2023", "3:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide text filename and valid flight and airline information after the option '-textFile'."));
    }

    /**
     * Tests that providing the optional parameter'-textFile' and filename with valid airline and flight information creates and writes airline and flight information successfully..
     */
    @Test
    void testOptionalParametersTextFileFilenameAndFlightInfoCreatesAndWritesSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "LNK", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-print', '-textFile' and filename with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintTextFileFilenameAndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "LNK", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs PDX at 1/1/2023 10:59 AM arrives LNK at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-pretty' with no filename/-, '-textFile' and filename with valid airline and flight information issues error.
     */
    @Test
    void testOptionalParametersPrettyWithNoFileTextFileFilenameAndFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "ORD", "1/11/2023", "10:59", "AM", "PHL", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'."));
    }

    /**
     * Tests that providing the optional parameters '-print', '-textFile' and filename with different airline names issues error.
     */
    @Test
    void testOptionalParametersPrintTextFileFilenameAndDifferentAirlineNamesIssuesError(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "PDX", "1/1/2023", "10:50", "AM", "LNK", "01/2/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "Frontier", "789", "ORD", "1/12/2023", "11:03", "AM", "PHL", "01/22/2023", "3:00", "PM");
        assertThat(result2.getTextWrittenToStandardError(), containsString("The airline name provided in the input does not match with airline name in the input text file."));
    }

    /**
     * Tests that providing the optional parameters  '-textFile' and filename with multiple flights are sorted correctly.
     */
    @Test
    void testOptionalParametersTextFileFilenameWithMultipleFlightsSortsCorrectly(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-textFile", tempDir + "/testfile.txt", "Frontier", "448", "PDX", "1/1/2023", "10:50", "AM", "LNK", "01/2/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-textFile", tempDir + "/testfile.txt", "Frontier", "789", "ORD", "1/12/2023", "11:03", "AM", "PHL", "01/22/2023", "3:00", "PM");
        assertThat(result2.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result2.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-textFile' filename  and '-print' with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersTextFileFilenamePrintAndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/filenew.txt", "-print", "IndiGo", "789", "PDX", "1/12/2023", "11:59", "am", "PIT", "01/22/2023", "8:05", "pm");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PDX at 1/12/2023 11:59 am arrives PIT at 01/22/2023 8:05 pm"));
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
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyPrintTest.txt", "Indi-Go", "448", "PDX", "1/1/2023", "10:00", "AM", "LNK", "01/1/2023", "11:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyFilenameAndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/testfile.txt", "Indi-Go", "448", "PHL", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs PHL at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 1 with
     * valid airline and flight information prints the flight data to the terminal and sorts the flights starting from same src correctly.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder1AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-textFile", tempDir + "/testfile.txt", "Frontier", "448", "PDX", "1/1/2023", "10:59", "AM", "LNK", "01/1/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-textFile", tempDir + "/testfile.txt", "Frontier", "789", "ORD", "1/12/2023", "11:03", "AM", "PHL", "01/12/2023", "3:00", "PM");
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "Frontier", "558", "PDX", "1/1/2023", "10:59", "AM", "ORD", "01/1/2023", "4:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 558 departs PDX at 1/1/2023 10:59 AM arrives ORD at 01/1/2023 4:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 2 with
     * valid airline and flight information prints the flight data to the terminal and sorts the flights starting from same src and same time correctly.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder2AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "PIT", "1/1/2023", "10:59", "AM", "LNK", "01/1/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "789", "MSP", "1/12/2023", "11:03", "AM", "PHL", "01/12/2023", "3:00", "PM");
        MainMethodResult result = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "223", "PIT", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "5:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 223 departs PIT at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 5:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and standard output and '-textFile' and filename in order 3 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder3AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "PIT", "1/1/2023", "10:59", "AM", "LNK", "01/1/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "789", "PDX", "1/12/2023", "11:03", "AM", "PHL", "01/12/2023", "3:00", "PM");
        MainMethodResult result = invokeMain("-pretty", "-", "-print", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "112", "LNK", "1/1/2023", "10:59", "AM", "ORD", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 112 departs LNK at 1/1/2023 10:59 AM arrives ORD at 01/2/2023 2:00 AM"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Indi-Go airline"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 4 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder4AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "-print", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "Pdx", "1/1/2023", "10:59", "AM", "ord", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs PDX at 1/1/2023 10:59 AM arrives ORD at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 5 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder5AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "-print", "Indi-Go", "448", "Lnk", "1/1/2023", "10:59", "AM", "mSp", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs LNK at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename in order 6 with valid airline and flight information prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameInOrder6AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "-print", "Indi-Go", "448", "MSN", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs MSN at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename with insufficient airline and flight information issues error.
     */
    @Test
    void testOptionalParametersPrintPrettyTextFilenameAndInsufficientFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "PHL", "1/11/2023", "10:59", "AM", "ORD", "01/2/2023", "2:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'."));
    }

    /**
     * Tests that providing the optional parameter '-print' along with airline and flight information having arrival date and time before departure date and time issues error.
     */
    @Test
    void testOptionalParameterPrintAndEarlyArrivalDateIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "Phl", "1/12/2023", "2:59", "PM", "Ord", "01/12/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("The provided arrival date and time should not be before or same as the departure date and time!"));
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
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "LNK", "1/1/2023", "10:59", "AM", "ORD", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-textFile' and filename in order 2 with valid airline and flight information creates airline and flight successfully.
     */
    @Test
    void testOptionalParametersPrettyAndTextFilenameInOrder2AndFlightInfoCreatesAirlineSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "PHL", "1/1/2023", "10:59", "AM", "LNK", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-textFile' and filename in incorrect order 2 issues error.
     */
    @Test
    void testOptionalParametersPrettyAndTextFilenameInIncorrectOrderAndFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "-pretty", tempDir + "/prettyfile.txt", "PHL", "1/1/2023", "10:59", "AM", "PDX", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide text filename and/or standard output symbol(-) and valid flight and airline information after the options '-textFile' and '-pretty'."));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-textFile' and filename with insufficient airline and flight data issues error.
     */
    @Test
    void testOptionalParametersPrintPrettyAndTextFilenameAndInsufficientFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-textFile", tempDir + "/testfile.txt", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "Lnk", "1/1/2023", "10:59", "AM", "Pit", "01/2/2023", "2:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'"));
    }

    /**
     * Tests that providing the optional parameters '-print', with unknown flight source location code issues error.
     */
    @Test
    void testOptionalParametersPrintWithUnknownFlightSrcLocationIssuesError() {
        MainMethodResult result = invokeMain("-print", "Indi-Go", "448", "KOL", "1/1/2023", "10:59", "AM", "PDX", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("The three-letter source location code does not correspond to a known airport!"));
    }

    /**
     * Tests that providing the optional parameters '-print', with unknown flight destination location code issues error.
     */
    @Test
    void testOptionalParametersPrintWithUnknownFlightDstLocationIssuesError() {
        MainMethodResult result = invokeMain("-print", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "KOL", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("The three-letter destination location code does not correspond to a known airport!"));
    }

    /**
     * Tests that providing the optional parameters '-print', with same src and destination location code issues error.
     */
    @Test
    void testOptionalParametersPrintWithSameSrcDstLocationIssuesError() {
        MainMethodResult result = invokeMain("-print", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "PDX", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("The provided source and the destination location code is same"));
    }

    /**
     * Tests that providing the optional parameters '-xmlFile', without xml filename and insufficient flight and airline information issues error.
     */
    @Test
    void testOptionalParametersXmlFileWithoutFilenameAndInvalidFlightDataIssuesError() {
        MainMethodResult result = invokeMain("-xmlFile", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "PHL", "01/2/2023", "2:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide xml filename and valid flight and airline information along with '-xmlFile'."));
    }

    /**
     * Tests that providing the optional parameters '-xmlFile', without xml filename and valid flight and airline information issues error.
     */
    @Test
    void testOptionalParametersXmlFileWithoutFilenameButValidFlightIssuesError() {
        MainMethodResult result = invokeMain("-xmlFile", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "PHL", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide xml filename and valid flight and airline information along with '-xmlFile'."));
    }

    /**
     * Tests that providing the optional parameters '-xmlFile' and '-textFile' with valid flight and airline information issues error.
     */
    @Test
    void testOptionalParametersXmlFileAndTextFilenameButValidFlightIssuesError() {
        MainMethodResult result = invokeMain("-xmlFile", "-textFile", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "PHL", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide either '-xmlFile' or '-textFile' option along with filename and valid flight and airline information."));
    }

    /**
     * Tests that providing the optional parameters '-xmlFile' and filename with valid flight and airline information in incorrect order issues error.
     */
    @Test
    void testOptionalParametersXmlFileWithValidFlightButIncorrectOrderIssuesError() {
        MainMethodResult result = invokeMain("Indi-Go", "448", "-xmlFile", "/testFile.xml", "PDX", "1/1/2023", "10:59", "AM", "PHL", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide xml filename and valid flight and airline information after the option '-xmlFile'."));
    }

    /**
     * Tests that providing the optional parameter '-xmlFile' and filename with valid airline and flight information creates and writes airline and flight information successfully..
     */
    @Test
    void testOptionalParametersXmlFileFilenameAndFlightInfoCreatesAndWritesSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "448", "LNK", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-xmlFile' and '-textFile' with valid flight and airline information issues error.
     */
    @Test
    void testOptionalParametersXmlFileAndFilenameAndTextFilenameButValidFlightIssuesError() {
        MainMethodResult result = invokeMain("-xmlFile", "/testXmlfile.xml", "-textFile", "Indi-Go", "448", "PDX", "1/1/2023", "10:59", "AM", "PHL", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide either '-xmlFile' or '-textFile' option along with filename and valid flight and airline information."));
    }

    /**
     * Tests that providing the optional parameter '-xmlFile' and filename and '-print' with valid airline and flight information creates and writes airline and flight information successfully..
     */
    @Test
    void testOptionalParametersXmlFileFilenameAndPrintWithFlightInfoCreatesAndWritesSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "-print", "Indi-Go", "448", "LNK", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs LNK at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 PM"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameter '-print' and '-xmlFile' and filename with valid airline and flight information creates and writes airline and flight information successfully..
     */
    @Test
    void testOptionalParametersPrintAndXmlFileFilenameWithFlightInfoCreatesAndWritesSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "448", "LNK", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs LNK at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 PM"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-xmlFile' and filename in order 1 with valid airline and flight information creates airline and flight successfully.
     */
    @Test
    void testOptionalParametersPrettyAndXmlFilenameInOrder1AndFlightInfoCreatesAirlineSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "448", "LNK", "1/1/2023", "10:59", "AM", "ORD", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-xmlFile' and filename in order 2 with valid airline and flight information creates airline and flight successfully.
     */
    @Test
    void testOptionalParametersPrettyAndXmlFilenameInOrder2AndFlightInfoCreatesAirlineSuccessfully(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "PHL", "1/1/2023", "10:59", "AM", "LNK", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-pretty' and filename and '-xmlFile' and filename in incorrect order 2 issues error.
     */
    @Test
    void testOptionalParametersPrettyAndXmlFilenameInIncorrectOrderAndFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "448", "-pretty", tempDir + "/prettyfile.txt", "PHL", "1/1/2023", "10:59", "AM", "PDX", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide xml filename and/or standard output symbol(-) and valid flight and airline information after the options '-xmlFile' and '-pretty'."));
    }

    /**
     * Tests that providing the optional parameters '-print', '-xmlFile' and filename and '-textFile' with insufficient flight information issues error.
     */
    @Test
    void testOptionalParametersPrintXmlandTextFilenameAndInsufficientFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-xmlFile", tempDir + "/testXmlfile.xml", "-textFile", "Indi-Go", "448", "PHL", "1/1/2023", "10:59", "AM", "PDX", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide either '-xmlFile' or '-textFile' option along with filename and valid flight and airline information."));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-xmlFile' and filename in order 1 with
     * valid airline and flight information prints the flight data to the terminal and sorts the flights starting from same src correctly.
     */
    @Test
    void testOptionalParametersPrintPrettyXmlFilenameInOrder1AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Frontier", "448", "PDX", "1/1/2023", "10:59", "AM", "LNK", "01/1/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Frontier", "789", "ORD", "1/12/2023", "11:03", "AM", "PHL", "01/12/2023", "3:00", "PM");
        MainMethodResult result = invokeMain("-print", "-pretty", tempDir + "/prettyfile.txt", "-xmlFile", tempDir + "/testXmlfile.xml", "Frontier", "558", "PDX", "1/1/2023", "10:59", "AM", "ORD", "01/1/2023", "4:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 558 departs PDX at 1/1/2023 10:59 AM arrives ORD at 01/1/2023 4:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-xmlFile' and filename in order 2 with
     * valid airline and flight information prints the flight data to the terminal and sorts the flights starting from same src and same time correctly.
     */
    @Test
    void testOptionalParametersPrintPrettyXmlFilenameInOrder2AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "448", "PIT", "1/1/2023", "10:59", "AM", "LNK", "01/1/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "789", "MSP", "1/12/2023", "11:03", "AM", "PHL", "01/12/2023", "3:00", "PM");
        MainMethodResult result = invokeMain("-print", "-xmlFile", tempDir + "/testXmlfile.xml", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "223", "PIT", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "5:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 223 departs PIT at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 5:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and standard output and '-xmlFile' and filename in order 3 with valid airline and flight information
     * prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyXmlFilenameInOrder3AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result1 = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "448", "PIT", "1/1/2023", "10:59", "AM", "LNK", "01/1/2023", "2:00", "PM");
        MainMethodResult result2 = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "789", "PDX", "1/12/2023", "11:03", "AM", "PHL", "01/12/2023", "3:00", "PM");
        MainMethodResult result = invokeMain("-pretty", "-", "-print", "-xmlFile", tempDir + "/testXmlfile.xml", "Indi-Go", "112", "LNK", "1/1/2023", "10:59", "AM", "ORD", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 112 departs LNK at 1/1/2023 10:59 AM arrives ORD at 01/2/2023 2:00 AM"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Indi-Go airline"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-xmlFile' and filename in order 4 with valid airline and flight information
     * prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyXmlFilenameInOrder4AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "-print", "-pretty", tempDir + "/prettyfile.txt", "Indi-Go", "448", "Pdx", "1/1/2023", "10:59", "AM", "ord", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs PDX at 1/1/2023 10:59 AM arrives ORD at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-xmlFile' and filename in order 5 with valid airline and flight information
     * prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyXmlFilenameInOrder5AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-pretty", tempDir + "/prettyfile.txt", "-xmlFile", tempDir + "/testXmlfile.xml", "-print", "Indi-Go", "448", "Lnk", "1/1/2023", "10:59", "AM", "mSp", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs LNK at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-pretty' and filename and '-xmlFile' and filename in order 6 with valid airline and flight information
     * prints the flight data to the terminal.
     */
    @Test
    void testOptionalParametersPrintPrettyXmlFilenameInOrder6AndFlightInfoPrintsCorrectFlightData(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-xmlFile", tempDir + "/testXmlfile.xml", "-pretty", tempDir + "/prettyfile.txt", "-print", "Indi-Go", "448", "MSN", "1/1/2023", "10:59", "AM", "MSP", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 448 departs MSN at 1/1/2023 10:59 AM arrives MSP at 01/2/2023 2:00 AM"));
    }

    /**
     * Tests that providing the optional parameters '-print', '-xmlFile' and filename and '-textFile' with valid flight information issues error.
     */
    @Test
    void testOptionalParametersPrintXmlandTextFilenameAndValidFlightInfoIssuesError(@TempDir File tempDir) {
        MainMethodResult result = invokeMain("-print", "-xmlFile", tempDir + "/testXmlfile.xml", "-textFile", tempDir + "/testfile.txt", "Indi-Go", "448", "PHL", "1/1/2023", "10:59", "AM", "PDX", "01/2/2023", "2:00", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide either '-xmlFile' or '-textFile' option along with filename and valid flight and airline information."));
    }
}