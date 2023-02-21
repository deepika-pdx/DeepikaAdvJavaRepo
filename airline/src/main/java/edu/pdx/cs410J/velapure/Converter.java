package edu.pdx.cs410J.velapure;

import edu.pdx.cs410J.ParserException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Converter {

    /**
     * Usage information of this class.
     */
    private static final String USAGE = "usage: java -cp target/airline-2023.0.0.jar edu.pdx.cs410J.<your-login-id>.Converter <args>" + "\n" +
            "args are (in this order):" + "\n" +
            "textFile" + "   " + "The name of the text file to read the airline info." + "\n" +
            "xmlFile" + "   " + "The name of the xml file to write the airline info read from the text file.";

    /**
     * This is the main method of the Converter class which converts the representation of the Airline in a text file to an XML file.
     *
     * @param args
     *         Arguments:'textFile', 'xmlFile'
     */
    public static void main(String[] args) {
        if (args != null) {
            try {
                int argLength = args.length;
                // Validate command line arguments
                if (argLength == 0) {
                    throw new AirlineException("Missing command line arguments. " + "\n" + USAGE);
                } else if (argLength < 2) {
                    throw new AirlineException("Too few command line arguments. " + "\n" + USAGE);
                } else if (argLength > 2) {
                    throw new AirlineException("Too many command line arguments. " + "\n" + USAGE);
                }

                // Extract command line arguments
                String textFilename = args[0];
                String xmlFilename = args[1];

                // Validate if file names with correct extension are provided as input
                if (!textFilename.contains(".txt")) {
                    throw new AirlineException("Please provide a file with '.txt' extension as the first argument. " + "\n" + USAGE);
                }
                if (!xmlFilename.contains(".xml")) {
                    throw new AirlineException("Please provide a file with '.xml' extension as the second argument. " + "\n" + USAGE);
                }

                // Convert the airline information from text to xml file
                try {
                    // Read the airline data from the text file
                    AirlineTextParser airlineTextParser = new AirlineTextParser(new FileReader(textFilename));
                    Airline readAirline = airlineTextParser.parse();

                    // Write the airline data to the xml file
                    AirlineXmlDumper airlineXmlDumper = new AirlineXmlDumper(xmlFilename);
                    airlineXmlDumper.dump(readAirline);
                } catch (FileNotFoundException e) {
                    throw new AirlineException("The text file specified in the command line argument is not found!");
                } catch (ParserException e) {
                    throw new AirlineException("Invalid text file. Unable to parse the text file specified in the command line argument.");
                } catch (IOException e) {
                    throw new AirlineException("Unable to write to the xml file specified in the command line argument.");
                }
            } catch (AirlineException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
