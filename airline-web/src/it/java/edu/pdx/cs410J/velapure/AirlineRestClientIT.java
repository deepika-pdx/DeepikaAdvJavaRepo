package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
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
    void test0RemoveAllDictionaryEntries() throws IOException {
        AirlineRestClient client = newAirlineRestClient();
        client.removeAllAirlineEntries();
    }


    @Test
    void test1AddedOneFlight() throws IOException, ParserException {
        AirlineRestClient client = newAirlineRestClient();
        String airlineName = "IndiGo";
        client.addFlightToTheSpecifiedAirline(airlineName, "789", "PHL", "6/19/2023 12:59 AM", "MSP", "06/19/2023 4:00 AM");

        Airline fetchedAirline = client.getAllFlightsOfAnAirline(airlineName);
        assertThat(airlineName, equalTo(fetchedAirline.getName()));
    }

    @Test
    void test2EmptyAirlineThrowsException() {
        AirlineRestClient client = newAirlineRestClient();
        String emptyString = "";

        HttpRequestHelper.RestException ex =
                assertThrows(HttpRequestHelper.RestException.class, () -> client.getAllFlightsOfAnAirline(emptyString));
        assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
        assertThat(ex.getMessage(), equalTo(Messages.missingRequiredParameter(AirlineServlet.AIRLINE_PARAMETER)));
    }
}
