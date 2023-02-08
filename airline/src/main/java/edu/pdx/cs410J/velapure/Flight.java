package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AbstractFlight;

import java.util.Date;

/**
 * This class is represents a <code>Flight</code>.
 */
public class Flight extends AbstractFlight {

    /**
     * Flight number of the flight
     */
    private int flightNumber;

    /**
     * Source location of the flight
     */
    private String source;

    /**
     * String representation of departure date and time of the flight
     */
    private String departureString;

    /**
     * Departure date and time of the flight
     */
    private Date departureDate;

    /**
     * Destination location of the flight
     */
    private String destination;

    /**
     * String representation of arrival date and time of the flight
     */
    private String arrivalString;

    /**
     * Arrival date and time of the flight
     */
    private Date arrivalDate;

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
    public Flight(int flightNumber, String source, String departureString, Date departureDate, String destination, String arrivalString, Date arrivalDate) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.departureString = departureString;
        this.departureDate = departureDate;
        this.destination = destination;
        this.arrivalString = arrivalString;
        this.arrivalDate = arrivalDate;
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
     * Returns the string representation of departure date and time of the flight from source location.
     *
     * @return The string representation of date and time of departure of the flight
     */
    public String getDepartureString() {
        return this.departureString;
    }

    /**
     * Returns the departure date and time of the flight from source location.
     *
     * @return The date and time of departure of the flight
     */
    @Override
    public Date getDeparture() {
        return this.departureDate;
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
     * Returns the string representation of arrival date and time of the flight at the destination location.
     *
     * @return The string representation ofdate and time of arrival of the flight
     */
    public String getArrivalString() {
        return this.arrivalString;
    }

    @Override

    /**
     * Returns the arrival date and time of the flight at the destination location.
     *
     * @return The date and time of arrival of the flight
     */
    public Date getArrival() {
        return this.arrivalDate;
    }
}
