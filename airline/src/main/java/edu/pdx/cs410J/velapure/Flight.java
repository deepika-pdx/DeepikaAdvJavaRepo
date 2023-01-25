package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AbstractFlight;

/**
 * This class is represents a <code>Flight</code>.
 */
public class Flight extends AbstractFlight {

    private int flightNumber;
    private String source;
    private String destination;
    private String departureString;
    private String arrivalString;

    /**
     * Creates a new <code>Flight</code>
     *
     * @param flightNumber
     *         A unique number that identifies the flight.
     * @param source
     *         A three-letter code specifying the source location of a flight.
     * @param departureString
     *         The date and time of departure of the flight from source location.
     * @param destination
     *         A three-letter code specifying the destination location of a flight.
     * @param arrivalString
     *         The date and time of arrival of the flight at the destination location.
     */
    public Flight(int flightNumber, String source, String departureString, String destination, String arrivalString) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.departureString = departureString;
        this.destination = destination;
        this.arrivalString = arrivalString;
    }

    @Override
    /**
     * Returns the flight number of the flight.
     *
     * @return A unique number that identifies the flight.
     */
    public int getNumber() {
        return this.flightNumber;
    }

    @Override
    /**
     * Returns the source location of the flight.
     *
     * @return A three-letter code specifying the source location of a flight.
     */
    public String getSource() {
        return this.source;
    }

    @Override
    /**
     * Returns the departure date and time of the flight from source location.
     *
     * @return The date and time of departure of the flight
     */
    public String getDepartureString() {
        return this.departureString;
    }

    @Override
    /**
     * Returns the destination location of the flight.
     *
     * @return A three-letter code specifying the destination location of a flight.
     */
    public String getDestination() {
        return this.destination;
    }

    @Override
    /**
     * Returns the arrival date and time of the flight at the destination location.
     *
     * @return The date and time of arrival of the flight
     */
    public String getArrivalString() {
        return this.arrivalString;
    }
}
