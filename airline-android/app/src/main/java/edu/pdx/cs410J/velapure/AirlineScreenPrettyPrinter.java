package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * This is <code>AirlineScreenPrettyPrinter</code> class for Project.
 */
public class AirlineScreenPrettyPrinter {

    /**
     * StringBuilder for pretty printing the airline and flight data to the screen.
     */
    private StringBuilder prettyStringBuilder;

    /**
     * Creates a new <code>AirlineScreenPrettyPrinter</code>
     *
     */
    public AirlineScreenPrettyPrinter() throws IOException {
        this.prettyStringBuilder = new StringBuilder();
    }

    /**
     * This method pretty prints the airline and flight data to the screen.
     *
     * @param prettyPrintAirline
     *         An airline object having flight details.
     */
    public String convertToPrettyText(AbstractAirline prettyPrintAirline, Optional<String> srcAirportCode, Optional<String> destAirportCode) throws IOException {
        Collections.sort((List) prettyPrintAirline.getFlights());

        prettyStringBuilder.append("******* Flight Information of the " + prettyPrintAirline.getName() + " airline " + "*******");
        prettyStringBuilder.append("\n");

        DateFormat flightDateFormatter = null;
        DateFormat flightTimeFormatter = null;
        Iterator flightIterator = prettyPrintAirline.getFlights().iterator();
        while (flightIterator.hasNext()) {
            Flight flight = (Flight) flightIterator.next();
            if (srcAirportCode.isPresent() && destAirportCode.isPresent()) {
                if (flight.getSource().equals(srcAirportCode.get()) && flight.getDestination().equals(destAirportCode.get())) {
                    prettyStringBuilder.append("\n");
                    prettyStringBuilder.append("Flight " + flight.getNumber() + " details:");
                    prettyStringBuilder.append("\n");
                    prettyStringBuilder.append("    Source: " + AirportNames.getName(flight.getSource()) + "(" + flight.getSource() + ")");
                    prettyStringBuilder.append("\n");
                    flightDateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
                    flightTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
                    prettyStringBuilder.append("    Departure date and time: " +
                            flightDateFormatter.format(flight.getDeparture()) + " " + flightTimeFormatter.format(flight.getDeparture()));
                    prettyStringBuilder.append("\n");
                    prettyStringBuilder.append("    Destination: " + AirportNames.getName(flight.getDestination()) + "(" + flight.getDestination() + ")");
                    prettyStringBuilder.append("\n");
                    prettyStringBuilder.append("    Arrival date and time: " + flightDateFormatter.format(flight.getArrival()) + " " + flightTimeFormatter.format(flight.getArrival()));
                    prettyStringBuilder.append("\n");
                    long flightDifference = (flight.getArrival().getTime() - flight.getDeparture().getTime());
                    long flightDuration = flightDifference / (60 * 1000);
                    prettyStringBuilder.append("    Flight duration: " + flightDuration + " minutes");
                    prettyStringBuilder.append("\n");
                }
            } else {
                prettyStringBuilder.append("\n");
                prettyStringBuilder.append("Flight " + flight.getNumber() + " details:");
                prettyStringBuilder.append("\n");
                prettyStringBuilder.append("    Source: " + AirportNames.getName(flight.getSource()) + "(" + flight.getSource() + ")");
                prettyStringBuilder.append("\n");
                flightDateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
                flightTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
                prettyStringBuilder.append("    Departure date and time: " +
                        flightDateFormatter.format(flight.getDeparture()) + " " + flightTimeFormatter.format(flight.getDeparture()));
                prettyStringBuilder.append("\n");
                prettyStringBuilder.append("    Destination: " + AirportNames.getName(flight.getDestination()) + "(" + flight.getDestination() + ")");
                prettyStringBuilder.append("\n");
                prettyStringBuilder.append("    Arrival date and time: " + flightDateFormatter.format(flight.getArrival()) + " " + flightTimeFormatter.format(flight.getArrival()));
                prettyStringBuilder.append("\n");
                long flightDifference = (flight.getArrival().getTime() - flight.getDeparture().getTime());
                long flightDuration = flightDifference / (60 * 1000);
                prettyStringBuilder.append("    Flight duration: " + flightDuration + " minutes");
                prettyStringBuilder.append("\n");
            }
        }
        return prettyStringBuilder.toString();
    }
}