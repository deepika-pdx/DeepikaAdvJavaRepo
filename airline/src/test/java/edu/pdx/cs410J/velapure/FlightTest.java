package edu.pdx.cs410J.velapure;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {

    /**
     * This unit test checks if the flight number provided in the constructor is correctly set in the
     * flightNumber variable of the class
     */
    @Test
    void test789IsReturnedAsFlightNumber() {
        Flight flight = createFlight(789, "PDX", "01/22/2023 12:30", new Date(), "PHL", "01/22/2023 14:30", new Date());
        assertThat(flight.getNumber(), equalTo(789));
    }

    /**
     * This unit test checks if the source location provided in the constructor is correctly set in the
     * source variable of the class
     */
    @Test
    void testKOLIsReturnedAsSource() {
        Flight flight = createFlight(789, "MSP", "01/22/2023 12:30", new Date(), "PDX", "01/22/2023 14:30", new Date());
        assertThat(flight.getSource(), equalTo("MSP"));
    }

    /**
     * This unit test checks if the departure time provided in the constructor is correctly set in the
     * departureString variable of the class
     */
    @Test
    void testDepartureTimeIsReturnedCorrectly() {
        Flight flight = createFlight(789, "PDX", "01/25/2023 15:30", new Date(), "MSP", "01/22/2023 14:30", new Date());
        assertThat(flight.getDepartureString(), equalTo("01/25/2023 15:30"));
    }

    /**
     * This unit test checks if the destination location provided in the constructor is correctly set in the
     * destination variable of the class
     */
    @Test
    void testLONIsReturnedAsDestination() {
        Flight flight = createFlight(789, "PHL", "01/25/2023 15:30", new Date(), "PDX", "01/22/2023 14:30", new Date());
        assertThat(flight.getDestination(), equalTo("PDX"));
    }

    /**
     * This unit test checks if the arrival time provided in the constructor is correctly set in the
     * arrivalString variable of the class
     */
    @Test
    void testArrivalTimeIsReturnedCorrectly() {
        Flight flight = createFlight(789, "PDX", "01/25/2023 15:30", new Date(), "MSP", "01/26/2023 4:15", new Date());
        assertThat(flight.getArrivalString(), equalTo("01/26/2023 4:15"));
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
