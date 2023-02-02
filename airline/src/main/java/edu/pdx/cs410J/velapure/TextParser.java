package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A skeletal implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AirlineParser<Airline> {
    private final Reader reader;

    public TextParser(Reader reader) {
        this.reader = reader;
    }

    @Override
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
                readAirlineName = textRead.split("=")[1].trim();
            }
            if (readAirlineName == null) {
                throw new ParserException("Missing airline name");
            } else {
                Airline readAirlineObj = new Airline(readAirlineName);
                String textContent = null;
                while ((textContent = br.readLine()) != null) {
                    if (textContent.contains("Flight number")) {
                        flightNumber = Integer.parseInt(textContent.split("=")[1].trim());
                    } else if (textContent.contains("Source location")) {
                        source = textContent.split("=")[1].trim();
                    } else if (textContent.contains("Departure date and time")) {
                        depatureDateTime = textContent.split("=")[1].trim();
                    } else if (textContent.contains("Destination location")) {
                        destination = textContent.split("=")[1].trim();
                    } else if (textContent.contains("Arrival date and time")) {
                        arrivalDateTime = textContent.split("=")[1].trim();
                    } else if (textContent.contains("End of Flight")) {
                        Flight readFlight = new Flight(flightNumber, source, depatureDateTime, destination, arrivalDateTime);
                        readAirlineObj.addFlight(readFlight);
                    }
                }
                return readAirlineObj;
            }
        } catch (IOException e) {
            throw new ParserException("While parsing airline text", e);
        }
    }
}
