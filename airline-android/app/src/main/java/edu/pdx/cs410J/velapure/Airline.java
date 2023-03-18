package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is represents an <code>Airline</code>.
 */
public class Airline extends AbstractAirline<Flight> {
    /**
     * Name of the airline
     */
    private final String name;

    /**
     * Flights of the airline
     */
    private List<Flight> flights;

    /**
     * Creates a new <code>Airline</code>
     *
     * @param name
     *         The Airline's name.
     */
    public Airline(String name) {
        this.name = name;
        this.flights = new ArrayList<Flight>();
    }

    /**
     * Returns the name of the airline.
     *
     * @return A name that identifies the airline.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Adds a flight to the airline.
     */
    @Override
    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    /**
     * Returns the list of flights of the airline.
     *
     * @return A list of flights of the airline.
     */
    @Override
    public Collection<Flight> getFlights() {
        return this.flights;
    }
}
