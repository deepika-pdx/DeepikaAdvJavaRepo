package edu.pdx.cs410J.velapure;

import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

    /**
     * Usage information of this project.
     */
    private static final String USAGE = "usage: java -jar target/airline-2023.0.0.jar [options] <args>" + "\n" +
            "args are (in this order):" + "\n" +
            "airline" + "The name of the airline" + "\n" +
            "flightNumber" + "The flight number" + "\n" +
            "source" + "Three-letter code of the departure airport" + "\n" +
            "departure date" + "Departure date in mm/dd/yyyy format" + "\n" +
            "departure time" + "Departure time in 24-hour time format" + "\n" +
            "destination" + "Three-letter code of the arrival airport" + "\n" +
            "arrival date" + "Arrival date in mm/dd/yyyy format" + "\n" +
            "arrival time" + "Arrival time in 24-hour time format" + "\n" + "\n" +

            "options are (options may appear in any order):" + "\n" +
            "-print" + "Prints the description of the newly added flight" + "\n" +
            "-README" + "Prints a README for this project and exits";

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
     *         'departureTime', 'destinationLocation', 'arrivalDate', 'arrivalTime'
     *         Options: '-print', '-README'
     */
    public static void main(String[] args) {
        if (args != null) {
            int argLength = args.length;
            switch (argLength) {
                case 0:
                    System.err.println("Missing command line arguments. " + USAGE);
                    return;
                case 1:
                    String optionalParameter1 = args[0];
                    switch (optionalParameter1) {
                        case "-README":
                            System.out.println("Project Description: ");
                            readContentFromREADME();
                            return;
                        case "-print":
                            System.err.println("Please provide valid flight and airline information along with '-print'. " + USAGE);
                            return;
                        default:
                            if (optionalParameter1.contains("-")) {
                                System.err.println("An unknown option was provided. " + USAGE);
                                return;
                            } else {
                                System.err.println("Too few command line arguments. " + USAGE);
                                return;
                            }
                    }
                case 2:
                    String optionalParam1 = args[0];
                    String optionalParam2 = args[1];
                    if (optionalParam1.equals("-README") || optionalParam2.equals("-README")) {
                        System.out.println("Project Description: ");
                        readContentFromREADME();
                        return;
                    } else if (optionalParam1.equals("-print") || optionalParam2.equals("-print")) {
                        System.err.println("Please provide valid flight and airline information along with '-print'. " + USAGE);
                        return;
                    } else if (optionalParam1.contains("-") || optionalParam2.contains("-")) {
                        System.err.println("An unknown option was provided. " + USAGE);
                        return;
                    } else {
                        System.err.println("Too few command line arguments. " + USAGE);
                        return;
                    }
                default:
                    if (Arrays.stream(args).anyMatch("-README"::equals)) {
                        System.out.println("Project Description: ");
                        readContentFromREADME();
                        return;
                    }
                    String firstArgument = args[0];
                    if (firstArgument.equals("-print")) {
                        if (argLength < 9) {
                            System.err.println("Too few command line arguments. " +
                                    "Please provide valid flight and airline information along with '-print'. " + USAGE);
                            return;
                        } else if (argLength > 9) {
                            System.err.println("Too many command line arguments. " +
                                    "Please provide valid flight and airline information along with '-print'. " + USAGE);
                            return;
                        }
                        // Validate and add airline and flight information
                        String airlineName = args[1];
                        String flightNumberString = args[2];
                        String srcLocation = args[3];
                        String departureDateString = args[4];
                        String departureTimeString = args[5];
                        String destLocation = args[6];
                        String arrivalDateString = args[7];
                        String arrivalTimeString = args[8];
                        validateArguments(airlineName, flightNumberString, srcLocation,
                                departureDateString, departureTimeString, destLocation, arrivalDateString, arrivalTimeString, Optional.of("printFlightInformation"));


                    } else if (firstArgument.contains("-")) {
                        System.err.println("An unknown option was provided. " + USAGE);
                        return;
                    }
                    if (argLength > 8) {
                        if (argLength == 9 && Arrays.stream(args).anyMatch("-print"::equals)) {
                            System.err.println("Please provide '-print' option before the flight and airline information. " + USAGE);
                            return;
                        }
                        System.err.println("Too many command line arguments. " + USAGE);
                        return;
                    } else if (argLength < 8) {
                        System.err.println("Too few command line arguments. " + USAGE);
                        return;
                    }
                    // Validate and add airline and flight information
                    String airlineName = args[0];
                    String flightNumberString = args[1];
                    String srcLocation = args[2];
                    String departureDateString = args[3];
                    String departureTimeString = args[4];
                    String destLocation = args[5];
                    String arrivalDateString = args[6];
                    String arrivalTimeString = args[7];
                    validateArguments(airlineName, flightNumberString, srcLocation, departureDateString,
                            departureTimeString, destLocation, arrivalDateString, arrivalTimeString, Optional.empty());
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
     */
    private static void validateArguments(String airlineName, String flightNumberString, String srcLocation, String departureDateString,
                                          String departureTimeString, String destLocation, String arrivalDateString, String arrivalTimeString, Optional<String> printFlightInformation) {
        // Validation of the provided flight number
        int flightNumber = 0;
        try {
            flightNumber = Integer.parseInt(flightNumberString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid flight number. Flight number provided should consist of only numbers between 0-9.");
            return;
        }

        // Validation of the provided source location
        if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
            System.err.println("Invalid source location. Source location provided should consist of only three alphabets [a-zA-Z].");
            return;
        }

        // Validation of the provided departure date
        boolean isDepartureDateValid = validateDate(departureDateString);
        if (!isDepartureDateValid) {
            System.err.println("Invalid departure date. Departure date provided should be in mm/dd/yyyy format.");
            return;
        }

        // Validation of the provided departure time
        boolean isDepartureTimeValid = validateTime(departureTimeString);
        if (!isDepartureTimeValid) {
            System.err.println("Invalid departure time. Departure time provided should be in 24-hour(hh:mm) format.");
            return;
        }

        String departureDateTimeString = departureDateString + " " + departureTimeString;

        // Validation of the provided destination location
        if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
            System.err.println("Invalid destination location. Destination location provided should consist of only three alphabets [a-zA-Z].");
            return;
        }

        // Validation of the provided arrival date
        boolean isArrivalDateValid = validateDate(arrivalDateString);
        if (!isArrivalDateValid) {
            System.err.println("Invalid arrival date. Arrival date provided should be in mm/dd/yyyy format.");
            return;
        }

        // Validation of the provided arrival time
        boolean isArrivalTimeValid = validateTime(arrivalTimeString);
        if (!isArrivalTimeValid) {
            System.err.println("Invalid arrival time. Arrival time provided should be in 24-hour(hh:mm) format.");
            return;
        }

        String arrivalDateAndTimeString = arrivalDateString + " " + arrivalTimeString;
        createAirlineAndFlight(airlineName, flightNumber, srcLocation, departureDateTimeString, destLocation,
                arrivalDateAndTimeString, printFlightInformation);
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
     */
    private static void createAirlineAndFlight(String airlineName, int flightNumber, String srcLocation, String departureDateTimeString,
                                               String destLocation, String arrivalDateAndTimeString, Optional<String> printFlightInformation) {
        // Creating airline and flight based on the provided input arguments
        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(flightNumber, srcLocation, departureDateTimeString, destLocation, arrivalDateAndTimeString);
        airline.addFlight(flight);

        if (printFlightInformation.isPresent() && printFlightInformation.get().equals("printFlightInformation")) {
            System.out.println(flight.toString());
        }
    }


    /**
     * Reads the content from the README.txt file and prints the content to the console.
     */
    private static void readContentFromREADME() {
        try {
            InputStream readme = Project1.class.getResourceAsStream("README.txt");
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
            System.err.println("Error occurred while reading from the README file!!");
            return;
        }

    }

    /**
     * Validates the input departure and arrival date.
     *
     * @param providedDateString
     *         The date of departure/arrival of the flight.
     *
     * @return true if input date is valid and false if input date is invalid.
     */
    private static boolean validateDate(String providedDateString) {
        boolean isValid = true;
        try {
            SimpleDateFormat dateFormatterOne = new SimpleDateFormat("MM/dd/yyyy");
            Date formattedDateTypeOne = dateFormatterOne.parse(providedDateString);
            SimpleDateFormat dateFormatterTwo = new SimpleDateFormat("M/dd/yyyy");
            Date formattedDateTypeTwo = dateFormatterTwo.parse(providedDateString);
            SimpleDateFormat dateFormatterThree = new SimpleDateFormat("MM/d/yyyy");
            Date formattedDateTypeThree = dateFormatterThree.parse(providedDateString);
            SimpleDateFormat dateFormatterFour = new SimpleDateFormat("M/d/yyyy");
            Date formattedDateTypeFour = dateFormatterThree.parse(providedDateString);
            if (!(dateFormatterOne.format(formattedDateTypeOne).equals(providedDateString) || dateFormatterTwo.format(formattedDateTypeTwo).equals(providedDateString) || dateFormatterThree.format(formattedDateTypeThree).equals(providedDateString) || dateFormatterFour.format(formattedDateTypeFour).equals(providedDateString))) {
                isValid = false;
            }
        } catch (ParseException e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Validates the input departure and arrival time.
     *
     * @param providedTime
     *         The time of departure/arrival of the flight.
     *
     * @return true if input time is valid and false if input time is invalid.
     */
    private static boolean validateTime(String providedTime) {
        boolean isValid = true;
        String[] providedTimeArray = providedTime.split(":");
        if (providedTimeArray.length == 2) {
            try {
                int providedHour = Integer.parseInt(providedTimeArray[0]);
                int providedMinute = Integer.parseInt(providedTimeArray[1]);
                if (!(providedHour >= 0 && providedHour <= 23 && providedMinute >= 0 && providedMinute <= 59)) {
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }
}