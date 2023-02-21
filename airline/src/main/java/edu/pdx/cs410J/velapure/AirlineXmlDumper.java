package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

/**
 * This is <code>AirlineXmlDumper</code> class for Project.
 */
public class AirlineXmlDumper extends AirlineXmlHelper implements AirlineDumper<Airline> {

    /**
     * PrintWriter for writing error messages.
     */
    private static PrintWriter err;

    /**
     * PrintWriter for writing airline and flight data to the file.
     */
    private static PrintWriter pw;

    /**
     * Creates a new <code>AirlineXmlDumper</code>
     *
     * @param fileName
     *         filename for writing airline and flight data.
     */
    public AirlineXmlDumper(String fileName) throws IOException {
        this(new File(fileName));
    }

    /**
     * Creates a new <code>AirlineXmlDumper</code>
     *
     * @param file
     *         file for writing airline and flight data.
     */
    public AirlineXmlDumper(File file) throws IOException {
        this(new PrintWriter(new FileWriter(file), true));
    }

    /**
     * Creates a new <code>AirlineXmlDumper</code>
     *
     * @param pw
     *         PrintWriter obj for writing airline and flight data to the file.
     */
    public AirlineXmlDumper(PrintWriter pw) {
        this.pw = pw;
    }

    static {
        err = new PrintWriter(System.err, true);
    }

    /**
     * This method writes the airline and flight data to a file.
     *
     * @param airline
     *         An airline object having flight details.
     */
    @Override
    public void dump(Airline airline) {
        Document doc = null;
        String errorMessage;

        // Creating the document object
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(this);
            builder.setEntityResolver(this);
            DOMImplementation dom = builder.getDOMImplementation();
            DocumentType documentType = dom.createDocumentType("airline", AirlineXmlHelper.PUBLIC_ID, AirlineXmlHelper.SYSTEM_ID);
            doc = dom.createDocument(null, "airline", documentType);
        } catch (ParserConfigurationException e) {
            errorMessage = "Illconfigured XML Parser!";
            throw new AirlineException(errorMessage);
        } catch (DOMException e) {
            errorMessage = "Error while creating XML Document";
            throw new AirlineException(errorMessage);
        }

        // Building the DOM tree
        try {
            Element airlineElement = doc.getDocumentElement();

            Element airlineName = doc.createElement("name");
            airlineName.appendChild(doc.createTextNode(airline.getName()));
            airlineElement.appendChild(airlineName);

            for (Flight flight : airline.getFlights()) {
                Element flightElement = doc.createElement("flight");

                Element flightNumber = doc.createElement("number");
                flightNumber.appendChild(doc.createTextNode(String.valueOf(flight.getNumber())));
                flightElement.appendChild(flightNumber);

                Element src = doc.createElement("src");
                src.appendChild(doc.createTextNode(flight.getSource()));
                flightElement.appendChild(src);

                Element depart = createDateElement(doc, flight.getDeparture(), "depart");
                flightElement.appendChild(depart);

                Element dest = doc.createElement("dest");
                dest.appendChild(doc.createTextNode(flight.getDestination()));
                flightElement.appendChild(dest);

                Element arrive = createDateElement(doc, flight.getArrival(), "arrive");
                flightElement.appendChild(arrive);

                airlineElement.appendChild(flightElement);
            }
        } catch (DOMException e) {
            errorMessage = "Exception while building DOM tree";
            throw new AirlineException(errorMessage);
        }

        // Transforming the DOM tree into XML
        try {
            Source source = new DOMSource(doc);
            Result result = new StreamResult(this.pw);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("doctype-system", AirlineXmlHelper.SYSTEM_ID);
            transformer.setOutputProperty("doctype-public", AirlineXmlHelper.PUBLIC_ID);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty("encoding", "ASCII");
            transformer.transform(source, result);
        } catch (TransformerException e) {
            errorMessage = "Error while transforming XML";
            throw new AirlineException(errorMessage);
        }
        this.pw.flush();
        this.pw.close();
    }

    /**
     * This method creates date element using flight departure/arrival date.
     *
     * @param doc
     *         Document obj for holding airline and flight details.
     * @param flightDate
     *         flight departure/arrival date.
     * @param dateType
     *         String holding 'depart' or 'arrive'.
     */
    private static Element createDateElement(Document doc, Date flightDate, String dateType) {
        Element flightDateElement = doc.createElement(dateType);

        Element dateElement = doc.createElement("date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(flightDate);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        dateElement.setAttribute("day", String.valueOf(day));
        dateElement.setAttribute("month", String.valueOf(month));
        dateElement.setAttribute("year", String.valueOf(year));

        flightDateElement.appendChild(dateElement);

        Element timeElement = doc.createElement("time");

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timeElement.setAttribute("hour", String.valueOf(hour));
        timeElement.setAttribute("minute", String.valueOf(minute));

        flightDateElement.appendChild(timeElement);
        return flightDateElement;
    }
}
