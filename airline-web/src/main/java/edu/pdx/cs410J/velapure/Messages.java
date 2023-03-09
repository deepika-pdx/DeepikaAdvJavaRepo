package edu.pdx.cs410J.velapure;

/**
 * This is <code>Messages</code> class for formatting messages on the server side. This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages {

    /**
     * User-understandable error message for providing invalid source airport code.
     */
    public static final String SRC_AIRPORT_CODE_IS_NOT_VALID = "Invalid source airport code. Source airport code provided \"%s\" should consist of only three alphabets [a-zA-Z].";

    /**
     * User-understandable error message for providing unknown source airport code.
     */
    public static final String SRC_AIRPORT_CODE_IS_UNKNOWN = "The three-letter source airport code provided \"%s\" does not correspond to a known airport!";

    /**
     * User-understandable error message for providing invalid destination airport code.
     */
    public static final String DEST_AIRPORT_CODE_IS_NOT_VALID = "Invalid destination airport code. Destination airport code provided \"%s\" should consist of only three alphabets [a-zA-Z].";

    /**
     * User-understandable error message for providing unknown destination airport code.
     */
    public static final String DEST_AIRPORT_CODE_IS_UNKNOWN = "The three-letter destination airport code provided \"%s\" does not correspond to a known airport!";

    /**
     * User-understandable error message for missing destination airport code.
     */
    public static final String DEST_AIRPORT_CODE_MISSING = "Destination airport code missing. Please provide destination airport code along with source airport code. ";

    /**
     * User-understandable error message for providing same source and destination airport code.
     */
    public static final String SRC_AND_DEST_AIRPORT_CODE_ARE_SAME = "Source and Destination airport code are same. Please provide different airport codes for flight's source and destination location. ";

    /**
     * User-understandable error message for providing invalid flight number.
     */
    public static final String FLIGHT_NUMBER_IS_INVALID = "The provided flight number \"%s\" is invalid. Flight number provided should consist of only numbers between 0-9. ";

    /**
     * User-understandable error message for providing invalid departure date.
     */
    public static final String DEPARTURE_DATE_IS_INVALID = "The provided departure date \"%s\" is invalid. ";

    /**
     * User-understandable error message for providing invalid arrival date.
     */
    public static final String ARRIVAL_DATE_IS_INVALID = "The provided arrival date \"%s\" is invalid. ";

    /**
     * User-understandable error message for providing invalid arrival date same or before departure date.
     */
    public static final String ARRIVAL_DATE_SHOULD_NOT_BE_SAME_OR_BEFORE_DEPARTURE_TIME = "The provided arrival date and time should not be before or same as the departure date and time.";

    /**
     * User-understandable error message for no direct flights between specified source and destination airport code.
     */
    public static final String NO_DIRECT_FLIGHTS = "There are no direct flights for the specified source and destination airport codes!";


    /**
     * Formats the error message about a missing parameter.
     *
     * @param parameterName
     *         String holding the request parameter name.
     *
     * @return the formatted error message.
     */
    public static String missingRequiredParameter(String parameterName) {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    /**
     * Formats the error message about a invalid parameter.
     *
     * @param parameterName
     *         String holding the request parameter name.
     * @param errorMessage
     *         String holding the error message.
     *
     * @return the formatted error message.
     */
    public static String invalidInputParameter(String parameterName, String errorMessage) {
        if (errorMessage.contains("\"%s\"")) {
            return String.format(errorMessage, parameterName);
        } else {
            return errorMessage;
        }
    }

    /**
     * This method returns message about deleting all airline entries.
     */
    public static String allAirlineEntriesDeleted() {
        return "All airline entries have been deleted";
    }
}
