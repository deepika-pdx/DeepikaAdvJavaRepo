package edu.pdx.cs410J.velapure;

import com.google.common.annotations.VisibleForTesting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {

    @VisibleForTesting
    static boolean isValidDateAndTime(String dateAndTime) {
        return true;
    }

    public static void main(String[] args) {
        if (args != null) {
            int argLength = args.length;
            if (argLength == 0) {
                System.err.println("Missing command line arguments. Please enter arguments in the order: " +
                        "<airline_name>, <flight_number>, <source_location>, <departure_date>, <departure_time>, " +
                        "<destination_location, <arrival_date>, <arrival_time>, '-print' (optional), '-README' (optional).");
                return;
            } else if (argLength < 8) {
                System.err.println("Too few command line arguments. Please enter all the non-optional arguments in the order: " +
                        "<airline_name>, <flight_number>, <source_location>, <departure_date>, <departure_time>, " +
                        "<destination_location, <arrival_date>, <arrival_time>, '-print' (optional), '-README' (optional).");
                System.err.println("Arguments provided: ");
                for (String arg : args) {
                    System.out.println(arg);
                }
                return;
            } else if (argLength > 10) {
                System.err.println("Too many command line arguments. Please enter correct number of arguments " +
                        "(optional and/or non-optional) in the order: " +
                        "<airline_name>, <flight_number>, <source_location>, <departure_date>, <departure_time>, " +
                        "<destination_location, <arrival_date>, <arrival_time>, '-print' (optional), '-README' (optional).");
                System.err.println("Arguments provided: ");
                for (String arg : args) {
                    System.out.println(arg);
                }
                return;
            } else {
                String airlineName = args[0];

                String flightNumberString = args[1];
                int flightNumber = 0;
                try {
                    flightNumber = Integer.parseInt(flightNumberString);
                } catch (NumberFormatException e) {
                    System.err.println("Flight number provided should consist of only numbers between 0-9.");
                    return;
                }

                String srcLocation = args[2];
                if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
                    System.err.println("Source location provided should consist of only three alphabets [a-zA-Z].");
                    return;
                }

                String departureDateString = args[3];
                boolean isDepartureDateValid = validateDate(departureDateString);
                if (!isDepartureDateValid) {
                    System.err.println("Departure date provided should be in mm/dd/yyyy format.");
                    return;
                }

                String departureTimeString = args[4];
                boolean isDepartureTimeValid = validateTime(departureTimeString);
                if (!isDepartureTimeValid) {
                    System.err.println("Departure time provided should be in 24-hour(hh:mm) format.");
                    return;
                }
                String departureDateTimeString = departureDateString + " " + departureTimeString;

                String destLocation = args[5];
                if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
                    System.err.println("Destination location provided should consist of only three alphabets [a-zA-Z].");
                    return;
                }

                String arrivalDateString = args[6];
                boolean isArrivalDateValid = validateDate(arrivalDateString);
                if (!isArrivalDateValid) {
                    System.err.println("Arrival date provided should be in mm/dd/yyyy format.");
                    return;
                }

                String arrivalTimeString = args[7];
                boolean isArrivalTimeValid = validateTime(arrivalTimeString);
                if (!isArrivalTimeValid) {
                    System.err.println("Arrival time provided should be in 24-hour(hh:mm) format.");
                    return;
                }
                String arrivalDateAndTimeString = arrivalDateString + " " + arrivalTimeString;

                Airline airline = new Airline(airlineName);
                Flight flight = new Flight(flightNumber, srcLocation, departureDateTimeString, destLocation, arrivalDateAndTimeString);
                airline.addFlight(flight);


                String optionalParameter1 = args.length == 9 ? args[8] : null;
                if (optionalParameter1.equals("-print")) {
                    System.out.println(flight.toString());
                    return;
                } else {
                    System.err.println("Please enter the optional parameters as '-print' or '-README'!");
                    return;
                }

            }
        }
    }

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