package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * This is <code>AirlineXmlParser</code> class for Project.
 */
public class AirlineXmlParser extends AirlineXmlHelper implements AirlineParser<Airline> {

    /**
     * Airline object read from the xml file.
     */
    private Airline readAirline;

    /**
     * Reader for reading airline and flight data from xml file.
     */
    private Reader reader;

    /**
     * Creates a new <code>AirlineXmlParser</code>
     *
     * @param fileName
     *         filename for reading airline and flight data.
     */
    public AirlineXmlParser(String fileName) throws FileNotFoundException {
        this(new File(fileName));
    }

    /**
     * Creates a new <code>AirlineXmlParser</code>
     *
     * @param file
     *         File for reading airline and flight data.
     */
    public AirlineXmlParser(File file) throws FileNotFoundException {
        this((Reader) (new FileReader(file)));
    }

    /**
     * Creates a new <code>AirlineXmlParser</code>
     *
     * @param reader
     *         Reader obj for reading airline and flight data from xml file.
     */
    public AirlineXmlParser(Reader reader) {
        this.reader = reader;
    }

    /**
     * This method reads the airline and flight data from a file.
     *
     * @return An airline object having flight details.
     */
    @Override
    public Airline parse() throws ParserException {
        try {
            Document doc = null;
            String errorMessage;

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(true);
                factory.setIgnoringComments(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setErrorHandler(this);
                builder.setEntityResolver(this);
                doc = builder.parse(new InputSource(this.reader));
            } catch (ParserConfigurationException e) {
                errorMessage = "Error while parsing the XML file." + " " + e.getMessage();
                throw new AirlineException(errorMessage);
            } catch (IOException e) {
                errorMessage = "Error while parsing the XML file." + " " + e.getMessage();
                throw new AirlineException(errorMessage);
            } catch (SAXException e) {
                errorMessage = "Error while parsing the XML file as it does not conform to the airline DTD." + " " + e.getMessage();
                throw new AirlineException(errorMessage);
            }

            Node n = doc.getChildNodes().item(1);
            Element root = (Element) doc.getChildNodes().item(1);
            String rootNodeName = root.getNodeName();
            if (!rootNodeName.equals("airline")) {
                errorMessage = "Not an Airline XML file!! Root element found: " + rootNodeName;
                throw new AirlineException(errorMessage);
            } else {
                NodeList airlineNodes = root.getChildNodes();

                for (int i = 1; i <= airlineNodes.getLength(); i++) {
                    Node node = airlineNodes.item(i);
                    if (node instanceof Element) {
                        Element airlineElement = (Element) node;
                        String airlineNodeName = airlineElement.getNodeName();
                        switch (airlineNodeName) {
                            case "name":
                                this.readAirline = new Airline(airlineElement.getFirstChild().getNodeValue());
                                break;
                            case "flight":
                                readFlightData(airlineElement);
                                break;
                        }
                    }
                }
            }
            return this.readAirline;
        } catch (AirlineException e) {
            throw new ParserException(e.getMessage());
        }
    }

    /**
     * This method reads the flight data from a document obj.
     *
     * @param airlineElement
     *         Element holding flight nodes of an airline
     */
    private void readFlightData(Element airlineElement) throws ParserException {
        int flightNumber = 0;
        String source = null;
        String depatureDateTime = null;
        Date departureDate = null;
        String destination = null;
        String arrivalDateTime = null;
        Date arrivalDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        try {
            NodeList flightElements = airlineElement.getChildNodes();
            for (int j = 1; j <= flightElements.getLength(); j++) {
                Node flightNode = flightElements.item(j);
                if (flightNode instanceof Element) {
                    Element flightElement = (Element) flightNode;
                    String flightElementName = flightElement.getNodeName();
                    switch (flightElementName) {
                        case "number":
                            try {
                                flightNumber = Integer.parseInt(flightElement.getFirstChild().getNodeValue());
                            } catch (NumberFormatException e) {
                                throw new AirlineException("Malformed xml file. Unable to parse flight number.");
                            }
                            break;
                        case "src":
                            source = flightElement.getFirstChild().getNodeValue();
                            break;
                        case "depart":
                            departureDate = createDateFromXmlFile(flightElement);
                            depatureDateTime = sdf.format(departureDate);
                            break;
                        case "dest":
                            destination = flightElement.getFirstChild().getNodeValue();
                            break;
                        case "arrive":
                            arrivalDate = createDateFromXmlFile(flightElement);
                            arrivalDateTime = sdf.format(arrivalDate);
                            break;
                    }
                }
            }
            validateSourceAndDestParameters(source, destination);
            validateDepartureAndArrivalDates(departureDate, arrivalDate);
            Flight readFlight = new Flight(flightNumber, source, depatureDateTime, departureDate, destination, arrivalDateTime, arrivalDate);
            this.readAirline.addFlight(readFlight);
        } catch (AirlineException e) {
            throw new ParserException(e.getMessage());
        }
    }

    /**
     * This method reads and creates the flight date details.
     *
     * @param flightDateElement
     *         Element holding flight departure/arrival date and time
     */
    private Date createDateFromXmlFile(Element flightDateElement) throws ParserException {
        Calendar calendar = Calendar.getInstance();
        NodeList flightDateElements = flightDateElement.getChildNodes();
        for (int k = 1; k <= flightDateElements.getLength(); k++) {
            Node dateNode = flightDateElements.item(k);
            if (dateNode instanceof Element) {
                Element dateElement = (Element) dateNode;
                String dateElementName = dateElement.getNodeName();
                switch (dateElementName) {
                    case "date":
                        NamedNodeMap dateAttributes = dateElement.getAttributes();
                        for (int h = 0; h < dateAttributes.getLength(); h++) {
                            Node dateAttribute = dateAttributes.item(h);
                            String dateAttributeName = dateAttribute.getNodeName();
                            switch (dateAttributeName) {
                                case "day":
                                    int day = 0;
                                    try {
                                        day = Integer.parseInt(dateAttribute.getNodeValue());
                                    } catch (NumberFormatException e) {
                                        throw new AirlineException("Malformed xml file. Unable to parse day of the given date.");
                                    }
                                    calendar.set(Calendar.DAY_OF_MONTH, day);
                                    break;
                                case "month":
                                    int month = 0;
                                    try {
                                        month = Integer.parseInt(dateAttribute.getNodeValue());
                                    } catch (NumberFormatException e) {
                                        throw new AirlineException("Malformed xml file. Unable to parse month of the given date.");
                                    }
                                    calendar.set(Calendar.MONTH, month);
                                    break;
                                case "year":
                                    int year = 0;
                                    try {
                                        year = Integer.parseInt(dateAttribute.getNodeValue());
                                    } catch (NumberFormatException e) {
                                        throw new AirlineException("Malformed xml file. Unable to parse year of the given date.");
                                    }
                                    calendar.set(Calendar.YEAR, year);
                                    break;
                                default:
                                    throw new AirlineException("Invalid element in date: " + dateAttributeName);
                            }

                        }
                        break;
                    case "time":
                        NamedNodeMap timeAttributes = dateElement.getAttributes();
                        for (int h = 0; h < timeAttributes.getLength(); h++) {
                            Node timeAttribute = timeAttributes.item(h);
                            String timeAttributeName = timeAttribute.getNodeName();
                            switch (timeAttributeName) {
                                case "hour":
                                    int hour = 0;
                                    try {
                                        hour = Integer.parseInt(timeAttribute.getNodeValue());
                                    } catch (NumberFormatException e) {
                                        throw new AirlineException("Malformed xml file. Unable to parse given time.");
                                    }
                                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                                    break;
                                case "minute":
                                    int mins = 0;
                                    try {
                                        mins = Integer.parseInt(timeAttribute.getNodeValue());
                                    } catch (NumberFormatException e) {
                                        throw new AirlineException("Malformed xml file. Unable to parse given time.");
                                    }
                                    calendar.set(Calendar.MINUTE, mins);
                                    break;
                                default:
                                    throw new AirlineException("Invalid element in time: " + timeAttributeName);
                            }
                        }
                        break;
                }
            }
        }
        return calendar.getTime();
    }

    /**
     * This method validates the source and destination location of the flight read from the xml file.
     *
     * @param srcLocation
     *         Flight's source location.
     * @param destLocation
     *         Flight's destination location.
     */
    private void validateSourceAndDestParameters(String srcLocation, String destLocation) throws AirlineException {

        // Validation of the provided source location
        if (!(Pattern.matches("[a-zA-Z]+", srcLocation)) || srcLocation.length() != 3) {
            throw new AirlineException("Invalid source location. Unable to parse the provided xml file.");
        } else if (AirportNames.getName(srcLocation.toUpperCase()) == null) {
            throw new AirlineException("The three-letter source airport code in the input xml file does not correspond to a known airport!");
        } else {
            srcLocation = srcLocation.toUpperCase();
        }


        // Validation of the provided destination location
        if (!(Pattern.matches("[a-zA-Z]+", destLocation)) || destLocation.length() != 3) {
            throw new AirlineException("Invalid destination location. Unable to parse the provided xml file.");
        } else if (AirportNames.getName(destLocation.toUpperCase()) == null) {
            throw new AirlineException("The three-letter destination airport code in the input xml file does not correspond to a known airport!");
        } else {
            destLocation = destLocation.toUpperCase();
        }

        if (srcLocation.equals(destLocation)) {
            throw new AirlineException("The source and the destination airport codes in the input xml file should not be same.");
        }
    }

    /**
     * This method validates the departure and arrival date and time of the flight read from the xml file.
     *
     * @param departureDate
     *         Flight's departure date.
     * @param arrivalDate
     *         Flight's arrival date.
     */
    private void validateDepartureAndArrivalDates(Date departureDate, Date arrivalDate) throws AirlineException {
        if (arrivalDate.before(departureDate) || arrivalDate.equals(departureDate)) {
            throw new AirlineException("Invalid arrival and departure date present in the xml file. The arrival date and time should not be before or same as the departure date and time.");
        }
    }
}
