package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@TestMethodOrder(MethodName.class)
class AirlineRestClientIT {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    private AirlineRestClient newAirlineRestClient() {
        int port = Integer.parseInt(PORT);
        return new AirlineRestClient(HOSTNAME, port);
    }

    @Test
    void test01RemoveAllDictionaryEntries() throws IOException {
        AirlineRestClient client = newAirlineRestClient();
        client.removeAllAirlineEntries();
    }


    @Test
    void test02AddedOneFlight() throws IOException, ParserException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";
        client.addFlightToTheSpecifiedAirline(airlineName, "789", "PHL", "6/19/2023 12:59 AM", "MSP", "06/19/2023 4:00 AM");

        Airline fetchedAirline = client.getAllFlightsOfAnAirline(airlineName);
        assertThat(airlineName, equalTo(fetchedAirline.getName()));
    }

    @Test
    void test03EmptyAirlineThrowsException() {
        AirlineRestClient client = newAirlineRestClient();
        String emptyString = "";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getAllFlightsOfAnAirline(emptyString));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), equalTo(Messages.missingRequiredParameter(AirlineServlet.AIRLINE_PARAMETER)));
    }

    @Test
    void test04InvalidSourceAirportCodeThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "PD5", "LAS"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Invalid source airport code."));
    }

    @Test
    void test05InvalidDestAirportCodeThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "PDX", "L7"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Invalid destination airport code."));
    }

    @Test
    void test06SrcAndDestAirportCodeAreSameThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "PDX", "PDX"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Source and Destination airport code are same."));
    }

    @Test
    void test07SrcAirportCodeIsUnknownThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "POX", "PDX"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("The three-letter source airport code provided \"POX\" does not correspond to a known airport!"));
    }

    @Test
    void test08SrcIsPresentButDestAirportCodeIsMissingThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "PDX", ""));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Destination airport code missing."));
    }

    @Test
    void test09EmptyAirlinePOSTThrowsException() {
        AirlineRestClient client = newAirlineRestClient();
        String emptyString = "";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline(emptyString, "789", "PHL", "6/19/2023 12:59 AM", "MSP", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), equalTo(Messages.missingRequiredParameter(AirlineServlet.AIRLINE_PARAMETER)));
    }

    @Test
    void test10InvalidFlightNumberThrowsException() {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789l", "PHL", "6/19/2023 12:59 AM", "MSP", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("The provided flight number \"789l\" is invalid."));
    }

    @Test
    void test11POSTInvalidSourceAirportCodeThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "89PDX", "6/19/2023 12:59 AM", "MSP", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Invalid source airport code."));
    }

    @Test
    void test12POSTInvalidDestAirportCodeThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "PDX", "6/19/2023 12:59 AM", "MS0", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Invalid destination airport code."));
    }

    @Test
    void test13POSTSrcAndDestAirportCodeAreSameThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "MSP", "6/19/2023 12:59 AM", "MSp", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Source and Destination airport code are same."));
    }

    @Test
    void test14POSTDestAirportCodeIsUnknownThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "PDX", "6/19/2023 12:59 AM", "MSD", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("The three-letter destination airport code provided \"MSD\" does not correspond to a known airport!"));
    }

    @Test
    void test15POSTDepartDateIsInvalidThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "PDX", "6/19/2023 12:59", "MSP", "06/19/2023 4:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("The provided departure date \"6/19/2023 12:59\" is invalid."));
    }

    @Test
    void test16POSTArriveDateIsMissingThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "PDX", "6/19/2023 12:59 AM", "MSP", ""));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), equalTo(Messages.missingRequiredParameter(AirlineServlet.ARRIVAL_PARAMETER)));
    }

    @Test
    void test17POSTArrivalDateIsInvalidThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "PDX", "6/19/2023 12:59 AM", "MSP", "06/19/2023 14:00 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("Invalid arrival time. Please arrival the arrival time in 12-hour(hh:mm) format."));
    }

    @Test
    void test18POSTArrivalDateIsSameAsDepartDateThrowsException() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlightToTheSpecifiedAirline("IndiGo", "789", "PDX", "6/19/2023 12:59 AM", "MSP", "6/19/2023 12:59 AM"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), containsString("The provided arrival date and time should not be before or same as the departure date and time."));
    }

    @Test
    void test19POSTCreateAirlineAndFlight() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "Frontier";
        client.addFlightToTheSpecifiedAirline(airlineName, "874", "LAS", "8/28/2023 10:30 AM", "MSP", "08/28/2023 12:30 PM");

        Airline fetchedAirline = client.getAllFlightsOfAnAirline(airlineName);
        assertThat(airlineName, equalTo(fetchedAirline.getName()));
        assertThat(1, equalTo(fetchedAirline.getFlights().size()));
    }

    @Test
    void test20POSTAddFlightToPreviousAirline() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "Frontier";
        client.addFlightToTheSpecifiedAirline(airlineName, "874", "LAS", "3/24/2023 10:30 AM", "MSP", "03/24/2023 12:30 PM");

        Airline fetchedAirline = client.getAllFlightsOfAnAirline(airlineName);
        assertThat(airlineName, equalTo(fetchedAirline.getName()));
        assertThat(2, equalTo(fetchedAirline.getFlights().size()));
    }

    @Test
    void test21POSTAddFlightToPreviousAirlineWithDifferentSrcAndDest() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "Frontier";
        client.addFlightToTheSpecifiedAirline(airlineName, "874", "MSP", "3/24/2023 10:30 AM", "LAS", "03/24/2023 12:30 PM");

        Airline fetchedAirline = client.getAllFlightsOfAnAirline(airlineName);
        assertThat(airlineName, equalTo(fetchedAirline.getName()));
        assertThat(3, equalTo(fetchedAirline.getFlights().size()));
    }

    @Test
    void test22POSTCheckIfTwoFlightWithSameSrcAndDestAreReturnedForPreviousAirline() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "Frontier";

        Airline fetchedAirline = client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "LAS", "MSP");
        assertThat(airlineName, equalTo(fetchedAirline.getName()));
        assertThat(2, equalTo(fetchedAirline.getFlights().size()));
    }

    @Test
    void test23POSTAirlinetWithNoDirectFlightsIssuesError() throws ParserException, IOException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "Frontier";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(airlineName, "PDX", "AVP"));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        assertThat(ex.getMessage(), containsString(Messages.NO_DIRECT_FLIGHTS));
    }
}
