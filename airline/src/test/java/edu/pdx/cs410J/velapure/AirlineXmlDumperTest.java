package edu.pdx.cs410J.velapure;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for the {@link AirlineXmlDumper} class.
 */
public class AirlineXmlDumperTest {

    /**
     * This unit test checks if the airline details are correctly written to a xml file.
     */
    @Test
    void airlineDetailsAreCorrectlyDumpedInXmlFormat() throws IOException, ParserConfigurationException, SAXException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        Flight flight = new Flight(789, "PDX", "01/25/2023 15:30", new Date(), "PHL", "01/26/2023 4:15", new Date());
        airline.addFlight(flight);

        File xmlFile = new File("src/test/resources/edu/pdx/cs410J/velapure/airline.xml");
        AirlineXmlDumper dumper = new AirlineXmlDumper(xmlFile);
        dumper.dump(airline);

        AirlineXmlHelper helper = new AirlineXmlHelper();

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        DocumentBuilder builder =
                factory.newDocumentBuilder();
        builder.setErrorHandler(helper);
        builder.setEntityResolver(helper);

        assertDoesNotThrow(() -> {
            builder.parse(this.getClass().getResourceAsStream("airline.xml"));
        });
    }
}
