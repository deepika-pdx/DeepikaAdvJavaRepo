package edu.pdx.cs410J.velapure;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J airline Project
 */
public class Project3 {

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

            "options are (options may appear in any order):" + "\n" +
            "-pretty file" + "   " + "Pretty print the airline’s flights to a text file or standard out. Provide this option as '-pretty file' or '-pretty -'" + "\n" +
            "-textFile file" + "   " + "File to read/write the airline info" + "\n" +
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
     * User-understandable error message for providing filename or standard output symbol and valid airline and flight information along with '-pretty' option.
     */
    private static final String PLEASE_PROVIDE_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY = "Please provide filename or standard output symbol(-) and valid flight and airline information along with '-pretty'. ";

    /**
     * User-understandable error message for providing text filename and valid airline and flight information along with '-print' and '-textFile' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_TEXT_FILE = "Please provide text filename and valid flight and airline information along with '-print' and '-textFile'. ";

    /**
     * User-understandable error message for providing text filename or standard output symbol(-) and valid airline and flight information along with '-pretty' and '-print' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_PRINT = "Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-print'. ";

    /**
     * User-understandable error message for providing text filename or standard output symbol(-) and valid airline and flight information along with '-pretty' and '-textFile' option.
     */
    private static final String PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE = "Please provide text filename or standard output symbol(-) and valid flight and airline information along with '-pretty' and '-textFile'. ";

    /**
     * User-understandable error message for an unknown option provided.
     */
    private static final String AN_UNKNOWN_OPTION_WAS_PROVIDED = "An unknown option was provided. ";

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
     *         Options: '-pretty file/-', '-textFile file', '-print', '-README'
     */
    public static void main(String[] args) {
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
                        // with (pretty and file/symbol) or (textfile and file)
                        processAirlineDetailsWithOnlyPrettyOrTextFileOption(args);
                        break;
                    case 13:
                        // with print and (pretty and file/symbol) or (textfile and file)
                        processAirlineDetailsWithPrintAndPrettyOrTextFileOption(args);
                        break;
                    case 14:
                        // with (pretty and file/symbol) and (textfile and file)
                        processAirlineDetailsWithPrettyAndTextFileOptions(args);
                        break;
                    case 15:
                        // with print and (pretty and file/symbol) and (textfile and file)
                        processAirlineDetailsWithPrintPrettyAndTextFileOptions(args);
                        break;
                    default:
                        handleUnknownOption(args);
                        if (argLength < 10) {
                            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                        }
                        if (argLength > 16) {
                            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
                        }
                }
            } catch (AirlineException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void processAirlineDetailsWithPrintPrettyAndTextFileOptions(String[] args) throws AirlineException {
        if (args[0].equals("-print") && args[1].equals("-textFile") && args[2].contains(".txt") && args[3].equals("-pretty") && (args[4].contains(".txt") || args[4].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[2]), Optional.of(args[4]));
        } else if (args[0].equals("-print") && args[1].equals("-pretty") && (args[2].contains(".txt") || args[2].equals("-")) && args[3].equals("-textFile") && args[4].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[4]), Optional.of(args[2]));
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-pretty") && (args[3].contains(".txt") || args[3].equals("-")) && args[4].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[1]), Optional.of(args[3]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-textFile") && args[3].contains(".txt") && args[4].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[3]), Optional.of(args[1]));
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-print") && args[3].equals("-pretty") && (args[4].contains(".txt") || args[4].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[1]), Optional.of(args[4]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-print") && args[3].equals("-textFile") && args[4].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14],
                    Optional.of("printFlightInformation"), Optional.of(args[4]), Optional.of(args[1]));
        } else if (Arrays.stream(args).anyMatch("-print"::equals) && Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide text filename and/or standard output symbol(-) and " +
                    "valid flight and airline information after the options '-textFile' and '-pretty'. " + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processAirlineDetailsWithPrettyAndTextFileOptions(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-print"::equals) && Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_PRINT + "\n" + USAGE);
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-pretty") && (args[3].contains(".txt") || args[3].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    Optional.empty(), Optional.of(args[1]), Optional.of(args[3]));
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-textFile") && args[3].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13],
                    Optional.empty(), Optional.of(args[3]), Optional.of(args[1]));
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide text filename and/or standard output symbol(-) and valid flight and airline information after the options '-textFile' and '-pretty'. " + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processAirlineDetailsWithPrintAndPrettyOrTextFileOption(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY_AND_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-print"::equals) && !Arrays.stream(args).anyMatch("-textFile"::equals) && !Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT + "\n" + USAGE);
        } else if (args[0].equals("-print") && args[1].equals("-textFile") && args[2].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.of(args[2]), Optional.empty());
        } else if (args[0].equals("-print") && args[1].equals("-pretty") && (args[2].contains(".txt") || args[2].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[2]));
        } else if (args[0].equals("-textFile") && args[1].contains(".txt") && args[2].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.of(args[1]), Optional.empty());
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-")) && args[2].equals("-print")) {
            // Validate and add airline and flight information
            validateArguments(args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12],
                    Optional.of("printFlightInformation"), Optional.empty(), Optional.of(args[1]));
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException("Please provide text filename and valid flight and airline information after the option '-textFile'. " + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals) && Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException("Please provide text filename or standard output symbol(-) and valid flight and airline information after the option '-pretty'. " + "\n" + USAGE);
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processAirlineDetailsWithOnlyPrettyOrTextFileOption(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-textFile"::equals) && Arrays.stream(args).anyMatch("-pretty"::equals)) {
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
        } else if (!args[0].equals("-textFile") && Arrays.stream(args).anyMatch("-textFile"::equals)) {
            throw new AirlineException("Please provide text filename and valid flight and airline information after the option '-textFile'. " + "\n" + USAGE);
        } else if (!args[0].equals("-pretty") && Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException("Please provide text filename or standard output symbol(-) and valid flight and airline information after the option '-pretty'. " + "\n" + USAGE);
        } else if (args[0].equals("-textFile") && args[1].contains(".txt")) {
            // Validate and add airline and flight information
            validateArguments(args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11],
                    Optional.empty(), Optional.of(args[1]), Optional.empty());
        } else if (args[0].equals("-pretty") && (args[1].contains(".txt") || args[1].equals("-"))) {
            // Validate and add airline and flight information
            validateArguments(args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11],
                    Optional.empty(), Optional.empty(), Optional.of(args[1]));
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processAirlineDetailsWithOnlyPrintOption(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-textFile"::equals)) {
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
                    args[10], Optional.of("printFlightInformation"), Optional.empty(), Optional.empty());
        } else {
            handleUnknownOption(args);
            throw new AirlineException(TOO_MANY_COMMAND_LINE_ARGUMENTS + "\n" + USAGE);
        }
    }

    private static void processAirlineDetailsWithoutOptions(String[] args) throws AirlineException {
        if (Arrays.stream(args).anyMatch("-print"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRINT + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-textFile"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_TEXT_FILENAME_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_TEXT_FILE + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-pretty"::equals)) {
            throw new AirlineException(TOO_FEW_COMMAND_LINE_ARGUMENTS +
                    PLEASE_PROVIDE_FILENAME_OR_STD_OP_SYMBOL_AND_VALID_FLIGHT_AND_AIRLINE_INFORMATION_ALONG_WITH_PRETTY + "\n" + USAGE);
        } else if (Arrays.stream(args).anyMatch("-"::startsWith)) {
            throw new AirlineException(AN_UNKNOWN_OPTION_WAS_PROVIDED + "\n" + USAGE);
        }
        // Validate and add airline and flight information
        validateArguments(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * This method checks if any unknown option was provided.
     *
     * @param args
     *         The command line arguments.
     */
    private static void handleUnknownOption(String[] args) throws AirlineException {
        for (String arg : args) {
            if (arg.startsWith("-") && !arg.equals("-print") && !arg.equals("-textFile") && !arg.equals("-pretty")) {
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
     * @param destLocation
     *         A three-letter code specifying the destination location of a flight.
     * @param arrivalDateString
     *         A String that holds the date of arrival of the flight at the destination location.
     * @param arrivalTimeString
     *         A String that holds the time of arrival of the flight at the destination location.
     * @param printFlightInformation
     *         {@link Optional} parameter that holds the data depending on which Flight information is printed to the terminal.
     * @param textFilename
     *         {@link Optional} parameter that holds the filename of the text file which does/does not contain airline and flight information.
     */
    private static void validateArguments(String airlineName, String flightNumberString, String srcLocation, String departureDateString,
                                          String departureTimeString, String departureTimeIndicationString, String destLocation, String arrivalDateString,
                                          String arrivalTimeString, String arrivalTimeIndicationString, Optional<String> printFlightInformation,
                                          Optional<String> textFilename, Optional<String> prettyFilename) throws AirlineException {

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
        }


        // Validation of the provided destination location
        if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
            throw new AirlineException("Invalid destination location. Destination location provided should consist of only three alphabets [a-zA-Z].");
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

        if (arrivalDate.before(departureDate)) {
            throw new AirlineException("The provided arrival date and time should not be before the departure date and time!");
        }

        boolean fileExists = false;
        Airline readAirline = null;
        if (textFilename.isPresent()) {
            String filename = textFilename.get();
            File textFile = new File(filename);
            fileExists = textFile.exists();
            try {
                if (!fileExists) {
                    textFile.createNewFile();
                } else {
                    TextParser parser = new TextParser(new FileReader(filename));
                    readAirline = parser.parse();
                    if (!readAirline.getName().equals(airlineName)) {
                        throw new AirlineException("The airline name provided in the input does not match with airline name in the input text file.");
                    }
                }
            } catch (IOException e) {
                throw new AirlineException("Unable to create a file with the specified name and path. Filename: " + filename);
            } catch (ParserException e) {
                throw new AirlineException(e.getMessage());
            }
        }

        if (textFilename.isPresent()) {
            if (!fileExists) {
                Airline newlyCreatedAirline = createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation,
                        arrivalDateAndTimeString, arrivalDate, printFlightInformation);
                try {
                    TextDumper textDumper = new TextDumper(new FileWriter(textFilename.get()));
                    textDumper.dump(newlyCreatedAirline);
                } catch (IOException e) {
                    throw new AirlineException("Unable to write to the file with the specified name and path. Filename: " + textFilename.get());
                }
            } else {
                Flight newFlight = new Flight(flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation, arrivalDateAndTimeString, arrivalDate);
                readAirline.addFlight(newFlight);
                try {
                    TextDumper textDumper = new TextDumper(new FileWriter(textFilename.get()));
                    textDumper.dump(readAirline);
                } catch (IOException e) {
                    throw new AirlineException("Unable to write to the file with the specified name and path. Filename: " + textFilename.get());
                }
                if (printFlightInformation.isPresent() && printFlightInformation.get().equals("printFlightInformation")) {
                    System.out.println(newFlight.toString());
                }
            }
        } else {
            createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation,
                    arrivalDateAndTimeString, arrivalDate, printFlightInformation);
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
     * @param destLocation
     *         A three-letter code specifying the destination location of a flight.
     * @param arrivalDateAndTimeString
     *         A String that holds the date and time of arrival of the flight at the destination location.
     * @param printFlightInformation
     *         {@link Optional} parameter that holds the data depending on which Flight information is printed to the terminal.
     *
     * @return An airline object holding airline and flight data.
     */
    private static Airline createAirlineAndFlight(String airlineName, int flightNumber, String srcLocation, String departureDateTimeString,
                                                  Date departureDate, String destLocation, String arrivalDateAndTimeString, Date arrivalDate,
                                                  Optional<String> printFlightInformation) {
        // Creating airline and flight based on the provided input arguments
        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(flightNumber, srcLocation, departureDateTimeString, departureDate, destLocation, arrivalDateAndTimeString, arrivalDate);
        airline.addFlight(flight);

        if (printFlightInformation.isPresent() && printFlightInformation.get().equals("printFlightInformation")) {
            System.out.println(flight.toString());
        }
        return airline;
    }


    /**
     * Reads the content from the README.txt file and prints the content to the console.
     */
    private static void readContentFromREADME() throws AirlineException {
        try {
            InputStream readme = Project3.class.getResourceAsStream("README.txt");
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