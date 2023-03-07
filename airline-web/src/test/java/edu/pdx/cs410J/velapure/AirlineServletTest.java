package edu.pdx.cs410J.velapure;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

    @Test
    void initiallyServletContainsNoAirlineEntries() throws IOException {
        AirlineServlet servlet = new AirlineServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter pw = mock(PrintWriter.class);
        String airlineName = "Frontier";
        when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn(airlineName);
        when(response.getWriter()).thenReturn(pw);

        servlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void addOneAirlineToAirlineFlightMap() throws IOException {
        AirlineServlet servlet = new AirlineServlet();

        String airlineName = "Frontier";
        String flightNumberString = "123";
        String srcAirport = "PDX";
        String destAirport = "LAS";
        String departure = "1/1/2023 10:59 AM";
        String arrival = "01/1/2023 2:00 PM";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(AirlineServlet.AIRLINE_PARAMETER)).thenReturn(airlineName);
        when(request.getParameter(AirlineServlet.FLIGHT_NO_PARAMETER)).thenReturn(flightNumberString);
        when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(srcAirport);
        when(request.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(destAirport);
        when(request.getParameter(AirlineServlet.DEPARTURE_PARAMETER)).thenReturn(departure);
        when(request.getParameter(AirlineServlet.ARRIVAL_PARAMETER)).thenReturn(arrival);

        HttpServletResponse response = mock(HttpServletResponse.class);

        // Use a StringWriter to gather the text from multiple calls to println()
        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter, true);

        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        assertThat(stringWriter.toString(), containsString("Flight 123 departs PDX at 1/1/2023 10:59 AM arrives LAS at 01/1/2023 2:00 PM"));

        // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
        ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCode.capture());

        assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));
        assertThat(servlet.getAirline(airlineName).getName(), equalTo(airlineName));
    }
}
