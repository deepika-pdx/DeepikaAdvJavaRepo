package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

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
            String destination = null;
            String arrivalDateTime = null;
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
                        Flight readFlight = new Flight(flightNumber, source, depatureDateTime, destination, arrivalDateTime);
                        readAirlineObj.addFlight(readFlight);
                    }
                }
                return readAirlineObj;
            }
        } catch (IOException e) {
            throw new ParserException("Malformed text file. Unable to parse data.", e);
        }
    }
}
