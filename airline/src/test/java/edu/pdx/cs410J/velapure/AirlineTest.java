package edu.pdx.cs410J.velapure;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Airline} class.
 */
public class AirlineTest {

    /**
     * This unit test checks if the named provided to the Airline class constructor
     * correctly sets the name variable of the class.
     */
    @Test
    void testAirlineNamedIndigoIsNamedIndigo() {
        String airlineName = "Indigo";
        Airline airline = new Airline(airlineName);
        assertThat(airline.getName(), equalTo(airlineName));
    }

    /**
     * This unit test checks if all the flights of an airline are returned correctly.
     */
    @Test
    void testAllFlightsOfTheAirlineAreReturned() {
        String airlineName = "Indigo";
        Airline airline = new Airline(airlineName);

        Flight flight855 = createFlight(855, "PUN", "01/22/2023 14:30", new Date(), "HYD", "01/22/2023 16:30", new Date());
        Flight flight111 = createFlight(111, "LON", "01/24/2023 5:30", new Date(), "PDX", "01/25/2023 12:30", new Date());
        airline.addFlight(flight855);
        airline.addFlight(flight111);

        assertThat(airline.getFlights(), is(notNullValue()));
        assertThat(airline.getFlights().size(), equalTo(2));
    }

    /**
     * Creates a new <code>Flight</code> for testing
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
     *
     * @return A flight object with specified parameters.
     */
    private static Flight createFlight(int flightNumber, String source, String departureString, Date departureDate, String destination, String arrivalString, Date arrivalDate) {
        Flight flight = new Flight(flightNumber, source, departureString, departureDate, destination, arrivalString, arrivalDate);
        return flight;
    }
}
