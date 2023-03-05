package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Disabled
    @Test
    void test0RemoveAllMappings() throws IOException {
        AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllDictionaryEntries();
    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project5.class);
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing only the optional parameter '-README' provides the details about Project.
     */
    @Test
    void test2OnlyOptionalParameterREADMEProvidesProjectDetails() {
        MainMethodResult result = invokeMain(Project5.class, "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Assignment Name: CS510 Advance Java Project 5"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-host' without '-port' and total 6 args issues error.
     */
    @Test
    void test3HostOptionWithoutPortAnd6ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-port' without '-host' and total 6 args issues error.
     */
    @Test
    void test4PortOptionWithoutHostAnd6ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 1 pretty prints the flight information.
     */
    @Test
    void test5HostPortSearchParamInOrder1PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Frontier");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 2 pretty prints the flight information.
     */
    @Test
    void test6HostPortSearchParamInOrder2PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-search", "Frontier");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 3 pretty prints the flight information.
     */
    @Test
    void test7HostPortSearchParamInOrder3PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "-host", HOSTNAME, "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 4 pretty prints the flight information.
     */
    @Test
    void test8HostPortSearchParamInOrder4PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "-port", PORT, "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 5 pretty prints the flight information.
     */
    @Test
    void test9HostPortSearchParamInOrder5PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 6 pretty prints the flight information.
     */
    @Test
    void test10HostPortSearchParamInOrder6PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Frontier", "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' with non-existing file and total 6 args in order 1 issues error that flight does not exist.
     */
    @Test
    void test11HostPortSearchParamInOrder6WithNonExistentFlightIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Searched airline does not exist"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search' and '-print' and total 7 args issues error.
     */
    @Test
    void test12HostPortSearchAndPrintWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search' with airline name and only source airport code and total 7 args issues error.
     */
    @Test
    void test13HostPortSearchAndOnlySrcAirportCodeWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "PDX");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_DEST_AIRPORT_CODE_ALONG_WITH_SOUCRE_AIRPORT_CODE));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search' with airline name and extra param and total 7 args issues error.
     */
    @Test
    void test14HostPortSearchAndExtraWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and few airline details and total 7 args issues error.
     */
    @Test
    void test15HostPortAndFewAirlineDetailsWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Frontier", "589", "PDX");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-host' without '-port' and total 8 args issues error.
     */
    @Test
    void test16HostOptionWithoutPortAnd8ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "PDX", "LAS", "test1", "test2");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-port' without '-host' and total 6 args issues error.
     */
    @Test
    void test17PortOptionWithoutHostAnd8ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Frontier", "PDX", "LAS", "test1", "test2");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 1 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test18HostPortSearchParamInOrder1PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 2 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test19HostPortSearchParamInOrder2PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 3 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test20HostPortSearchParamInOrder3PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "PDX", "LAS", "-host", HOSTNAME, "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 4 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test21HostPortSearchParamInOrder4PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "PDX", "LAS", "-port", PORT, "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 5 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test22HostPortSearchParamInOrder5PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "PDX", "LAS", "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 6 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test23HostPortSearchParamInOrder6PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Frontier", "PDX", "LAS", "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' with non-existing file and total 8 args in order 1 issues error that flight does not exist.
     */
    @Test
    void test24HostPortSearchParamInOrder1WithNonExistentFlightIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Searched airline does not exist"));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' with non-existing file, '-print' and total 8 args in order 1 issues error.
     */
    @Test
    void test25HostPortSearchParamInOrder1WithPrintOptionIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "-print", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 9 args in order 1 issues error.
     */
    @Test
    void test26HostPortSearchParamInOrder1WithPrintOptionIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "PDX", "LAS", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and insufficient airline details and total 10 args in order 1 issues error.
     */
    @Test
    void test27HostPortParamInOrder1WithInsufficientAirlineDetailsIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Frontier", "789", "PDX", "1/12/2023", "23:59", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters airline details and total 10 args just creates airline and flight but does not output anything.
     */
    @Test
    void test28SufficientAirlineDetailsCreatesDataSuccessfully() {
        MainMethodResult result = invokeMain(Project5.class, "IndiGo", "789", "ORD", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters airline details, '-print' and total 11 args just creates airline and flight but does not output anything.
     */
    @Test
    void test29SufficientAirlineDetailsWithPrintCreatesDataSuccessfullyAndPrints() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PHL at 1/12/2023 12:59 AM arrives MSP at 01/22/2023 1:00 PM"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters airline details and total 11 args issues error.
     */
    @Test
    void test30ExtraAirlineDetailsWith11ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM", "test");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing the optional parameters airline details and total 12 args issues error.
     */
    @Test
    void test31ExtraAirlineDetailsWith12ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing the optional parameters airline details and total 12 args issues error.
     */
    @Test
    void test32HostWithoutPortAirlineDetailsWith12ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME));
    }

    /**
     * Tests that providing the optional parameters airline details and total 12 args issues error.
     */
    @Test
    void test33PortWithoutHostAirlineDetailsWith12ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER));
    }

    /**
     * Tests that providing the optional parameters airline details and total 13 args issues error.
     */
    @Test
    void test34PortWithoutHostAirlineDetailsWith13ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 1 adds flight to the server.
     */
    @Test
    void test35HostPortAirlineDetailsWith14ParamInOrder1AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 2 adds flight to the server.
     */
    @Test
    void test36HostPortAirlineDetailsWith14ParamInOrder2AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', '-print', airline details and total 14 args issues error.
     */
    @Test
    void test37HostPortAirlineDetailsAndPrintWith14ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "-host", HOSTNAME, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 1 prints
     * the newly added flight information.
     */
    @Test
    void test38HostPortPrintParamInOrder1PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 2 prints
     * the newly added flight information.
     */
    @Test
    void test39HostPortPrintParamInOrder2PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 3 prints
     * the newly added flight information.
     */
    @Test
    void test40HostPortPrintParamInOrder3PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "-host", HOSTNAME, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 4 prints
     * the newly added flight information.
     */
    @Test
    void test40HostPortPrintParamInOrder4PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "-port", PORT, "-host", HOSTNAME, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 5 prints
     * the newly added flight information.
     */
    @Test
    void test41HostPortPrintParamInOrder5PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-print", "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Disabled
    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 6 prints
     * the newly added flight information.
     */
    @Test
    void test42HostPortPrintParamInOrder6PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-print", "-host", HOSTNAME, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 16 args issues error.
     */
    @Test
    void test43HostPortPrintParamInOrder1WithExtraParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM", "test");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and airline name and total 5 args issues error.
     */
    @Test
    void test44HostPortWithAirlineNameIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "IndiGo");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search', '-unknown' and airline name and total 5 args issues error.
     */
    @Test
    void test45HostPortSearchAndUnknownOptionWithAirlineNameIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "IndiGo", "-unknown");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.AN_UNKNOWN_OPTION_WAS_PROVIDED));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }


    @Disabled
    @Test
    void test2EmptyServer() {
        MainMethodResult result = invokeMain(Project5.class, HOSTNAME, PORT);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(PrettyPrinter.formatWordCount(0)));
    }

    @Disabled
    @Test
    void test3NoDefinitionsThrowsAppointmentBookRestException() {
        String word = "WORD";
        try {
            invokeMain(Project5.class, HOSTNAME, PORT, word);
            fail("Should have thrown a RestException");

        } catch (UncaughtExceptionInMain ex) {
            RestException cause = (RestException) ex.getCause();
            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        }
    }

    @Disabled
    @Test
    void test4AddDefinition() {
        String word = "WORD";
        String definition = "DEFINITION";

        MainMethodResult result = invokeMain(Project5.class, HOSTNAME, PORT, word, definition);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(Messages.definedWordAs(word, definition)));

        result = invokeMain(Project5.class, HOSTNAME, PORT, word);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(word, definition)));

        result = invokeMain(Project5.class, HOSTNAME, PORT);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(word, definition)));
    }
}