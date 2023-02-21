package edu.pdx.cs410J.velapure;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J airline Project
 */
public class Project4 {

    /**
     * Usage information of this project.
     */
    private static final String USAGE = "usage: java -jar target/airline-2023.0.0.jar [options] <args>" + "\n" +
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

            "options are (Options may appear in any order. Please do not provide '-xmlFile' and '-textFile' option together.) :" + "\n" +
            "-xmlFile file" + "   " + "Xml file to read/write the airline info" + "\n" +
            "-textFile file" + "   " + "Text file to read/write the airline info" + "\n" +
            "-pretty file" + "   " + "Pretty print the airline’s flights to a text file or standard out. Provide this option as '-pretty file' or '-pretty -'" + "\n" +
            "-print" + "   " + "Prints the description of the newly added flight" + "\n" +
            "-README" + "   " + "Prints a README for this project and exits";
    /**
     * User-understandable error message for providing valid airline and flight information along with '-print' option.
     */
    private static final String PLEASE_PROVIDE_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT = "Please provide valid flight and airline information along with '-print'. ";

    /**
     * User-understandable error message for providing text filename and valid airline and flight information along with '-textFile' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_TEXT_FILE = "Please provide text filename and valid flight and airline information along with '-textFile'. ";

    /**
     * User-understandable error message for providing xml filename and valid airline and flight information along with '-xmlFile' option.
     */
    private static final String PLEASE_PROVIDE_XML_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_XML_FILE = "Please provide xml filename and valid flight and airline information along with '-xmlFile'. ";

    /**
     * User-understandable error message for providing filename or standard output symbol and valid airline and flight information along with '-pretty' option.
     */
    private static final String PLEASE_PROVIDE_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY = "Please provide filename or standard output symbol(-) and valid flight and airline information along with '-pretty'. ";

    /**
     * User-understandable error message for providing text filename and valid airline and flight information along with '-print' and '-textFile' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_TEXT_FILE = "Please provide text filename and valid flight and airline information along with '-print' and '-textFile'. ";

    /**
     * User-understandable error message for providing xml filename and valid airline and flight information along with '-print' and '-xmlFile' option.
     */
    private static final String PLEASE_PROVIDE_XML_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_XML_FILE = "Please provide xml filename and valid flight and airline information along with '-print' and '-xmlFile'. ";

    /**
     * User-understandable error message for providing '-textFile' and '-xmlFile' option together.
     */
    private static final String PLEASE_PROVIDE_EITHER_XML_FILE_OR_TEXT_FILE_OPTION_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION = "Please provide either '-xmlFile' or '-textFile' option along with filename and valid flight and airline information. ";

    /**
     * User-understandable error message for providing text filename or standard output symbol(-) and valid airline and flight information along with '-pretty' and '-print' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_PRINT = "Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-print'. ";

    /**
     * User-understandable error message for providing text filename or standard output symbol(-) and valid airline and flight information along with '-pretty' and '-textFile' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE = "Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'. ";

    /**
     * User-understandable error message for providing xml filename or standard output symbol(-) and valid airline and flight information along with '-pretty' and '-xmlFile' option.
     */
    private static final String PLEASE_PROVIDE_XML_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_XML_FILE = "Please provide xml filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-xmlFile'. ";

    /**
     * User-understandable error message for an unknown option provided.
     */
    private static final String AN_UNKNOWN_OPTION_WAS_PROVIDED = "An unknown option was provided. ";

    /**
     * User-understandable error message when invalid optional parameter combination.
     */
    private static final String INVALID_OPTIONAL_PARAMETER_COMBINATION = "Invalid optional parameter combination. ";

    /**
     * User-understandable error message when too few command line arguments are passed.
     */
    private static final String TOO_FEW_COMMAND_LINE_ARGUMENTS = "Too few command line arguments. ";

    /**
     * User-understandable error message when too many command line arguments are passed.
     */
    private static final String TOO_MANY_COMMAND_LINE_ARGUMENTS = "Too many command line arguments. ";

    /**
     * User-understandable error message when '-print' or '-textFile' is provided more than once.
     */
    private static final String PLEASE_PROVIDE_OPTION_PRINT_OR_TEXT_FILE_ONLY_ONCE = "Please provide option '-print' or '-textFile' only once. ";

    @VisibleForTesting
    static boolean isValidDateAndTime(String dateAndTime) {
        return true;
    }

    /**
     * This is the main method of the Airline project and below is the description
     * of the parameters expected to be passed as arguments.
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     *         Options: '-xmlFile file', '-textFile file', '-pretty file/-', '-print', '-README'
     */
    public static void main(String[] args) {
        if (args != null) {
            try {
                if (Arrays.stream(args).anyMatch("-README"::equals)) {
                    System.out.println("Project Description: ");
                    readContentFromREADME();
                    return;
                }
                if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-textFile"::equals)) {
                    throw new AirlineException(INVALID_OPTIONAL_PARAMETER_COMBINATION +
                            PLEASE_PROVIDE_EITHER_XML_FILE_OR_TEXT_FILE_OPTION_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION + "\n" + USAGE);
                }
                int argLength = args.length;
                switch (argLength) {
                    case 0:
                        throw new AirlineException("Missing command line arguments. " + "\n" + USAGE);
                    case 10:
                        // with no options
                        processAirlineDetailsWithoutOptions(args);
                        break;
                    case 11:
                        // with print option
                        processAirlineDetailsWithOnlyPrintOption(args);
                        break;
                    case 12:
                        // with (pretty and file/symbol) or (textfile and file) or (xmlfile and file)
                        processAirlineDetailsWithOnlyPrettyOrTextOrXmlFileOption(args);
                        break;
                    case 13:
                        // with print and {(pretty and file/symbol) or (textfile and file) or (xmlfile and file)}
                        processAirlineDetailsWithPrintAndPrettyOrTextOrXmlFileOption(args);
                        break;
                    case 14:
                        // with (pretty and file/symbol) and {(textfile and file) or (xmlfile and file)}
                        processAirlineDetailsWithPrettyAndTextOrXmlFileOptions(args);
                        break;
                    case 15:
                        // with print and (pretty and file/symbol) and {(textfile and file) or (xmlfile and file)}
                        processAirlineDetailsWithPrintPrettyAndTextOrXmlFileOptions(args);
                        break;
                    default:
                        handleUnknownOption(args);
                        if (argLength < 10) {
                            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                        }
                        if (argLength > 15) {
                            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                        }
                }
            } catch (AirlineException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * This method processes the airline details with '-print', '-pretty' and ('-textFile' or '-xmlFile') options
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     *         Options: '-pretty file/-', '-textFile file', '-xmlFile file', '-print'
     */
    private static void processAirlineDetailsWithPrintPrettyAndTextOrXmlFileOptions(String[] args) throws AirlineException {
        //process below six combinations of '-print', '-xmlFile' and '-pretty'
        if (args[0].equals("-print") && args[1].equals("-xmlFile") && args[3].equals("-pretty") && (args[4].contains(".txt") || args[4].equals("-"))) {
            if (!args[2].contains(".xml") && args[2].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[2]), Optional.empty(), Optional.of(args[4]));
        } else if (args[0].equals("-print") && args[1].equals("-pretty") && (args[2].contains(".txt") || args[2].equals("-")) && args[3].equals("-xmlFile")) {
            if (!args[4].contains(".xml") && args[4].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[4]), Optional.empty(), Optional.of(args[2]));
        } else if (args[0].equals("-xmlFile") && args[2].equals("-pretty") && (args[3].contains(".txt") || args[3].equals("-")) && args[4].equals("-print")) {
            if (!args[1].contains(".xml") && args[1].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[1]), Optional.empty(), Optional.of(args[3]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-xmlFile") && args[4].equals("-print")) {
            if (!args[3].contains(".xml") && args[3].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[3]), Optional.empty(), Optional.of(args[1]));
        } else if (args[0].equals("-xmlFile") && args[2].equals("-print") && args[3].equals("-pretty") && (args[4].contains(".txt") || args[4].equals("-"))) {
            if (!args[1].contains(".xml") && args[1].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[1]), Optional.empty(), Optional.of(args[4]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-print") && args[3].equals("-xmlFile")) {
            if (!args[4].contains(".xml") && args[4].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[4]), Optional.empty(), Optional.of(args[1]));
        }
        //process below six combinations of '-print', '-textFile' and '-pretty'
        else if (args[0].equals("-print") && args[1].equals("-textFile") && args[2].contains(".txt") && args[3].equals("-pretty") && (args[4].contains(".txt") || args[4].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[2]), Optional.of(args[4]));
        } else if (args[0].equals("-print") && args[1].equals("-pretty") && (args[2].contains(".txt") || args[2].equals("-")) && args[3].equals("-textFile") && args[4].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[4]), Optional.of(args[2]));
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-pretty") && (args[3].contains(".txt") || args[3].equals("-")) && args[4].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[1]), Optional.of(args[3]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-textFile") && args[3].contains(".txt") && args[4].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[3]), Optional.of(args[1]));
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-print") && args[3].equals("-pretty") && (args[4].contains(".txt") || args[4].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[1]), Optional.of(args[4]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-print") && args[3].equals("-textFile") && args[4].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[4]), Optional.of(args[1]));
        } else if (Arrays.stream(args).anyMatch("-print"::equals) && Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide xml filename and pretty text filename and/or standard output symbol(-) and " +
                    "valid flight and airline information after the options '-xmlFile' and '-pretty'. " + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-print"::equals) && Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide text filename and/or standard output symbol(-) and " +
                    "valid flight and airline information after the options '-textFile' and '-pretty'. " + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    /**
     * This method processes the airline details with '-pretty' and ('-textFile' or '-xmlFile') options
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     *         Options: '-pretty file/-', '-textFile file', '-xmlFile file'
     */
    private static void processAirlineDetailsWithPrettyAndTextOrXmlFileOptions(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-print"::equals) && Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-print"::equals) && Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_PRINT + "\n" + USAGE);
        } else if (args[0].equals("-xmlFile") && args[2].equals("-pretty") && (args[3].contains(".txt") || args[3].equals("-"))) {
            if (!args[1].contains(".xml") && args[1].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    Optional.empty(), Optional.of(args[1]), Optional.empty(), Optional.of(args[3]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-xmlFile")) {
            if (!args[3].contains(".xml") && args[3].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    Optional.empty(), Optional.of(args[3]), Optional.empty(), Optional.of(args[1]));
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-pretty") && (args[3].contains(".txt") || args[3].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    Optional.empty(), Optional.empty(), Optional.of(args[1]), Optional.of(args[3]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-textFile") && args[3].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    Optional.empty(), Optional.empty(), Optional.of(args[3]), Optional.of(args[1]));
        } else if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide xml filename and/or standard output symbol(-) and valid flight and airline information after the options '-xmlFile' and '-pretty'. " + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide text filename and/or standard output symbol(-) and valid flight and airline information after the options '-textFile' and '-pretty'. " + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    /**
     * This method processes the airline details with '-print' and ('-pretty' or '-textFile' or '-xmlFile') options
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     *         Options: '-pretty file/-', '-textFile file', '-xmlFile file', '-print'
     */
    private static void processAirlineDetailsWithPrintAndPrettyOrTextOrXmlFileOption(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-print"::equals) && !Arrays.stream(args).anyMatch("-xmlFile"::equals)
                && !Arrays.stream(args).anyMatch("-textFile"::equals) && !Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT + "\n" + USAGE);
        } else if (args[0].equals("-print") && args[1].equals("-xmlFile")) {
            if (!args[2].contains(".xml") && args[2].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.of(args[2]), Optional.empty(), Optional.empty());
        } else if (args[0].equals("-print") && args[1].equals("-textFile") && args[2].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[2]), Optional.empty());
        } else if (args[0].equals("-print") && args[1].equals("-pretty") && (args[2].contains(".txt") || args[2].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.empty(), Optional.of(args[2]));
        } else if (args[0].equals("-xmlFile") && args[2].equals("-print")) {
            if (!args[1].contains(".xml") && args[1].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.of(args[1]), Optional.empty(), Optional.empty());
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[1]), Optional.empty());
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.empty(), Optional.of(args[1]));
        } else if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException("Please provide xml filename and valid flight and airline information after the option '-xmlFile'. " + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException("Please provide text filename and valid flight and airline information after the option '-textFile'. " + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException("Please provide text filename or standard output symbol(-) and valid flight and airline information after the option '-pretty'. " + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    /**
     * This method processes the airline details with  '-pretty' or '-textFile' or '-xmlFile' option
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     *         Options: '-pretty file/-', '-textFile file', '-xmlFile file'
     */
    private static void processAirlineDetailsWithOnlyPrettyOrTextOrXmlFileOption(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-xmlFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_PRINT + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT + "\n" + USAGE);
        } else if (!args[0].equals("-xmlFile") && Arrays.stream(args).anyMatch("-xmlFile"::equals)) {
            throw new AirlineException("Please provide xml filename and valid flight and airline information after the option '-xmlFile'. " + "\n" + USAGE);
        } else if (!args[0].equals("-textFile") && Arrays.stream(args).anyMatch("-textFile"::equals)) {
            throw new AirlineException("Please provide text filename and valid flight and airline information after the option '-textFile'. " + "\n" + USAGE);
        } else if (!args[0].equals("-pretty") && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide text filename or standard output symbol(-) and valid flight and airline information after the option '-pretty'. " + "\n" + USAGE);
        } else if (args[0].equals("-xmlFile")) {
            if (!args[1].contains(".xml") && args[1].contains(".")) {
                throw new AirlineException("Please provide filename with only '.xml' extension after the '-xmlFile' option. " + "\n" + USAGE);
            }
            // Validate and add airline and flight information
            validateArguments(args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11],
                    Optional.empty(), Optional.of(args[1]), Optional.empty(), Optional.empty());
        } else if (args[0].equals("-textFile") && args[1].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11],
                    Optional.empty(), Optional.empty(), Optional.of(args[1]), Optional.empty());
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11],
                    Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(args[1]));
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
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
    private static void processAirlineDetailsWithOnlyPrintOption(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-xmlFile"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY + "\n" + USAGE);
        } else if (!args[0].equals("-print") && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException("Please provide valid flight and airline information after the '-print' option. " + "\n" + USAGE);
        } else if (args[0].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9],
                    args[10], Optional.of("printFlightInformation"), Optional.empty(), Optional.empty(), Optional.empty());
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    /**
     * This method processes the airline details without any options.
     *
     * @param args
     *         Arguments:'airlineName', 'flightNumber', 'sourceLocation', 'departureDate',
     *         'departureTime', 'departureTimeIndication', 'destinationLocation', 'arrivalDate', 'arrivalTime', 'arrivalTimeIndication'
     */
    private static void processAirlineDetailsWithoutOptions(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-xmlFile"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_XML_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_XML_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-"::startsWith)) {
            throw new AirlineException(AN_UNKNOWN_OPTION_WAS_PROVIDED + "\n" + USAGE);
        }
        // Validate and add airline and flight information
        validateArguments(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * This method checks if any unknown option was provided.
     *
     * @param args
     *         The command line arguments.
     */
    private static void handleUnknownOption(String[] args) throws AirlineException {
        for (String arg : args) {
            if (arg.startsWith("-") && !arg.equals("-print") && !arg.equals("-textFile") && !arg.equals("-pretty") && !arg.equals("-xmlFile")) {
                throw new AirlineException(AN_UNKNOWN_OPTION_WAS_PROVIDED + "\n" + USAGE);
            }
        }
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
     * @param printFlightInformation
     *         {@link Optional} parameter that holds the data depending on which Flight information is printed to the terminal.
     * @param xmlFilename
     *         {@link Optional} parameter that holds the filename of the xml file which does/does not contain airline and flight information.
     * @param textFilename
     *         {@link Optional} parameter that holds the filename of the text file which does/does not contain airline and flight information.
     * @param prettyFilename
     *         {@link Optional} parameter that holds the filename/standard output symbol to which airline and flight information is pretty printed.
     */
    private static void validateArguments(String airlineName, String flightNumberString, String srcLocation, String departureDateString,
                                          String departureTimeString, String departureTimeIndicationString, String destLocation, String arrivalDateString,
                                          String arrivalTimeString, String arrivalTimeIndicationString, Optional<String> printFlightInformation,
                                          Optional<String> xmlFilename, Optional<String> textFilename, Optional<String> prettyFilename) throws AirlineException {

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

        Airline readAirline = null;
        Airline createdAirline = null;
        Flight newFlight = null;

        // If '-xmlFile' option is present
        if (xmlFilename.isPresent()) {
            String filename = xmlFilename.get();
            File xmlFile = new File(filename);
            boolean fileExists = xmlFile.exists();
            try {
                if (!fileExists) {
                    xmlFile.createNewFile();
                    createdAirline = createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation,
                            arrivalDateAndTimeString, arrivalDate);
                    try {
                        AirlineXmlDumper airlineXmlDumper = new AirlineXmlDumper(filename);
                        airlineXmlDumper.dump(createdAirline);
                    } catch (IOException e) {
                        throw new AirlineException("Unable to write to the file with the specified name and path. Filename: " + filename);
                    }
                } else {
                    AirlineXmlParser airlineXmlParser = new AirlineXmlParser(new FileReader(filename));
                    readAirline = airlineXmlParser.parse();
                    if (!readAirline.getName().equals(airlineName)) {
                        throw new AirlineException("The airline name provided in the input does not match with airline name in the input xml file.");
                    }

                    newFlight = new Flight(flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation, arrivalDateAndTimeString, arrivalDate);
                    readAirline.addFlight(newFlight);

                    try {
                        AirlineXmlDumper airlineXmlDumper = new AirlineXmlDumper(filename);
                        airlineXmlDumper.dump(readAirline);
                    } catch (IOException e) {
                        throw new AirlineException("Unable to write to the file with the specified name and path. Filename: " + filename);
                    }
                }
            } catch (IOException e) {
                throw new AirlineException("Unable to create a file with the specified name and path. Filename: " + filename);
            } catch (ParserException e) {
                throw new AirlineException(e.getMessage());
            }
        } else
            // If '-textFile' option is present
            if (textFilename.isPresent()) {
                String filename = textFilename.get();
                File textFile = new File(filename);
                boolean fileExists = textFile.exists();
                try {
                    if (!fileExists) {
                        textFile.createNewFile();
                        createdAirline = createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation,
                                arrivalDateAndTimeString, arrivalDate);
                        try {
                            AirlineTextDumper airlineTextDumper = new AirlineTextDumper(new FileWriter(textFilename.get()));
                            airlineTextDumper.dump(createdAirline);
                        } catch (IOException e) {
                            throw new AirlineException("Unable to write to the file with the specified name and path. Filename: " + textFilename.get());
                        }
                    } else {
                        AirlineTextParser parser = new AirlineTextParser(new FileReader(filename));
                        readAirline = parser.parse();
                        if (!readAirline.getName().equals(airlineName)) {
                            throw new AirlineException("The airline name provided in the input does not match with airline name in the input text file.");
                        }

                        newFlight = new Flight(flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation, arrivalDateAndTimeString, arrivalDate);
                        readAirline.addFlight(newFlight);

                        try {
                            AirlineTextDumper airlineTextDumper = new AirlineTextDumper(new FileWriter(textFilename.get()));
                            airlineTextDumper.dump(readAirline);
                        } catch (IOException e) {
                            throw new AirlineException("Unable to write to the file with the specified name and path. Filename: " + textFilename.get());
                        }
                    }
                } catch (IOException e) {
                    throw new AirlineException("Unable to create a file with the specified name and path. Filename: " + filename);
                } catch (ParserException e) {
                    throw new AirlineException(e.getMessage());
                }
            } else {
                createdAirline = createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation,
                        arrivalDateAndTimeString, arrivalDate);
            }

        // If '-pretty' option is present
        if (prettyFilename.isPresent()) {
            Airline prettyPrintAirline = null;
            if (readAirline != null) {
                prettyPrintAirline = readAirline;
            } else {
                prettyPrintAirline = createdAirline;
            }

            String prettyPrintValue = prettyFilename.get();
            if (!prettyPrintValue.equals("-")) {
                // Pretty print to file
                String prettyPrintFilename = prettyPrintValue;
                File prettyFile = new File(prettyPrintFilename);
                try {
                    prettyFile.createNewFile();
                    try {
                        AirlinePrettyPrinter prettyPrinter = new AirlinePrettyPrinter(new PrintWriter(new FileWriter(prettyPrintFilename)));
                        prettyPrinter.dump(prettyPrintAirline);
                    } catch (IOException e) {
                        throw new AirlineException("Unable to write to the pretty print file with the specified name and path. Filename: " + prettyPrintFilename);
                    }
                } catch (IOException e) {
                    throw new AirlineException("Unable to create a pretty file with the specified name and path. Filename: " + prettyPrintFilename);
                }
            } else {
                // Pretty print to terminal
                try {
                    AirlinePrettyPrinter prettyPrinter = new AirlinePrettyPrinter(new PrintWriter(new FileWriter(prettyPrintValue)));
                    prettyPrinter.dumpToTerminal(prettyPrintAirline);
                } catch (IOException e) {
                    throw new AirlineException("Unable to pretty print the airline and flight details to the terminal!");
                }
            }
        }

        // If '-print' option is present
        if (printFlightInformation.isPresent() && printFlightInformation.get().equals("printFlightInformation")) {
            if (newFlight != null) {
                System.out.println(newFlight.toString());
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
     * Reads the content from the README.txt file and prints the content to the console.
     */
    private static void readContentFromREADME() throws AirlineException {
        try {
            InputStream readme = Project4.class.getResourceAsStream("README.txt");
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