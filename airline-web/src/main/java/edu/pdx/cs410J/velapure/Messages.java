package edu.pdx.cs410J.velapure;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages {

    public static final String SRC_AIRPORT_CODE_IS_NOT_VALID = "Invalid source airport code. Source airport code provided \"%s\" should consist of only three alphabets [a-zA-Z].";

    public static final String SRC_AIRPORT_CODE_IS_UNKNOWN = "The three-letter source airport code provided \"%s\" does not correspond to a known airport!";

    public static final String DEST_AIRPORT_CODE_IS_NOT_VALID = "Invalid destination airport code. Destination airport code provided \"%s\" should consist of only three alphabets [a-zA-Z].";

    public static final String DEST_AIRPORT_CODE_IS_UNKNOWN = "The three-letter destination airport code provided \"%s\" does not correspond to a known airport!";

    public static final String DEST_AIRPORT_CODE_MISSING = "Destination airport code missing. Please provide destination airport code along with source airport code. ";

    public static final String SRC_AND_DEST_AIRPORT_CODE_ARE_SAME = "Source and Destination airport code are same. Please provide different airport codes for flight's source and destination location. ";

    public static final String FLIGHT_NUMBER_IS_INVALID = "The provided flight number \"%s\" is invalid. Flight number provided should consist of only numbers between 0-9. ";

    public static final String DEPARTURE_DATE_IS_INVALID = "The provided departure date \"%s\" is invalid. ";

    public static final String ARRIVAL_DATE_IS_INVALID = "The provided arrival date \"%s\" is invalid. ";

    public static final String ARRIVAL_DATE_SHOULD_NOT_BE_SAME_OR_BEFORE_DEPARTURE_TIME = "The provided arrival date and time should not be before or same as the departure date and time.";


    public static String missingRequiredParameter(String parameterName) {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String invalidInputParameter(String parameterName, String errorMessage) {
        if (errorMessage.contains("\"%s\"")) {
            return String.format(errorMessage, parameterName);
        } else {
            return errorMessage;
        }
    }

    public static String addedFlightToTheAirline(String airline, String flightNumber, String srcAirport, String departure, String destAirport, String arrival) {
        return String.format("Added flight with flight number as %s, sourceAirportCode as %s, departure date and time as %s, " +
                "destinationAirportCode as %s and arrival date and time as %s for the airline %s.", flightNumber, srcAirport, departure, destAirport, arrival, airline);
    }

    public static String allAirlineEntriesDeleted() {
        return "All airline entries have been deleted";
    }

}
