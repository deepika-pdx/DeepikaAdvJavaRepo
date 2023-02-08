package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * This is <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AirlineParser<Airline> {
    /**
     * Reader for reading airline and flight data from a file.
     */
    private final Reader reader;

    /**
     * Creates a new <code>TextParser</code>
     *
     * @param reader
     *         reader for reading airline and flight data from a file.
     */
    public TextParser(Reader reader) {
        this.reader = reader;
    }

    @Override
    /**
     * This method reads the airline and flight data from a file.
     *
     * @return An airline object having flight details created by reading data from file.
     */
    public Airline parse() throws ParserException {
        try (BufferedReader br = new BufferedReader(this.reader)) {
            String textRead = br.readLine();
            String readAirlineName = null;
            int flightNumber = 0;
            String source = null;
            String depatureDateTime = null;
            Date departureDate = null;
            String destination = null;
            String arrivalDateTime = null;
            Date arrivalDate = null;
            if (textRead != null) {
                String[] airlineNameArray = textRead.split("=");
                if (airlineNameArray != null && airlineNameArray.length > 1) {
                    readAirlineName = airlineNameArray[1].trim();
                } else {
                    throw new ParserException("Malformed text file. Unable to parse data.");
                }
            }
            if (readAirlineName == null) {
                throw new ParserException("Missing airline name");
            } else {
                Airline readAirlineObj = new Airline(readAirlineName);
                String textContent = null;
                while ((textContent = br.readLine()) != null) {
                    if (textContent.contains("Flight number")) {
                        String[] flightNumberArray = textContent.split("=");
                        if (flightNumberArray != null && flightNumberArray.length > 1) {
                            flightNumber = Integer.parseInt(flightNumberArray[1].trim());
                        } else {
                            throw new ParserException("Malformed text file. Unable to parse data.");
                        }
                    } else if (textContent.contains("Source location")) {
                        String[] sourceArray = textContent.split("=");
                        if (sourceArray != null && sourceArray.length > 1) {
                            source = sourceArray[1].trim();
                        } else {
                            throw new ParserException("Malformed text file. Unable to parse data.");
                        }
                    } else if (textContent.contains("Departure date and time")) {
                        String[] depatureDateTimeArray = textContent.split("=");
                        if (depatureDateTimeArray != null && depatureDateTimeArray.length > 1) {
                            depatureDateTime = depatureDateTimeArray[1].trim();
                        } else {
                            throw new ParserException("Malformed text file. Unable to parse data.");
                        }
                    } else if (textContent.contains("Destination location")) {
                        String[] destinationArray = textContent.split("=");
                        if (destinationArray != null && destinationArray.length > 1) {
                            destination = destinationArray[1].trim();
                        } else {
                            throw new ParserException("Malformed text file. Unable to parse data.");
                        }
                    } else if (textContent.contains("Arrival date and time")) {
                        String[] arrivalDateTimeArray = textContent.split("=");
                        if (arrivalDateTimeArray != null && arrivalDateTimeArray.length > 1) {
                            arrivalDateTime = arrivalDateTimeArray[1].trim();
                        } else {
                            throw new ParserException("Malformed text file. Unable to parse data.");
                        }
                    } else if (textContent.contains("End of Flight")) {
                        if (flightNumber == 0 || source == null || depatureDateTime == null || destination == null || arrivalDateTime == null) {
                            throw new ParserException("Malformed text file. Unable to parse data.");
                        }
                        validateSourceAndDestParameters(source, destination);
                        Date[] dateArray = validateDepartureAndArrivalDateTime(depatureDateTime, arrivalDateTime);
                        departureDate = dateArray[0];
                        arrivalDate = dateArray[1];
                        Flight readFlight = new Flight(flightNumber, source, depatureDateTime, departureDate, destination, arrivalDateTime, arrivalDate);
                        readAirlineObj.addFlight(readFlight);
                    }
                }
                return readAirlineObj;
            }
        } catch (AirlineException e) {
            String errorMessage = e.getMessage();
            if (e.getMessage().contains("Please")) {
                errorMessage = "Malformed text file. Unable to parse data.";
            }
            throw new ParserException(e.getMessage());
        } catch (IOException e) {
            throw new ParserException("Malformed text file. Unable to parse data.", e);
        }
    }

    private void validateSourceAndDestParameters(String srcLocation, String destLocation) throws AirlineException {

        // Validation of the provided source location
        if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
            throw new AirlineException("Invalid source location. Unable to parse the provided text file.");
        } else if (AirportNames.getName(srcLocation.toUpperCase()) == null) {
            throw new AirlineException("The three-letter source location code does not correspond to a known airport!");
        } else {
            srcLocation = srcLocation.toUpperCase();
        }


        // Validation of the provided destination location
        if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
            throw new AirlineException("Invalid destination location. Unable to parse the provided text file.");
        } else if (AirportNames.getName(destLocation.toUpperCase()) == null) {
            throw new AirlineException("The three-letter destination location code does not correspond to a known airport!");
        } else {
            destLocation = destLocation.toUpperCase();
        }

        if (srcLocation.equals(destLocation)) {
            throw new AirlineException("The source and the destination location code should not be same.");
        }
    }

    private Date[] validateDepartureAndArrivalDateTime(String depatureDateTime, String arrivalDateTime) throws AirlineException {

        Date[] flightDates = new Date[2];
        // Validation of the departure date and time
        String[] departureDateTimeArray = depatureDateTime.split(" ");
        if (departureDateTimeArray.length != 3) {
            throw new AirlineException("Malformed text file. Unable to parse data.");
        }
        Date departureDate = null;
        departureDate = AirlineDateTimeValidator.validateDateAndTime(departureDateTimeArray[0], departureDateTimeArray[1], departureDateTimeArray[2], "Departure");

        // Validation of the arrival date and time
        String[] arrivalDateTimeArray = arrivalDateTime.split(" ");
        if (arrivalDateTimeArray.length != 3) {
            throw new AirlineException("Malformed text file. Unable to parse data.");
        }
        Date arrivalDate = null;
        arrivalDate = AirlineDateTimeValidator.validateDateAndTime(arrivalDateTimeArray[0], arrivalDateTimeArray[1], arrivalDateTimeArray[2], "Arrival");

        if (arrivalDate.before(departureDate)) {
            throw new AirlineException("The arrival date and time is before the departure date and time. Unable to parse the provided text file.");
        }
        flightDates[0] = departureDate;
        flightDates[1] = arrivalDate;
        return flightDates;
    }
}
