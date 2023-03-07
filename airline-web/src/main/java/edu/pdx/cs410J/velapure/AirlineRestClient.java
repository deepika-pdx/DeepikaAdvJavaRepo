package edu.pdx.cs410J.velapure;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.
 */
public class AirlineRestClient {
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     *
     * @param hostName
     *         The name of the host
     * @param port
     *         The port
     */
    public AirlineRestClient(String hostName, int port) {
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
        this.http = http;
    }

    /**
     * Returns all flights of a specified airline.
     */
    public Airline getAllFlightsOfAnAirline(String airlineName) throws IOException, ParserException {
        Response response = http.get(Map.of(AirlineServlet.AIRLINE_PARAMETER, airlineName));
        throwExceptionIfNotOkayHttpStatus(response);
        String content = response.getContent();

        AirlineXmlParser xmlParser = new AirlineXmlParser(new StringReader(response.getContent()));
        return xmlParser.parse();
    }

    /**
     * Returns only those flights of an airline with the specified src and dest airport.
     */
    public Airline getFlightsWithSpecifiedSrcAndDestAirportOfAnAirline(String airlineName, String srcAirportCode, String destAirportCode) throws IOException, ParserException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(AirlineServlet.AIRLINE_PARAMETER, airlineName);
        paramMap.put(AirlineServlet.SRC_PARAMETER, srcAirportCode);
        paramMap.put(AirlineServlet.DEST_PARAMETER, destAirportCode);
        Response response = http.get(paramMap);
        throwExceptionIfNotOkayHttpStatus(response);
        String content = response.getContent();

        AirlineXmlParser xmlParser = new AirlineXmlParser(new StringReader(response.getContent()));
        return xmlParser.parse();
    }

    public String addFlightToTheSpecifiedAirline(String airlineName, String flightNumberString, String srcAirport, String departure,
                                                 String destAirport, String arrival) throws IOException {
        Response response = http.post(Map.of(AirlineServlet.AIRLINE_PARAMETER, airlineName, AirlineServlet.FLIGHT_NO_PARAMETER, flightNumberString,
                AirlineServlet.SRC_PARAMETER, srcAirport, AirlineServlet.DEPARTURE_PARAMETER, departure,
                AirlineServlet.DEST_PARAMETER, destAirport, AirlineServlet.ARRIVAL_PARAMETER, arrival));
        throwExceptionIfNotOkayHttpStatus(response);
        return response.getContent();
    }

    public void removeAllAirlineEntries() throws IOException {
        Response response = http.delete(Map.of());
        throwExceptionIfNotOkayHttpStatus(response);
    }

    private void throwExceptionIfNotOkayHttpStatus(Response response) {
        int code = response.getHttpStatusCode();
        if (code != HTTP_OK) {
            String message = response.getContent();
            throw new RestException(code, message);
        }
    }
}
