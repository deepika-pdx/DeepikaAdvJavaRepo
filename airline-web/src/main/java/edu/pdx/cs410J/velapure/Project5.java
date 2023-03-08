package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    /**
     * Usage information of this project.
     */
    private static final String USAGE = "usage: java -jar target/airline-client.jar [options] <args>" + "\n" +
            "args are (in this order):" + "\n" +
            "airline" + "   " + "The name of the airline" + "\n" +
            "flightNumber" + "   " + "The flight number" + "\n" +
            "source" + "   " + "Three-letter code of the departure airport" + "\n" +
            "departure date" + "   " + "Departure date in mm/dd/yyyy format" + "\n" +
            "departure time" + "   " + "Departure time in 12-hour time format" + "\n" +
            "departure time indication" + "   " + "Departure time indication i.e. AM/am or PM/pm" + "\n" +
            "destination" + "   " + "Three-letter code of the arrival airport" + "\n" +
            "arrival date" + "   " + "Arrival date in mm/dd/yyyy format" + "\n" +
            "arrival time" + "   " + "Arrival time in 12-hour time format" + "\n" + "\n" +
            "arrival time indication" + "   " + "Arrival time indication i.e. AM/am or PM/pm" + "\n" +

            "options are (Options may appear in any order. Please do not provide '-search' and '-print' option together.) :" + "\n" +
            "-host hostname" + "   " + "Host computer on which the server runs" + "\n" +
            "-port port" + "   " + "Port on which the server is listening" + "\n" +
            "-search" + "   " + "Search for flights" + "\n" +
            "-print" + "   " + "Prints the description of the newly added flight" + "\n" +
            "-README" + "   " + "Prints the README for this project and exits";

    /**
     * User-understandable error message when too few command line arguments are passed.
     */
    public static final String MISSING_COMMAND_LINE_ARGUMENTS = "Missing command line arguments. ";

    /**
     * User-understandable error message when too few command line arguments are passed.
     */
    public static final String TOO_FEW_COMMAND_LINE_ARGUMENTS = "Too few command line arguments. ";

    /**
     * User-understandable error message when too many command line arguments are passed.
     */
    public static final String TOO_MANY_COMMAND_LINE_ARGUMENTS = "Too many command line arguments. ";

    /**
     * User-understandable error message for providing port number along with host name.
     */
    public static final String PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME = "Please provide port number along with hostname. ";

    /**
     * User-understandable error message for providing hostname number along with port number.
     */
    public static final String PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER = "Please provide hostname along with port number. ";

    /**
     * User-understandable error message for not providing '-search' and '-print' together.
     */
    public static final String PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER = "Please do not provide '-search' and '-print' optional parameters together. ";

    /**
     * User-understandable error message for providing destination airport code along with source airport code.
     */
    public static final String PLEASE_PROVIDE_DEST_AIRPORT_CODE_ALONG_WITH_SOUCRE_AIRPORT_CODE = "Please provide destination airport code along with source airport code. ";

    /**
     * User-understandable error message for an unknown option provided.
     */
    public static final String AN_UNKNOWN_OPTION_WAS_PROVIDED = "An unknown option was provided. ";

    /**
     * User-understandable error message for an non-existent airline name provided.
     */
    public static final String SPECIFIED_AIRLINE_DOES_NOT_EXIST = "The specified airline does not exist. ";


    public static void main(String... args) {
        if (args != null) {
            try {
                if (Arrays.stream(args).anyMatch("-README"::equals)) {
                    System.out.println("Project Description: ");
                    readContentFromREADME();
                    return;
                }

                int argLength = args.length;
                switch (argLength) {
                    case 0:
                        // Missing command line arguments
                        throw new AirlineException(MISSING_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                    case 6:
                        // Display all the flights of the airline specified with the '-search' parameter
                        searchAndDisplayAllFlightsOfTheSpecifiedAirline(args);
                        break;
                    case 7:
                        processInvalidScenariosWith7Param(args);
                    case 8:
                        searchAndDisplayOnlyThoseFlightWithSpecifiedSrcAndDest(args);
                        break;
                    case 9:
                        processInvalidScenariosWith9Param(args);
                    case 10:
                        processAirlineDetailsWithoutOptions(args);
                        break;
                    case 11:
                        processAirlineDetailsWithOnlyPrintOption(args);
                        break;
                    case 12:
                        processInvalidScenariosWith12Param(args);
                    case 13:
                        processInvalidScenariosWith13Param(args);
                    case 14:
                        addFlightToTheSpecifiedAirlineWithoutPrintOption(args);
                        break;
                    case 15:
                        addFlightToTheSpecifiedAirlineWithPrintOption(args);
                        break;
                }
                if (argLength < 6) {
                    throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                }
                if (argLength > 15) {
                    throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                }
            } catch (AirlineException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void addFlightToTheSpecifiedAirlineWithPrintOption(String[] args) {
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (args[0].equals("-host") && args[2].equals("-port") && args[4].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    args[14], Optional.of(args[1]), Optional.of(args[3]), Optional.of("printFlightInformation"));
        } else if (args[0].equals("-port") && args[2].equals("-host") && args[4].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    args[14], Optional.of(args[3]), Optional.of(args[1]), Optional.of("printFlightInformation"));
        } else if (args[0].equals("-print") && args[1].equals("-host") && args[3].equals("-port")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    args[14], Optional.of(args[2]), Optional.of(args[4]), Optional.of("printFlightInformation"));
        } else if (args[0].equals("-print") && args[1].equals("-port") && args[3].equals("-host")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    args[14], Optional.of(args[4]), Optional.of(args[2]), Optional.of("printFlightInformation"));
        } else if (args[0].equals("-host") && args[2].equals("-print") && args[3].equals("-port")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    args[14], Optional.of(args[1]), Optional.of(args[4]), Optional.of("printFlightInformation"));
        } else if (args[0].equals("-port") && args[2].equals("-print") && args[3].equals("-host")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    args[14], Optional.of(args[4]), Optional.of(args[1]), Optional.of("printFlightInformation"));
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void addFlightToTheSpecifiedAirlineWithoutPrintOption(String[] args) {
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else if (args[0].equals("-host") && args[2].equals("-port")) {
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    args[13], Optional.of(args[1]), Optional.of(args[3]), Optional.empty());
        } else if (args[0].equals("-port") && args[2].equals("-host")) {
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    args[13], Optional.of(args[3]), Optional.of(args[1]), Optional.empty());
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void handleSimilarInvalidScenarios(String[] args) {
        // Invalid scenarios
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if ((Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals)) ||
                (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals)
                        && Arrays.stream(args).anyMatch("-print"::equals))) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processInvalidScenariosWith13Param(String[] args) {
        handleSimilarInvalidScenarios(args);
    }

    private static void processInvalidScenariosWith12Param(String[] args) {
        // Invalid scenarios
        if (Arrays.stream(args).anyMatch("-host"::equals) && !Arrays.stream(args).anyMatch("-port"::equals)) {
            throw new AirlineException(PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-port"::equals) && !Arrays.stream(args).anyMatch("-host"::equals)) {
            throw new AirlineException(PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER + "\n" + USAGE);
        } else {
            handleSimilarInvalidScenarios(args);
        }
    }

    /**
     * This method processes the airline details with only '-print' option
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     *         Options: '-print'
     */
    private static void processAirlineDetailsWithOnlyPrintOption(String[] args) {
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else if (args[0].equals("-print")) {
            // Valid scenario: create the flight and print the flight information
            validateArguments(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9],
                    args[10], Optional.empty(), Optional.empty(), Optional.of("printFlightInformation"));
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processInvalidScenariosWith9Param(String[] args) {
        // Invalid scenarios
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void searchAndDisplayOnlyThoseFlightWithSpecifiedSrcAndDest(String[] args) {
        // Display all the flights of the airline specified with the '-search' parameter from given source airport to destination airport
        if (Arrays.stream(args).anyMatch("-host"::equals) && !Arrays.stream(args).anyMatch("-port"::equals)) {
            throw new AirlineException(PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-port"::equals) && !Arrays.stream(args).anyMatch("-host"::equals)) {
            throw new AirlineException(PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (args[0].equals("-host") && args[2].equals("-port") && args[4].equals("-search")) {
            validateAndSearchAirlineDetails(args[1], args[3], args[5], Optional.of(args[6]), Optional.of(args[7]));
        } else if (args[0].equals("-port") && args[2].equals("-host") && args[4].equals("-search")) {
            validateAndSearchAirlineDetails(args[3], args[1], args[5], Optional.of(args[6]), Optional.of(args[7]));
        } else if (args[0].equals("-search") && args[4].equals("-host") && args[6].equals("-port")) {
            validateAndSearchAirlineDetails(args[5], args[7], args[1], Optional.of(args[2]), Optional.of(args[3]));
        } else if (args[0].equals("-search") && args[4].equals("-port") && args[6].equals("-host")) {
            validateAndSearchAirlineDetails(args[7], args[5], args[1], Optional.of(args[2]), Optional.of(args[3]));
        } else if (args[0].equals("-host") && args[2].equals("-search") && args[6].equals("-port")) {
            validateAndSearchAirlineDetails(args[1], args[7], args[3], Optional.of(args[4]), Optional.of(args[5]));
        } else if (args[0].equals("-port") && args[2].equals("-search") && args[6].equals("-host")) {
            validateAndSearchAirlineDetails(args[7], args[1], args[3], Optional.of(args[4]), Optional.of(args[5]));
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processInvalidScenariosWith7Param(String[] args) {
        // Invalid scenarios
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (args[0].equals("-host") && args[2].equals("-port") && args[4].equals("-search")
                || args[0].equals("-port") && args[2].equals("-host") && args[4].equals("-search")) {
            // Validation of the provided source location airport code
            String srcLocation = args[6];
            if (Pattern.matches("[a-zA-Z]+", srcLocation) && srcLocation.length() == 3 && AirportNames.getName(srcLocation.toUpperCase()) != null) {
                throw new AirlineException(PLEASE_PROVIDE_DEST_AIRPORT_CODE_ALONG_WITH_SOUCRE_AIRPORT_CODE + "\n" + USAGE);
            } else {
                handleUnknownOption(args);
                throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
            }
        } else if (!Arrays.stream(args).anyMatch("-search"::equals)) {
            handleUnknownOption(args);
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void searchAndDisplayAllFlightsOfTheSpecifiedAirline(String[] args) {
        if (Arrays.stream(args).anyMatch("-host"::equals) && !Arrays.stream(args).anyMatch("-port"::equals)) {
            throw new AirlineException(PLEASE_PROVIDE_PORT_NUMBER_ALONG_WITH_HOSTNAME + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-port"::equals) && !Arrays.stream(args).anyMatch("-host"::equals)) {
            throw new AirlineException(PLEASE_PROVIDE_HOSTNAME_ALONG_WITH_PORT_NUMBER + "\n" + USAGE);
        } else if (args[0].equals("-host") && args[2].equals("-port") && args[4].equals("-search")) {
            validateAndSearchAirlineDetails(args[1], args[3], args[5], Optional.empty(), Optional.empty());
        } else if (args[0].equals("-port") && args[2].equals("-host") && args[4].equals("-search")) {
            validateAndSearchAirlineDetails(args[3], args[1], args[5], Optional.empty(), Optional.empty());
        } else if (args[0].equals("-search") && args[2].equals("-host") && args[4].equals("-port")) {
            validateAndSearchAirlineDetails(args[3], args[5], args[1], Optional.empty(), Optional.empty());
        } else if (args[0].equals("-search") && args[2].equals("-port") && args[4].equals("-host")) {
            validateAndSearchAirlineDetails(args[5], args[3], args[1], Optional.empty(), Optional.empty());
        } else if (args[0].equals("-host") && args[2].equals("-search") && args[4].equals("-port")) {
            validateAndSearchAirlineDetails(args[1], args[5], args[3], Optional.empty(), Optional.empty());
        } else if (args[0].equals("-port") && args[2].equals("-search") && args[4].equals("-host")) {
            validateAndSearchAirlineDetails(args[5], args[1], args[3], Optional.empty(), Optional.empty());
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    /**
     * This method processes the airline details without any options.
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     */
    private static void processAirlineDetailsWithoutOptions(String[] args) {
        // Invalid scenarios
        if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(PLEASE_DO_NOT_PROVIDE_PRINT_AND_SEARCH_PARAMETER_TOGETHER + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals) &&
                Arrays.stream(args).anyMatch("-search"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-print"::equals) ||
                (Arrays.stream(args).anyMatch("-host"::equals) && Arrays.stream(args).anyMatch("-port"::equals))) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
        }
        // Valid scenario: Only create the flight and exit
        validateArguments(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * This method validates the command line arguments and prints necessary information
     * to the terminal in case of validation failure.
     *
     * @param airlineName
     *         Name of the airline.
     * @param flightNumberString
     *         A String that holds the unique number identifying the flight.
     * @param srcLocation
     *         A three-letter code specifying the source location of a flight.
     * @param departureDateString
     *         A String that holds the date of departure of the flight from source location.
     * @param departureTimeString
     *         A String that holds the time of departure of the flight from source location.
     * @param departureTimeIndicationString
     *         A String that holds the time indication i.e. AM/PM of the flight's departure.
     * @param destLocation
     *         A three-letter code specifying the destination location of a flight.
     * @param arrivalDateString
     *         A String that holds the date of arrival of the flight at the destination location.
     * @param arrivalTimeString
     *         A String that holds the time of arrival of the flight at the destination location.
     * @param arrivalTimeIndicationString
     *         A String that holds the time indication i.e. AM/PM of the flight's arrival.
     * @param hostname
     *         {@link Optional} parameter that holds the host computer on which the server runs.
     * @param portNumberString
     *         {@link Optional} parameter that holds the port on which the server is listening.
     * @param printFlightInformation
     *         {@link Optional} parameter that holds the data depending on which Flight information is printed to the terminal.
     */
    private static void validateArguments(String airlineName, String flightNumberString, String srcLocation, String departureDateString,
                                          String departureTimeString, String departureTimeIndicationString, String destLocation, String arrivalDateString,
                                          String arrivalTimeString, String arrivalTimeIndicationString, Optional<String> hostname,
                                          Optional<String> portNumberString, Optional<String> printFlightInformation) throws AirlineException {

        // Validation of the provided flight number
        int flightNumber = 0;
        try {
            flightNumber = Integer.parseInt(flightNumberString);
        } catch (NumberFormatException e) {
            throw new AirlineException("Invalid flight number. Flight number provided should consist of only numbers between 0-9.");
        }

        // Validation of the provided source location
        if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
            throw new AirlineException("Invalid source location. Source location provided should consist of only three alphabets [a-zA-Z].");
        } else if (AirportNames.getName(srcLocation.toUpperCase()) == null) {
            throw new AirlineException("The three-letter source location code does not correspond to a known airport!");
        } else {
            srcLocation = srcLocation.toUpperCase();
        }

        // Validation of the provided destination location
        if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
            throw new AirlineException("Invalid destination location. Destination location provided should consist of only three alphabets [a-zA-Z].");
        } else if (AirportNames.getName(destLocation.toUpperCase()) == null) {
            throw new AirlineException("The three-letter destination location code does not correspond to a known airport!");
        } else {
            destLocation = destLocation.toUpperCase();
        }

        if (srcLocation.equals(destLocation)) {
            throw new AirlineException("The provided source and the destination location code is same. " +
                    "Please provide different airport codes for flight's source and destination location.");
        }

        // Validation of the departure date and time
        String departureTimeIndication = departureTimeIndicationString;
        Date departureDate = null;
        departureDate = AirlineDateTimeValidator.validateDateAndTime(departureDateString, departureTimeString, departureTimeIndication, "Departure");
        String departureDateTimeString = departureDateString + " " + departureTimeString + " " + departureTimeIndication;

        // Validation of the arrival date and time
        String arrivalTimeIndication = arrivalTimeIndicationString;
        Date arrivalDate = null;
        arrivalDate = AirlineDateTimeValidator.validateDateAndTime(arrivalDateString, arrivalTimeString, arrivalTimeIndication, "Arrival");
        String arrivalDateAndTimeString = arrivalDateString + " " + arrivalTimeString + " " + arrivalTimeIndication;

        if (arrivalDate.before(departureDate) || arrivalDate.equals(departureDate)) {
            throw new AirlineException("The provided arrival date and time should not be before or same as the departure date and time.");
        }

        Airline createdAirline = null;
        String responseMessage = null;

        if (hostname.isPresent() && portNumberString.isPresent()) {
            // Validation of the provided port number
            int portNumber = 0;
            try {
                portNumber = Integer.parseInt(portNumberString.get());
            } catch (NumberFormatException e) {
                throw new AirlineException("Invalid port number. Port number provided should be an integer.");
            }
            // Send parameters to REST client
            AirlineRestClient client = new AirlineRestClient(hostname.get(), portNumber);
            try {
                responseMessage = client.addFlightToTheSpecifiedAirline(airlineName, flightNumberString,
                        srcLocation, departureDateTimeString, destLocation, arrivalDateAndTimeString);
            } catch (IOException e) {
                throw new AirlineException("Error while contacting the server: " + e.getMessage());
            }
        } else {
            createdAirline = createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation,
                    arrivalDateAndTimeString, arrivalDate);
        }

        // If '-print' option is present
        if (printFlightInformation.isPresent() && printFlightInformation.get().equals("printFlightInformation")) {
            if (responseMessage != null) {
                System.out.println(responseMessage);
            } else if (createdAirline != null) {
                System.out.println(((ArrayList) createdAirline.getFlights()).get(0).toString());
            }
        }
    }

    /**
     * This creates the airline and flight using the information provided from the command line.
     *
     * @param airlineName
     *         Name of the airline.
     * @param flightNumber
     *         A  unique number identifying the flight.
     * @param srcLocation
     *         A three-letter code specifying the source location of a flight.
     * @param departureDateTimeString
     *         A String that holds the date and time of departure of the flight from source location.
     * @param departureDate
     *         A Date object that holds the date and time of departure of the flight from source location.
     * @param destLocation
     *         A three-letter code specifying the destination location of a flight.
     * @param arrivalDateAndTimeString
     *         A String that holds the date and time of arrival of the flight at the destination location.
     * @param arrivalDate
     *         A Date object that holds the date and time of arrival of the flight at destination location.
     *
     * @return An airline object holding airline and flight data.
     */
    private static Airline createAirlineAndFlight(String airlineName, int flightNumber, String srcLocation, String departureDateTimeString,
                                                  Date departureDate, String destLocation, String arrivalDateAndTimeString, Date arrivalDate) {
        // Creating airline and flight based on the provided input arguments
        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation, arrivalDateAndTimeString, arrivalDate);
        airline.addFlight(flight);
        return airline;
    }

    /**
     * Validates the hostname, port number and airline search parameters and searches for respective flight details.
     *
     * @param hostname
     *         Host computer on which the server runs.
     * @param portNumberString
     *         A String that holds the port on which the server is listening.
     * @param airlineName
     *         Name of the airline to be searched.
     * @param srcAirportCode
     *         {@link Optional} parameter that holds a three-letter code specifying the source airport of a flight.
     * @param destAirportCode
     *         {@link Optional} parameter that holds a three-letter code specifying the destination airport of a flight.
     */
    private static void validateAndSearchAirlineDetails(String hostname, String portNumberString, String airlineName,
                                                        Optional<String> srcAirportCode, Optional<String> destAirportCode) {
        // Validation of the provided port number
        int portNumber = 0;
        try {
            portNumber = Integer.parseInt(portNumberString);
        } catch (NumberFormatException e) {
            throw new AirlineException("Invalid port number. Port number provided should be an integer.");
        }

        String srcLocation = null;
        // Validation of the provided source location airport code
        if (srcAirportCode.isPresent()) {
            srcLocation = srcAirportCode.get();
            if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
                throw new AirlineException("Invalid source location. Source location provided should consist of only three alphabets [a-zA-Z].");
            } else if (AirportNames.getName(srcLocation.toUpperCase()) == null) {
                throw new AirlineException("The three-letter source location code does not correspond to a known airport!");
            } else {
                srcLocation = srcLocation.toUpperCase();
            }
        }

        String destLocation = null;
        // Validation of the provided destination location
        if (destAirportCode.isPresent()) {
            destLocation = destAirportCode.get();
            if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
                throw new AirlineException("Invalid destination location. Destination location provided should consist of only three alphabets [a-zA-Z].");
            } else if (AirportNames.getName(destLocation.toUpperCase()) == null) {
                throw new AirlineException("The three-letter destination location code does not correspond to a known airport!");
            } else {
                destLocation = destLocation.toUpperCase();
            }
        } else if (srcAirportCode.isPresent()) {
            throw new AirlineException("Destination airport code missing. " +
                    "Please provide destination airport code along with source airport code. ");
        }

        if (srcLocation != null && destLocation != null && srcLocation.equals(destLocation)) {
            throw new AirlineException("The provided source and the destination location code is same. " +
                    "Please provide different airport codes for flight's source and destination location.");
        }

        // Send parameters to REST client
        AirlineRestClient client = new AirlineRestClient(hostname, portNumber);
        try {
            if (srcAirportCode.isPresent() && destAirportCode.isPresent()) {
                Airline fetchedAirline = client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, srcLocation, destLocation);
                AirlineWebPrettyPrinter prettyPrinter = new AirlineWebPrettyPrinter(new PrintWriter(new StringWriter()));
                prettyPrinter.dumpToTerminal(fetchedAirline);
            } else {
                Airline fetchedAirline = client.getAllFlightsOfAnAirline(airlineName);
                AirlineWebPrettyPrinter prettyPrinter = new AirlineWebPrettyPrinter(new PrintWriter(new StringWriter()));
                prettyPrinter.dumpToTerminal(fetchedAirline);
            }
        } catch (IOException | ParserException e) {
            throw new AirlineException("Error while contacting the server: " + e.getMessage());
        } catch (HttpRequestHelper.RestException e) {
            if (e.getHttpStatusCode() == 404) {
                if (e.getMessage().contains(Messages.NO_DIRECT_FLIGHTS)) {
                    throw new AirlineException(Messages.NO_DIRECT_FLIGHTS);
                } else {
                    throw new AirlineException(Project5.SPECIFIED_AIRLINE_DOES_NOT_EXIST);
                }
            }
            throw new AirlineException(e.getMessage());
        }
    }

    /**
     * This method checks if any unknown option was provided.
     *
     * @param args
     *         The command line arguments.
     */
    private static void handleUnknownOption(String[] args) throws AirlineException {
        for (String arg : args) {
            if (arg.startsWith("-") && !arg.equals("-host") && !arg.equals("-port") && !arg.equals("-search") && !arg.equals("-print")) {
                throw new AirlineException(AN_UNKNOWN_OPTION_WAS_PROVIDED + "\n" + USAGE);
            }
        }
    }

    /**
     * Reads the content from the README.txt file and prints the content to the console.
     */
    private static void readContentFromREADME() throws AirlineException {
        try {
            InputStream readme = Project5.class.getResourceAsStream("README.txt");
            if (readme != null) {
                BufferedReader readmeReader = new BufferedReader(new InputStreamReader(readme));
                String readLine = readmeReader.readLine();
                while (readLine != null) {
                    System.out.println(readLine);
                    readLine = readmeReader.readLine();
                }
            }
            return;
        } catch (IOException e) {
            throw new AirlineException("Error occurred while reading from the README file!!");
        }
    }
}