package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");


    @Test
    void test01RemoveAllMappings() throws IOException {
        AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllAirlineEntries();
    }

    @Test
    void test02NoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project5.class);
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.MISSING_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing only the optional parameter '-README' provides the details about Project.
     */
    @Test
    void test03OnlyOptionalParameterREADMEProvidesProjectDetails() {
        MainMethodResult result = invokeMain(Project5.class, "-README");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Assignment Name: CS510 Advance Java Project 5"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-host' without '-port' and total 6 args issues error.
     */
    @Test
    void test04HostOptionWithoutPortAnd6ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-port' without '-host' and total 6 args issues error.
     */
    @Test
    void test05PortOptionWithoutHostAnd6ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 1 adds flight to the server.
     */
    @Test
    void test06HostPortAirlineDetailsWith14ParamInOrder1AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Spirit", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 1 adds flight to the server.
     */
    @Test
    void test07HostPortAirlineDetailsWith14ParamInOrder1AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Spirit", "4856", "PDX", "3/9/2023", "10:59", "AM", "LAS", "03/9/2023", "4:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 1 adds flight to the server.
     */
    @Test
    void test08HostPortAirlineDetailsWith14ParamInOrder1AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Spirit", "641", "PDX", "3/11/2023", "10:59", "AM", "LAS", "03/11/2023", "4:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 2 adds flight to the server.
     */
    @Test
    void test09HostPortAirlineDetailsWith14ParamInOrder2AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "Frontier", "12345", "PDX", "3/24/2023", "6:00", "PM", "LAS", "3/24/2023", "9:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', airline details and total 14 args in order 2 adds flight to the server.
     */
    @Test
    void test10HostPortAirlineDetailsWith14ParamInOrder2AddsFlightToTheServer() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "Frontier", "4238", "PHL", "3/24/2023", "6:00", "PM", "DFW", "3/24/2023", "9:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host, '-port', '-print', airline details and total 14 args issues error.
     */
    @Test
    void test11HostPortAirlineDetailsAndPrintWith14ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "-host", HOSTNAME, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 1 pretty prints the flight information.
     */
    @Test
    void test12HostPortSearchParamInOrder1PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Frontier");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Frontier airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 12345 details:"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 2 pretty prints the flight information.
     */
    @Test
    void test13HostPortSearchParamInOrder2PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-search", "Spirit");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Spirit airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 details:"));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 3 pretty prints the flight information.
     */
    @Test
    void test14HostPortSearchParamInOrder3PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Spirit", "-host", HOSTNAME, "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Spirit airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 details:"));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 4 pretty prints the flight information.
     */
    @Test
    void test15HostPortSearchParamInOrder4PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "-port", PORT, "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Frontier airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 12345 details:"));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 5 pretty prints the flight information.
     */
    @Test
    void test16HostPortSearchParamInOrder5PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Frontier airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 12345 details:"));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 6 args in order 6 pretty prints the flight information.
     */
    @Test
    void test17HostPortSearchParamInOrder6PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Spirit", "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Spirit airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 details:"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' with non-existing airline and total 6 args in order 1 issues error that flight does not exist.
     */
    @Test
    void test18HostPortSearchParamInOrder6WithNonExistentAirlineIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.SPECIFIED_AIRLINE_DOES_NOT_EXIST));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search' and '-print' and total 7 args issues error.
     */
    @Test
    void test19HostPortSearchAndPrintWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "-print");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search' with airline name and only source airport code and total 7 args issues error.
     */
    @Test
    void test20HostPortSearchAndOnlySrcAirportCodeWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "PDX");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_DEST_AIRPORT_CODE_ALONG_WITH_SOUCRE_AIRPORT_CODE));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search' with airline name and extra param and total 7 args issues error.
     */
    @Test
    void test21HostPortSearchAndExtraWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and few airline details and total 7 args issues error.
     */
    @Test
    void test22HostPortAndFewAirlineDetailsWith7ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Frontier", "589", "PDX");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-host' without '-port' and total 8 args issues error.
     */
    @Test
    void test23HostOptionWithoutPortAnd8ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Frontier", "PDX", "LAS", "test1", "test2");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing only the optional parameter '-port' without '-host' and total 6 args issues error.
     */
    @Test
    void test24PortOptionWithoutHostAnd8ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Frontier", "PDX", "LAS", "test1", "test2");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 1 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test25HostPortSearchParamInOrder1PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "Frontier", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Frontier airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 12345 details:"));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 2 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test26HostPortSearchParamInOrder2PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-search", "Spirit", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Spirit airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 4856 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 641 details:"));
    }


    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 3 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test27HostPortSearchParamInOrder3PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "PDX", "LAS", "-host", HOSTNAME, "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Frontier airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 12345 details:"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 4 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test28HostPortSearchParamInOrder4PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Frontier", "PDX", "LAS", "-port", PORT, "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Frontier airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 12345 details:"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 5 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test29HostPortSearchParamInOrder5PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-search", "Spirit", "PDX", "LAS", "-port", PORT);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Spirit airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 4856 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 641 details:"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 8 args in order 6 pretty prints
     * the flight information from given source to destination airport.
     */
    @Test
    void test30HostPortSearchParamInOrder6PrettyPrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-search", "Spirit", "PDX", "LAS", "-host", HOSTNAME);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the Spirit airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 4856 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 641 details:"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' with non-existing airline and total 8 args in order 1 issues error that flight does not exist.
     */
    @Test
    void test31HostPortSearchParamInOrder1WithNonExistentFlightIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "PDX", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.SPECIFIED_AIRLINE_DOES_NOT_EXIST));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' with non-existing airline, '-print' and total 8 args in order 1 issues error.
     */
    @Test
    void test32HostPortSearchParamInOrder1WithPrintOptionIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "-print", "LAS");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-search' and total 9 args in order 1 issues error.
     */
    @Test
    void test33HostPortSearchParamInOrder1WithPrintOptionIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "SpiceJet", "PDX", "LAS", "extra");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and insufficient airline details and total 10 args in order 1 issues error.
     */
    @Test
    void test34HostPortParamInOrder1WithInsufficientAirlineDetailsIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "Frontier", "789", "PDX", "1/12/2023", "23:59", "AM");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters airline details and total 10 args just creates airline and flight
     * but does not add any flight to the server and does not output anything.
     */
    @Test
    void test35SufficientAirlineDetailsCreatesDataSuccessfully() {
        MainMethodResult result = invokeMain(Project5.class, "IndiGo", "789", "ORD", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters airline details, '-print' and total 11 args just creates airline and
     * flight and prints the flight info. But does not add any flight to the server.
     */
    @Test
    void test36SufficientAirlineDetailsWithPrintCreatesDataSuccessfullyAndPrints() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PHL at 1/12/2023 12:59 AM arrives MSP at 01/22/2023 1:00 PM"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters airline details and total 11 args issues error.
     */
    @Test
    void test37ExtraAirlineDetailsWith11ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM", "test");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing the optional parameters airline details and total 12 args issues error.
     */
    @Test
    void test38ExtraAirlineDetailsWith12ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing the optional parameters airline details and total 12 args issues error.
     */
    @Test
    void test39HostWithoutPortAirlineDetailsWith12ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME));
    }

    /**
     * Tests that providing the optional parameters airline details and total 12 args issues error.
     */
    @Test
    void test40PortWithoutHostAirlineDetailsWith12ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER));
    }

    /**
     * Tests that providing the optional parameters airline details and total 13 args issues error.
     */
    @Test
    void test41PortWithoutHostAirlineDetailsWith13ParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 1 prints
     * the newly added flight information.
     */
    @Test
    void test42HostPortPrintParamInOrder1PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 789 departs PHL at 1/12/2023 12:59 AM arrives MSP at 01/22/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 2 prints
     * the newly added flight information.
     */
    @Test
    void test43HostPortPrintParamInOrder2PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-print", "IndiGo", "896", "PHL", "4/15/2023", "2:50", "PM", "MSP", "04/15/2023", "6:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 896 departs PHL at 4/15/2023 2:50 PM arrives MSP at 04/15/2023 6:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 3 prints
     * the newly added flight information.
     */
    @Test
    void test44HostPortPrintParamInOrder3PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "-host", HOSTNAME, "-port", PORT, "American Airlines", "999", "DFW", "1/10/2023", "12:59", "AM", "PIT", "01/11/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 999 departs DFW at 1/10/2023 12:59 AM arrives PIT at 01/11/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 4 prints
     * the newly added flight information.
     */
    @Test
    void test45HostPortPrintParamInOrder4PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-print", "-port", PORT, "-host", HOSTNAME, "American Airlines", "1000", "PDX", "1/12/2023", "12:59", "AM", "LAS", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1000 departs PDX at 1/12/2023 12:59 AM arrives LAS at 01/22/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 5 prints
     * the newly added flight information.
     */
    @Test
    void test46HostPortPrintParamInOrder5PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-print", "-port", PORT, "American Airlines", "811", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 811 departs PHL at 1/12/2023 12:59 AM arrives MSP at 01/22/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 15 args in order 6 prints
     * the newly added flight information.
     */
    @Test
    void test47HostPortPrintParamInOrder6PrintsFlightInfo() {
        MainMethodResult result = invokeMain(Project5.class, "-port", PORT, "-print", "-host", HOSTNAME, "IndiGo", "97412", "PHL", "1/12/2023", "12:59", "AM", "DFW", "01/22/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 97412 departs PHL at 1/12/2023 12:59 AM arrives DFW at 01/22/2023 1:00 PM"));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and '-print' and total 16 args issues error.
     */
    @Test
    void test48HostPortPrintParamInOrder1WithExtraParamIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-print", "IndiGo", "789", "PHL", "1/12/2023", "12:59", "AM", "MSP", "01/22/2023", "1:00", "PM", "test");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_MANY_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port' and airline name and total 5 args issues error.
     */
    @Test
    void test49HostPortWithAirlineNameIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "IndiGo");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.TOO_FEW_COMMAND_LINE_ARGUMENTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search', '-unknown' and airline name and total 5 args issues error.
     */
    @Test
    void test50HostPortSearchAndUnknownOptionWithAirlineNameIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "IndiGo", "-unknown");
        assertThat(result.getTextWrittenToStandardError(), containsString(Project5.AN_UNKNOWN_OPTION_WAS_PROVIDED));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    /**
     * Tests that providing the optional parameters '-host','-port', '-search', and airline name and src and dest airport code no direct flight exists
     */
    @Test
    void test51HostPortSearchAndUnknownOptionWithAirlineNameIssuesError() {
        MainMethodResult result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", "spirit", "AVP", "PDX");
        assertThat(result.getTextWrittenToStandardError(), containsString(Messages.NO_DIRECT_FLIGHTS));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
    }

    @Test
    void test52CheckAllAirlineFunctionalities() {
        String airlineName = "American Airlines";

        MainMethodResult result = invokeMain(Project5.class, "-print", "-host", HOSTNAME, "-port", PORT, airlineName, "888", "DFW", "1/11/2023", "12:59", "AM", "PIT", "01/12/2023", "1:00", "PM");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 888 departs DFW at 1/11/2023 12:59 AM arrives PIT at 01/12/2023 1:00 PM"));

        result = invokeMain(Project5.class, "-host", HOSTNAME, "-port", PORT, "-search", airlineName);
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the " + airlineName));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 888 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 999 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 1000 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 811 details:"));

        result = invokeMain(Project5.class, "-port", PORT, "-host", HOSTNAME, "-search", airlineName, "DFW", "PIT");
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight Information of the " + airlineName + " airline"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 999 details:"));
        assertThat(result.getTextWrittenToStandardOut(), containsString("Flight 888 details:"));
    }
}