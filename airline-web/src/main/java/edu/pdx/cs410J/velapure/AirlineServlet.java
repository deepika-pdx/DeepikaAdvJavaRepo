package edu.pdx.cs410J.velapure;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.AirportNames;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * This is <code>AirlineServlet</code> class ultimately provides a REST API for working with an
 * <code>Airline</code>.
 */
public class AirlineServlet extends HttpServlet {

    /**
     * String containing the airline parameter.
     */
    static final String AIRLINE_PARAMETER = "airline";

    /**
     * String containing the source airport code parameter.
     */
    static final String SRC_PARAMETER = "src";

    /**
     * String containing the destination airport code parameter.
     */
    static final String DEST_PARAMETER = "dest";

    /**
     * String containing the flight number parameter.
     */
    static final String FLIGHT_NO_PARAMETER = "flightNumber";

    /**
     * String containing the flight departure parameter.
     */
    static final String DEPARTURE_PARAMETER = "depart";

    /**
     * String containing the flight arrival parameter.
     */
    static final String ARRIVAL_PARAMETER = "arrive";

    /**
     * Map storing the airlines.
     */
    private final Map<String, Airline> airlineFlightMap = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the flights of the
     * airline specified in the "airline" HTTP parameter to the HTTP response. If there are "src" and "dest" also
     * parameters in the HTTP request along with "airline"then the flights with the request src and dest
     * of the specified airline are written to the response.
     *
     * @param request
     *         A HttpServletRequest request.
     * @param response
     *         A HttpServletResponse response.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String airlineName = getParameter(AIRLINE_PARAMETER, request);
        String srcAirportCode = getParameter(SRC_PARAMETER, request);
        String destAirportCode = getParameter(DEST_PARAMETER, request);
        if (airlineName != null) {
            if (srcAirportCode != null) {
                try {
                    // Validation of the provided source location
                    srcAirportCode = validateSrcAirportCode(response, srcAirportCode);
                } catch (AirlineException e) {
                    specifiedParameterIsInvalid(response, srcAirportCode, e.getMessage());
                    return;
                }
            }
            if (destAirportCode != null) {
                try {
                    destAirportCode = validateDestAirportCode(response, destAirportCode);
                } catch (AirlineException e) {
                    specifiedParameterIsInvalid(response, destAirportCode, e.getMessage());
                    return;
                }
            }

            if (srcAirportCode != null && destAirportCode == null) {
                specifiedParameterIsInvalid(response, "", Messages.DEST_AIRPORT_CODE_MISSING);
                return;
            }

            if (destAirportCode != null && srcAirportCode == null) {
                specifiedParameterIsInvalid(response, "", Messages.SRC_AIRPORT_CODE_MISSING);
                return;
            }


            if (srcAirportCode != null && destAirportCode != null && srcAirportCode.equals(destAirportCode)) {
                specifiedParameterIsInvalid(response, srcAirportCode, Messages.SRC_AND_DEST_AIRPORT_CODE_ARE_SAME);
                return;
            }
            Optional<String> srcAirport = null;
            if (srcAirportCode != null) {
                srcAirport = Optional.of(srcAirportCode);
            } else {
                srcAirport = Optional.empty();
            }

            Optional<String> destAirport = null;
            if (destAirportCode != null) {
                destAirport = Optional.of(destAirportCode);
            } else {
                destAirport = Optional.empty();
            }
            writeAirlineAndFlightInformation(airlineName, srcAirport, destAirport, response);
        } else {
            missingRequiredParameter(response, AIRLINE_PARAMETER);
        }
    }

    /**
     * Handles an HTTP POST request by storing the flight details for the
     * "airline" and flight details request parameters.  It writes the airline details
     * to the HTTP response.
     *
     * @param request
     *         A HttpServletRequest request.
     * @param response
     *         A HttpServletResponse response.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String airlineName = getParameter(AIRLINE_PARAMETER, request);
        if (airlineName == null) {
            missingRequiredParameter(response, AIRLINE_PARAMETER);
            return;
        }

        String flightNumberString = getParameter(FLIGHT_NO_PARAMETER, request);
        int flightNumber = 0;
        if (flightNumberString == null) {
            missingRequiredParameter(response, FLIGHT_NO_PARAMETER);
            return;
        } else {
            try {
                flightNumber = Integer.parseInt(flightNumberString);
            } catch (NumberFormatException e) {
                specifiedParameterIsInvalid(response, flightNumberString, Messages.FLIGHT_NUMBER_IS_INVALID);
                return;
            }
        }

        String srcAirportCode = getParameter(SRC_PARAMETER, request);
        if (srcAirportCode == null) {
            missingRequiredParameter(response, SRC_PARAMETER);
            return;
        } else {
            try {
                srcAirportCode = validateSrcAirportCode(response, srcAirportCode);
            } catch (AirlineException e) {
                specifiedParameterIsInvalid(response, srcAirportCode, e.getMessage());
                return;
            }
        }

        String destAirportCode = getParameter(DEST_PARAMETER, request);
        if (destAirportCode == null) {
            missingRequiredParameter(response, DEST_PARAMETER);
            return;
        } else {
            try {
                // Validation of the provided destination location
                destAirportCode = validateDestAirportCode(response, destAirportCode);
            } catch (AirlineException e) {
                specifiedParameterIsInvalid(response, destAirportCode, e.getMessage());
                return;
            }
        }

        if (srcAirportCode.equals(destAirportCode)) {
            specifiedParameterIsInvalid(response, srcAirportCode, Messages.SRC_AND_DEST_AIRPORT_CODE_ARE_SAME);
            return;
        }

        String departureDateTimeString = getParameter(DEPARTURE_PARAMETER, request);
        Date departureDate = null;
        String departureFlightString = null;
        if (departureDateTimeString == null) {
            missingRequiredParameter(response, DEPARTURE_PARAMETER);
            return;
        } else {
            String[] departureDateArray = departureDateTimeString.split(" ");
            if (departureDateArray == null || departureDateArray.length != 3) {
                specifiedParameterIsInvalid(response, departureDateTimeString, Messages.DEPARTURE_DATE_IS_INVALID);
                return;
            }
            String departureDateString = departureDateArray[0];
            String departureTimeString = departureDateArray[1];
            String departureTimeIndication = departureDateArray[2];

            try {
                departureDate = AirlineDateTimeValidator.validateDateAndTime(departureDateString, departureTimeString, departureTimeIndication, "Departure");
                departureFlightString = departureDateString + " " + departureTimeString + " " + departureTimeIndication;
            } catch (AirlineException e) {
                specifiedParameterIsInvalid(response, departureDateTimeString, e.getMessage());
                return;
            }
        }

        String arrivalDateTimeString = getParameter(ARRIVAL_PARAMETER, request);
        Date arrivalDate = null;
        String arrivalFlightString = null;
        if (arrivalDateTimeString == null) {
            missingRequiredParameter(response, ARRIVAL_PARAMETER);
            return;
        } else {
            String[] arrivalDateArray = arrivalDateTimeString.split(" ");
            if (arrivalDateArray == null || arrivalDateArray.length != 3) {
                specifiedParameterIsInvalid(response, arrivalDateTimeString, Messages.ARRIVAL_DATE_IS_INVALID);
                return;
            }
            String arrivalDateString = arrivalDateArray[0];
            String arrivalTimeString = arrivalDateArray[1];
            String arrivalTimeIndication = arrivalDateArray[2];

            try {
                arrivalDate = AirlineDateTimeValidator.validateDateAndTime(arrivalDateString, arrivalTimeString, arrivalTimeIndication, "Arrival");
                arrivalFlightString = arrivalDateString + " " + arrivalTimeString + " " + arrivalTimeIndication;
            } catch (AirlineException e) {
                specifiedParameterIsInvalid(response, arrivalDateTimeString, e.getMessage());
                return;
            }
        }

        if (arrivalDate.before(departureDate) || arrivalDate.equals(departureDate)) {
            specifiedParameterIsInvalid(response, arrivalDateTimeString, Messages.ARRIVAL_DATE_SHOULD_NOT_BE_SAME_OR_BEFORE_DEPARTURE_TIME);
            return;
        }

        Airline airlineObj = airlineFlightMap.get(airlineName.toUpperCase());
        Flight flight = null;
        if (airlineObj == null) {
            airlineObj = new Airline(airlineName);
            flight = new Flight(flightNumber, srcAirportCode, departureFlightString, departureDate, destAirportCode, arrivalFlightString, arrivalDate);
            airlineObj.addFlight(flight);
            airlineFlightMap.put(airlineName.toUpperCase(), airlineObj);
        } else {
            flight = new Flight(flightNumber, srcAirportCode, departureFlightString, departureDate, destAirportCode, arrivalFlightString, arrivalDate);
            airlineObj.addFlight(flight);
            airlineFlightMap.put(airlineName.toUpperCase(), airlineObj);
        }

        PrintWriter pw = response.getWriter();
        pw.println(flight.toString());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Validates the specified source airport code.
     *
     * @param response
     *         A HttpServletResponse response.
     * @param srcAirportCode
     *         Specified source airport code.
     *
     * @return validated source airport code.
     */
    private String validateSrcAirportCode(HttpServletResponse response, String srcAirportCode) throws IOException {
        // Validation of the provided source location
        if (!(Pattern.matches("[a-zA-Z]+", srcAirportCode)) || srcAirportCode.length() != 3) {
            throw new AirlineException(Messages.SRC_AIRPORT_CODE_IS_NOT_VALID);
        } else if (AirportNames.getName(srcAirportCode.toUpperCase()) == null) {
            throw new AirlineException(Messages.SRC_AIRPORT_CODE_IS_UNKNOWN);
        } else {
            srcAirportCode = srcAirportCode.toUpperCase();
        }
        return srcAirportCode;
    }

    /**
     * Validates the specified destination airport code.
     *
     * @param response
     *         A HttpServletResponse response.
     * @param destAirportCode
     *         Specified destination airport code.
     *
     * @return validated destination airport code.
     */
    private String validateDestAirportCode(HttpServletResponse response, String destAirportCode) throws IOException {
        // Validation of the provided destination location
        if (!(Pattern.matches("[a-zA-Z]+", destAirportCode)) || destAirportCode.length() != 3) {
            throw new AirlineException(Messages.DEST_AIRPORT_CODE_IS_NOT_VALID);
        } else if (AirportNames.getName(destAirportCode.toUpperCase()) == null) {
            throw new AirlineException(Messages.DEST_AIRPORT_CODE_IS_UNKNOWN);
        } else {
            destAirportCode = destAirportCode.toUpperCase();
        }
        return destAirportCode;
    }

    /**
     * Handles an HTTP DELETE request by removing all airlineFlightMap entries.  This
     * behavior is exposed for testing purposes only.
     *
     * @param request
     *         A HttpServletRequest request.
     * @param response
     *         A HttpServletResponse response.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        this.airlineFlightMap.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allAirlineEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     *
     * @param response
     *         A HttpServletResponse response.
     * @param parameterName
     *         String holding the request parameter name.
     */
    private void missingRequiredParameter(HttpServletResponse response, String parameterName)
            throws IOException {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes an error message about an invalid parameter to the HTTP response.
     * The text of the error message is created by {@link Messages#invalidInputParameter(String, String)}
     *
     * @param response
     *         A HttpServletResponse response.
     * @param parameterName
     *         String holding the request parameter name.
     * @param errMsg
     *         String holding the error message.
     */
    private void specifiedParameterIsInvalid(HttpServletResponse response, String parameterName, String errMsg)
            throws IOException {
        String message = Messages.invalidInputParameter(parameterName, errMsg);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the flights of the given airline (from srcAirport to destAirport if specified) to the HTTP response.
     * The text of the message is formatted with {@link AirlineWebXmlDumper}
     *
     * @param airlineName
     *         Specified airline name.
     * @param srcAirport
     *         {@link Optional} parameter that holds the source airport.
     * @param destAirport
     *         {@link Optional} parameter that holds the destination airport.
     * @param response
     *         A HttpServletResponse response.
     */
    private void writeAirlineAndFlightInformation(String airlineName, Optional<String> srcAirport, Optional<String> destAirport, HttpServletResponse response) throws IOException {
        Airline fetchedAirline = this.airlineFlightMap.get(airlineName.toUpperCase());
        response.setContentType("text/xml");
        if (fetchedAirline == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            try {
                PrintWriter pw = response.getWriter();
                AirlineWebXmlDumper xmlDumper = new AirlineWebXmlDumper(pw);
                xmlDumper.dump(fetchedAirline, srcAirport, destAirport);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (AirlineException e) {
                if (e.getMessage().contains(Messages.NO_DIRECT_FLIGHTS)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
                } else {
                    throw new AirlineException(e.getMessage());
                }
            }
        }
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @param name
     *         name of the request parameter.
     * @param request
     *         A HttpServletRequest request.
     *
     * @return <code>null</code> if the value of the parameter is
     * <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
        String value = request.getParameter(name);
        if (value == null || "".equals(value)) {
            return null;
        } else {
            return value;
        }
    }

    /**
     * This method is visible only for testing.
     */
    @VisibleForTesting
    Airline getAirline(String airlineName) {
        return this.airlineFlightMap.get(airlineName.toUpperCase());
    }
}
