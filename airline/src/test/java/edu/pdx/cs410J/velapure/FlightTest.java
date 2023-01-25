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
        Flight flight = createFlight();
        assertThat(flight.getNumber(), equalTo(789));
    }

    /**
     * This unit test checks if the source location provided in the constructor is correctly set in the
     * source variable of the class
     */
    @Test
    void testKOLIsReturnedAsSource() {
        Flight flight = createFlight();
        assertThat(flight.getSource(), equalTo("KOL"));
    }

    /**
     * This unit test checks if the departure time provided in the constructor is correctly set in the
     * departureString variable of the class
     */
    @Test
    void testDepartureTimeIsReturnedCorrectly() {
        Flight flight = createFlight();
        assertThat(flight.getDepartureString(), equalTo("01/25/2023 15:30"));
    }

    /**
     * This unit test checks if the destination location provided in the constructor is correctly set in the
     * destination variable of the class
     */
    @Test
    void testLONIsReturnedAsDestination() {
        Flight flight = createFlight();
        assertThat(flight.getDestination(), equalTo("LON"));
    }

    /**
     * This unit test checks if the arrival time provided in the constructor is correctly set in the
     * arrivalString variable of the class
     */
    @Test
    void testArrivalTimeIsReturnedCorrectly() {
        Flight flight = createFlight();
        assertThat(flight.getArrivalString(), equalTo("01/26/2023 4:15"));
    }

    private static Flight createFlight() {
        int flightNumber = 789;
        String source = "KOL";
        String departureString = "01/25/2023 15:30";
        String destination = "LON";
        String arrivalString = "01/26/2023 4:15";
        Flight flight = new Flight(flightNumber, source, departureString, destination, arrivalString);
        return flight;
    }

//    @Test
//    void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
//        Flight flight = new Flight(789, "KOL", "01/25/2023 15:30", "LON", "01/26/2023 4:15");
//        assertThat(flight.getDeparture(), is(nullValue()));
//    }

}
