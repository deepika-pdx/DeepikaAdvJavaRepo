Assignment Name: CS510 Advance Java Project 5
Name: Deepika Parshvanath Velapure

Overview:
This is an 'Airline Web Application' project. It accepts details about an airline and its flight from the user via the command line or browser and
sends HTTP requests to the server for creating and fetching airline and flight details.An Airline has a unique name and can have multiple flights. Each flight
has a unique identifying number, and it departs from a source location at a given departure date and time, and arrives at a destination location
at a given arrival date and time. The airline and flight details are erased upon every server restart.

It supports below urls for creating and fetching airline and flight details:
GET "http://host:port/airline/flights?airline=name"                             - Fetches all the flights of the specified airline.
POST "http://host:port/airline/flights?airline=name"                            - Creates flight and/or airline with specifed flight and airline details.
                                                                                  It creates an airline object if the specified airline does not exist.
GET "http://host:port/airline/flights?airline=name&src=airport&dest=airport"    - Fetches only those flights of the airline with specified source and
                                                                                  destination airport codes

To access the REST from the command line:
java -jar target/airline-client.jar [options] <args>
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
options are (Options may appear in any order. Please do not provide '-search' and '-print' option together.):
    -host hostname    Host computer on which the server runs
    -port portNumber  Port on which the server is listening
    -search           Search for flights. Airline name is required with the '-search' option. The source and destination airport codes can be
                      optionally provided with the '-search' option.
    -print            Prints the description of the newly added flight
    -README           Prints a README for this project and exits

In case no options are provided:    If no optional arguments are provided the project creates an airline and flight information using the data
                                    provided in the command line.

Behavior of the '-search' option:   The '-search' option prints the information about the flights of the specified airline in a user-understandable
                                    simple text format. The airline name is required with the '-search' option. However, the source and destination
                                    airport codes can be optionally provided with the '-search' option. An error is issued if only either of the source
                                    or destination airport code is provided.

Behavior of the '-print' option:    The '-print' optional parameter provides the details of the newly added flight such as flight number, source location,
                                    departure date and time, destination location and arrival date and time.


