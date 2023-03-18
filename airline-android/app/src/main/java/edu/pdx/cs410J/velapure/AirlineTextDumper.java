package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirlineDumper;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * This is <code>TextDumper</code> class for Project.
 */
public class AirlineTextDumper implements AirlineDumper<Airline> {
    /**
     * Writer for writing airline and flight data to the file.
     */
    private final Writer writer;

    /**
     * Creates a new <code>TextDumper</code>
     *
     * @param writer
     *         writer for writing airline and flight data to the file.
     */
    public AirlineTextDumper(Writer writer) {
        this.writer = writer;
    }

    @Override
    /**
     * This method writes the airline and flight data to a file.
     *
     * @param airline
     *         An airline object having flight details.
     */
    public void dump(Airline airline) {
        try (PrintWriter pw = new PrintWriter(this.writer)) {
            pw.println("Airline= " + airline.getName());
            pw.println("Flight count= " + airline.getFlights().size());
            ArrayList<Flight> flights = (ArrayList<Flight>) airline.getFlights();
            for (int i = 0; i < flights.size(); i++) {
                pw.println("");
                int flightCounter = i + 1;
                pw.println("Flight " + flightCounter + " data:");
                pw.println("Flight number= " + flights.get(i).getNumber());
                pw.println("Source location= " + flights.get(i).getSource());
                pw.println("Departure date and time= " + flights.get(i).getDepartureString());
                pw.println("Destination location= " + flights.get(i).getDestination());
                pw.println("Arrival date and time= " + flights.get(i).getArrivalString());
                pw.println("End of Flight " + flightCounter + " data!");
                pw.flush();
            }
        }
    }
}
