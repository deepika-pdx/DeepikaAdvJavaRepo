Assignment Name: CS510 Advance Java Project 4
Name: Deepika Parshvanath Velapure

Overview:
This is an 'Airline' project. It accepts details about an airline and its flight from the user via the command line and creates airline and flight objects.
An Airline has a unique name and can have multiple flights. Each flight has a unique identifying number, and it departs from a source location at a given
departure date and time, and arrives at a destination location at a given arrival date and time. The airline and the details about its flight can be
optionally saved in text or xml file as well.

To run the project type the following in the command line:
java -jar target/airline-2023.0.0.jar [options] <args>
The arguments and the order in which they should be passed are as below:
args are (in this order):
    airline           The name of the airline
    flightNumber      The flight number
    source            Three-letter code of the departure airport
    departure date    Departure date in mm/dd/yyyy format
    departure time    Departure time in 12-hour time format
    departure period  Departure time period i.e. AM/am or PM/pm
    destination       Three-letter code of the arrival airport
    arrival date      Arrival date in mm/dd/yyyy format
    arrival time      Arrival time in 12-hour time format
    arrival period    Arrival time period i.e. AM/am or PM/pm
options are (Options may appear in any order. Please do not provide '-xmlFile' and '-textFile' option together.):
    -xmlFile file     Xml file to read/write the airline info
    -textFile file    Text file to read/write the airline info
    -pretty file      Pretty print the airline’s flights to a text file or standard out. Provide this option as '-pretty file' or '-pretty -'
    -print            Prints the description of the newly added flight
    -README           Prints a README for this project and exits

In case no options are provided:    If no optional arguments are provided the project creates an airline and flight information using the data provided in the command line.

Behavior of the '-xmlFile' option:  The '-xmlFile' option allows a user to store the information about an airline and its flights in a specified xml file.
                                    There are no restrictions on the filename. Also, the file path provided can be an absolute or relative path
                                    or it can even be the current directory. If the file provided with the '-xmlFile' does not exist, then a
                                    new file is created to store the airline and flight information passed through the arguments. If the file provided
                                    with the '-xmlFile' does exist, then the airline and flight information is read from that file and a new flight
                                    created using the command line arguments is added to the existing flights and the updated list of flights is written
                                    back to the file along with airline name. The xml file specified should conform to the DTD at
                                    'http://www.cs.pdx.edu/˜whitlock/dtds/airline.dtd' else the project throws exception.

Behavior of the '-textFile' option: The '-textFile' option allows a user to store the information about an airline and its flights in a specified text file.
                                    There are no restrictions on the filename. Also, the file path provided can be an absolute or relative path or it can even
                                    be the current directory. If the file provided with the '-textFile' does not exist, then a new file is created to store the
                                    airline and flight information passed through the arguments. If the file provided with the '-textFile' does exist, then the
                                    airline and flight information is read from that file and a new flight created using the command line arguments is added to
                                    the existing flights and the updated list of flights is written back to the file along with airline name. The text in the
                                    file is formatted in a particular manner and the project does throw exceptions if a malformed file is provided in the input
                                    command line arguments.

Behavior of the '-pretty' option:   The '-pretty' option prints the information about an airline and its flights in an user-understandable simple text format.
                                    It accepts either a file name with path to which the airline and flight data can be pretty printed or a '-' symbol which
                                    indicates that the airline and flight data will be printed to the standard output terminal. There are no restrictions on
                                    the filename. Also, the file path provided can be an absolute or relative path or it can even be the current directory.
                                    If the file provided with the '-pretty' does not exist, then a new file is created to pretty print the airline and flight
                                    information passed through the arguments. If the file provided with the '-pretty' does exist, then it is overwritten with
                                    the airline and flight information. If '-textFile' option is provided along with it, then the flights read from the text
                                    file provided with the '-textFile' option along with the new flight details provided in the command line are pretty printed.

Behavior of the '-print' option:    The '-print' optional parameter provides the flight details such as flight number, source location, departure date and time,
                                    destination location and arrival date and time.


