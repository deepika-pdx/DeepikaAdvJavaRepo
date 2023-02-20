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

public class AirlineXmlDumper extends AirlineXmlHelper implements AirlineDumper<Airline> {

    private static PrintWriter err;
    private static PrintWriter pw;

    public AirlineXmlDumper(String fileName) throws IOException {
        this(new File(fileName));
    }

    public AirlineXmlDumper(File file) throws IOException {
        this(new PrintWriter(new FileWriter(file), true));
    }

    public AirlineXmlDumper(PrintWriter pw) {
        this.pw = pw;
    }

    static {
        err = new PrintWriter(System.err, true);
    }

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

                Element arrive = createDateElement(doc, flight.getDeparture(), "arrive");
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
    }

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
