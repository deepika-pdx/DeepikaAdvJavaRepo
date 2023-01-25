package edu.pdx.cs410J.velapure;

import org.junit.jupiter.api.Test;

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
        Flight flight = createFlight(789, "DEL", "01/22/2023 12:30", "CHN", "01/22/2023 14:30");
        assertThat(flight.getNumber(), equalTo(789));
    }

    /**
     * This unit test checks if the source location provided in the constructor is correctly set in the
     * source variable of the class
     */
    @Test
    void testKOLIsReturnedAsSource() {
        Flight flight = createFlight(789, "DEL", "01/22/2023 12:30", "CHN", "01/22/2023 14:30");
        assertThat(flight.getSource(), equalTo("DEL"));
    }

    /**
     * This unit test checks if the departure time provided in the constructor is correctly set in the
     * departureString variable of the class
     */
    @Test
    void testDepartureTimeIsReturnedCorrectly() {
        Flight flight = createFlight(789, "DEL", "01/25/2023 15:30", "CHN", "01/22/2023 14:30");
        assertThat(flight.getDepartureString(), equalTo("01/25/2023 15:30"));
    }

    /**
     * This unit test checks if the destination location provided in the constructor is correctly set in the
     * destination variable of the class
     */
    @Test
    void testLONIsReturnedAsDestination() {
        Flight flight = createFlight(789, "DEL", "01/25/2023 15:30", "CHN", "01/22/2023 14:30");
        assertThat(flight.getDestination(), equalTo("CHN"));
    }

    /**
     * This unit test checks if the arrival time provided in the constructor is correctly set in the
     * arrivalString variable of the class
     */
    @Test
    void testArrivalTimeIsReturnedCorrectly() {
        Flight flight = createFlight(789, "DEL", "01/25/2023 15:30", "CHN", "01/26/2023 4:15");
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
    private static Flight createFlight(int flightNumber, String source, String departureString, String destination, String arrivalString) {
        Flight flight = new Flight(flightNumber, source, departureString, destination, arrivalString);
        return flight;
    }

//    @Test
//    void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
//        Flight flight = new Flight(789, "KOL", "01/25/2023 15:30", "LON", "01/26/2023 4:15");
//        assertThat(flight.getDeparture(), is(nullValue()));
//    }

}
