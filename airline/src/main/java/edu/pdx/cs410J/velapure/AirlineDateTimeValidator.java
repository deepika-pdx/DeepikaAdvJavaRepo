package edu.pdx.cs410J.velapure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AirlineDateTimeValidator {
    /**
     * Validates the input departure and arrival date.
     *
     * @param providedDateString
     *         The date of departure/arrival of the flight.
     *
     * @return true if input date is valid and false if input date is invalid.
     */
    public static Date validateDateAndTime(String providedDateString, String providedTimeString, String providedAmOrPmString, String flightDateType)
            throws AirlineException {
        Date flightDateAndTime = null;
        String matchedFlightDateFormatPattern = null;
        String matchedFlightTimeFormatPattern = null;

        if (!providedAmOrPmString.equalsIgnoreCase("AM") && !providedAmOrPmString.equalsIgnoreCase("PM")) {
            switch (flightDateType) {
                case "Departure":
                    throw new AirlineException("Invalid departure time. Please provide 'AM/am or PM/pm along with time!");
                case "Arrival":
                    throw new AirlineException("Invalid arrival time. Please provide 'AM/am or PM/pm along with time!");
            }
        }
        matchedFlightDateFormatPattern = validateDate(providedDateString);
        matchedFlightTimeFormatPattern = validateTime(providedTimeString);

        if (matchedFlightDateFormatPattern == null) {
            switch (flightDateType) {
                case "Departure":
                    throw new AirlineException("Invalid departure date. Please provide the departure date in mm/dd/yyyy format.");
                case "Arrival":
                    throw new AirlineException("Invalid arrival date. Please provide the arrival date in mm/dd/yyyy format.");
            }
        } else if (matchedFlightTimeFormatPattern == null) {
            switch (flightDateType) {
                case "Departure":
                    throw new AirlineException("Invalid departure time. Please provide the departure time in 12-hour(hh:mm) format.");
                case "Arrival":
                    throw new AirlineException("Invalid arrival time. Please arrival the arrival time in 12-hour(hh:mm) format.");
            }
        }
        try {
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(matchedFlightDateFormatPattern + " " + matchedFlightTimeFormatPattern + " " + "aa");
            flightDateAndTime = dateTimeFormatter.parse(providedDateString + " " + providedTimeString + " " + providedAmOrPmString);
        } catch (ParseException e) {
            switch (flightDateType) {
                case "Departure":
                    throw new AirlineException("Invalid departure date and/or time. Please provide the departure date in mm/dd/yyyy format and " +
                            "Please provide the departure time in 12-hour(hh:mm) format.");
                case "Arrival":
                    throw new AirlineException("Invalid arrival date and/or time. Please provide the arrival date in mm/dd/yyyy format and " +
                            "Please provide the arrival time in 12-hour(hh:mm) format.");
            }
        }
        return flightDateAndTime;
    }

    /**
     * Validates the input departure and arrival date.
     *
     * @param providedDateString
     *         The date of departure/arrival of the flight.
     *
     * @return true if input date is valid and false if input date is invalid.
     */
    public static String validateDate(String providedDateString) {
        String datePattern = null;
        try {
            datePattern = "MM/dd/yyyy";
            SimpleDateFormat dateFormatterOne = new SimpleDateFormat(datePattern);
            Date formattedDateTypeOne = dateFormatterOne.parse(providedDateString);
            if (dateFormatterOne.format(formattedDateTypeOne).equals(providedDateString)) {
                return datePattern;
            }

            datePattern = "M/dd/yyyy";
            SimpleDateFormat dateFormatterTwo = new SimpleDateFormat(datePattern);
            Date formattedDateTypeTwo = dateFormatterTwo.parse(providedDateString);
            if (dateFormatterTwo.format(formattedDateTypeTwo).equals(providedDateString)) {
                return datePattern;
            }

            datePattern = "MM/d/yyyy";
            SimpleDateFormat dateFormatterThree = new SimpleDateFormat(datePattern);
            Date formattedDateTypeThree = dateFormatterThree.parse(providedDateString);
            if (dateFormatterThree.format(formattedDateTypeThree).equals(providedDateString)) {
                return datePattern;
            }

            datePattern = "M/d/yyyy";
            SimpleDateFormat dateFormatterFour = new SimpleDateFormat(datePattern);
            Date formattedDateTypeFour = dateFormatterThree.parse(providedDateString);
            if (dateFormatterFour.format(formattedDateTypeFour).equals(providedDateString)) {
                return datePattern;
            }

            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Validates the input departure and arrival time.
     *
     * @param providedTimeString
     *         The time of departure/arrival of the flight.
     *
     * @return true if input time is valid and false if input time is invalid.
     */
    public static String validateTime(String providedTimeString) {
        String timePattern = null;
        String[] providedTimeArray = providedTimeString.split(":");
        String hourPattern = null;
        String minPattern = null;
        if (providedTimeArray.length == 2) {
            try {
                int providedHour = Integer.parseInt(providedTimeArray[0]);
                int providedMinute = Integer.parseInt(providedTimeArray[1]);
                if (!(providedHour >= 1 && providedHour <= 12 && providedMinute >= 0 && providedMinute <= 59)) {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }

            char[] hourStringArray = providedTimeArray[0].toCharArray();
            char[] minStringArray = providedTimeArray[1].toCharArray();

            switch (hourStringArray.length) {
                case 1:
                    hourPattern = "h";
                    break;
                case 2:
                    hourPattern = "hh";
                    break;
            }
            switch (minStringArray.length) {
                case 1:
                    minPattern = "m";
                    break;
                case 2:
                    minPattern = "mm";
                    break;
            }

            if (hourPattern == null || minPattern == null) {
                return timePattern;
            }
            timePattern = hourPattern + ":" + minPattern;
            return timePattern;
        } else {
            return timePattern;
        }
    }
}

