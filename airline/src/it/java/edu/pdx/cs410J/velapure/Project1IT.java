package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * An integration test for the {@link Project1} main class.
 */
class Project1IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain(Project1.class, args);
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
        assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments"));
    }

    /**
     * Tests that providing flight number with value that is not numeric issues an error
     */
    @Test
    void testFlightNumberContainsOnlyNumbers() {
        MainMethodResult result = invokeMain("IndiGo", "789c", "PUN", "01/22/2023", "14:30", "HYD", "01/22/2023", "16:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Flight number provided should consist of only numbers between 0-9."));
    }

    /**
     * Tests that providing source location with value that is not alphabetic (a-zA-Z) or contains alphabets more or less than 3 issues an error
     */
    @Test
    void testSourceLocationContainsOnlyThreeAlphabets() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN1#", "01/22/2023", "14:30", "HYD", "01/22/2023", "16:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Source location provided should consist of only three alphabets [a-zA-Z]"));
    }

    /**
     * Tests that providing departure date in format other than mm/dd/yyyy issues an error
     */
    @Test
    void testDepartureDateIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "15/12/2023", "14:30", "HYD", "01/22/2023", "16:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Departure date provided should be in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing departure time in format other than 24-hour time hh:mm issues an error
     */
    @Test
    void testDepartureTimeIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "23:60", "HYD", "01/22/2023", "16:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Departure time provided should be in 24-hour(hh:mm) format."));
    }

    /**
     * Tests that providing destination location with value that is not alphabetic (a-zA-Z) or contains alphabets more or less than 3 issues an error
     */
    @Test
    void testDestinationLocationContainsOnlyThreeAlphabets() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "01/22/2023", "14:30", "HYDBD", "01/22/2023", "16:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Destination location provided should consist of only three alphabets [a-zA-Z]"));
    }

    /**
     * Tests that providing arrival date in format other than mm/dd/yyyy issues an error
     */
    @Test
    void testArrivalDateIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/3/2023", "14:30", "HYD", "1/22/23", "16:30");
        assertThat(result.getTextWrittenToStandardError(), containsString("Arrival date provided should be in mm/dd/yyyy format."));
    }

    /**
     * Tests that providing arrival time in format other than 24-hour time hh:mm issues an error
     */
    @Test
    void testArrivalTimeIsProvidedInCorrectFormat() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "24:00");
        assertThat(result.getTextWrittenToStandardError(), containsString("Arrival time provided should be in 24-hour(hh:mm) format."));
    }

    /**
     * Tests that providing only the optional parameter '-print' issues error and provides necessary information.
     */
    @Test
    void testOnlyOptionalParameterPrintIssuesError() {
        MainMethodResult result = invokeMain("-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the optional parameter '-print' with less airline and flight information issues error.
     */
    @Test
    void testOptionalParameterPrintWithLessFlightAndAirlineInformationIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too few command line arguments. Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the optional parameter '-print' with airline and flight information along with extra parameters issues error.
     */
    @Test
    void testOptionalParameterPrintWithFlightAndAirlineInformationAndExtraParamIssuesError() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "23:00", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString("Too many command line arguments. Please provide valid flight and airline information along with '-print'."));
    }

    /**
     * Tests that providing the optional parameter '-print' prints the flight description correctly.
     */
    @Test
    void testOptionalParameterPrintProvidesTheFlightDescription() {
        MainMethodResult result = invokeMain("-print", "IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "23:00");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PUN at 1/12/2023 23:59 arrives HYD at 01/22/2023 23:00"));
    }

    /**
     * Tests that providing the optional parameter '-print' not at the beginning but elsewhere in the command line arguments issues an error.
     */
    @Test
    void testOptionalParameterNotProvidedAtStartIssuesError() {
        MainMethodResult result = invokeMain("IndiGo", "789", "PUN", "1/12/2023", "23:59", "HYD", "01/22/2023", "23:00", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString("Please provide '-print' option before the flight and airline information."));
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
}