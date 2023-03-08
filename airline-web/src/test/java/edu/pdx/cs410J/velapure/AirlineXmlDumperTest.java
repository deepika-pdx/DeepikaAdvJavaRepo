package edu.pdx.cs410J.velapure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for the {@link AirlineWebXmlDumper} class.
 */
public class AirlineXmlDumperTest {

    /**
     * This unit test checks if the airline details are correctly written to a xml file.
     *
     * @param tempDir
     *         A temporary directory to store the files created during testing.
     */
    @Test
    void airlineDetailsAreCorrectlyDumpedInXmlFormat(@TempDir File tempDir) throws IOException, ParserConfigurationException, SAXException, ParseException {
        String airlineName = "Test Airline";
        Airline airline = new Airline(airlineName);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date departureDate = sdf.parse("01/25/2023 15:30");
        Date arrivalDate = sdf.parse("01/26/2023 4:15");


        Flight flight = new Flight(789, "PDX", "01/25/2023 15:30", departureDate, "PHL", "01/26/2023 4:15", arrivalDate);
        airline.addFlight(flight);

        File xmlFile = new File(tempDir + "airline.xml");
        AirlineWebXmlDumper dumper = new AirlineWebXmlDumper(xmlFile);
        dumper.dump(airline, Optional.empty(), Optional.empty());

        AirlineXmlHelper helper = new AirlineXmlHelper();

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        DocumentBuilder builder =
                factory.newDocumentBuilder();
        builder.setErrorHandler(helper);
        builder.setEntityResolver(helper);

        assertDoesNotThrow(() -> {
            builder.parse(xmlFile);
        });
    }
}
