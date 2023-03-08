package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirportNames;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * This is <code>AirlineWebPrettyPrinter</code> class for Project.
 */
public class AirlineWebPrettyPrinter {

    /**
     * Writer for pretty printing the airline and flight data to the file.
     */
    private final PrintWriter pw;

    /**
     * Creates a new <code>AirlineWebPrettyPrinter</code>
     *
     * @param pw
     *         writer for pretty printing airline and flight data to the file.
     */
    public AirlineWebPrettyPrinter(PrintWriter pw) throws IOException {
        this.pw = pw;
    }

    /**
     * This method pretty prints the airline and flight data to standard output terminal.
     *
     * @param prettyPrintAirline
     *         An airline object having flight details.
     */
    public void dumpToTerminal(AbstractAirline prettyPrintAirline) throws IOException {
        Collections.sort((List) prettyPrintAirline.getFlights());

        PrintStream airlinePrintWriter = System.out;
        airlinePrintWriter.println("***************** Flight Information of the " +
                prettyPrintAirline.getName() + " airline " + "*****************");
        airlinePrintWriter.println("");

        DateFormat flightDateFormatter = null;
        DateFormat flightTimeFormatter = null;
        Iterator flightIterator = prettyPrintAirline.getFlights().iterator();
        while (flightIterator.hasNext()) {
            Flight flight = (Flight) flightIterator.next();
            airlinePrintWriter.println("");
            airlinePrintWriter.println("Flight " + flight.getNumber() + " details:");
            airlinePrintWriter.println("    Source: " + AirportNames.getName(flight.getSource()) + "(" + flight.getSource() + ")");
            flightDateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
            flightTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
            airlinePrintWriter.println("    Departure date and time: " +
                    flightDateFormatter.format(flight.getDeparture()) + " " + flightTimeFormatter.format(flight.getDeparture()));
            airlinePrintWriter.println("    Destination: " + AirportNames.getName(flight.getDestination()) + "(" + flight.getDestination() + ")");
            airlinePrintWriter.println("    Arrival date and time: " + flightDateFormatter.format(flight.getArrival()) + " " + flightTimeFormatter.format(flight.getArrival()));
            long flightDifference = (flight.getArrival().getTime() - flight.getDeparture().getTime());
            long flightDuration = flightDifference / (60 * 1000);
            airlinePrintWriter.println("    Flight duration: " + flightDuration + " minutes");
            airlinePrintWriter.flush();
        }
    }
}
