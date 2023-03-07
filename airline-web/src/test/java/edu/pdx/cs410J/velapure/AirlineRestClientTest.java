package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A unit test for the REST client that demonstrates using mocks and
 * dependency injection
 */
public class AirlineRestClientTest {

    @Test
    void getAllFlightsOfTheSpecifiedPerformsHttpGetButReturnsErrorAsAirlineDoesNotExist() throws ParserException, IOException {
        String airlineName = "Frontier";
        Map<String, String> airlineMap = Map.of(AirlineServlet.AIRLINE_PARAMETER, "Frontier");

        HttpRequestHelper http = mock(HttpRequestHelper.class);
        HttpRequestHelper.Response response = mock(HttpRequestHelper.Response.class);

        when(response.getHttpStatusCode()).thenReturn(HttpServletResponse.SC_NOT_FOUND);
        when(http.get(eq(airlineMap))).thenReturn(response);

        AirlineRestClient client = new AirlineRestClient(http);

        HttpRequestHelper.RestException restException = assertThrows(HttpRequestHelper.RestException.class, () -> {
            client.getAllFlightsOfAnAirline(airlineName);
        });
    }
}
