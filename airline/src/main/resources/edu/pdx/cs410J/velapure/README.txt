Assignment Name: CS510 Advance Java Project 2
Name: Deepika Parshvanath Velapure

Overview:
This is an 'Airline' project. It accepts details about an airline and its flight from the user via the command line and creates airline and flight objects.
An Airline has a unique name and can have multiple flights. Each flight has a unique identifying number, and it departs from a source location at a
given departure time, and arrives at a destination location at a given arrival time.

To run the project type the following in the command line:
java -jar target/airline-2023.0.0.jar [options] <args>
The arguments and the order in which they should be passed are as below:
args are (in this order):
    airline         The name of the airline
    flightNumber    The flight number
    source          Three-letter code of the departure airport
    departure date  Departure date in mm/dd/yyyy format
    departure time  Departure time in 24-hour time format
    destination     Three-letter code of the arrival airport
    arrival date    Arrival date in mm/dd/yyyy format
    arrival time    Arrival time in 24-hour time format
options are (options may appear in any order):
    -textFile file  File to read/write the airline info
    -print          Prints the description of the newly added flight
    -README         Prints a README for this project and exits

If no optional arguments are provided the project creates an airline and flight information using the data provided in the command line.
The '-print' optional parameter provides the flight details such as flight number, source location, departure date and time,
destination location and arrival date and time. The '-textFile' option allows a user to store the information about an airline and its flights.
There are no restrictions on the filename. Also, the file path provided can be an absolute or relative path or it can even be the current directory.
If the file provided with the '-textFile' does not exist, then a new file is created to store the airline and flight information passed
through the arguments.
If the file provided with the '-textFile' does exist, then the airline and flight information is read from that file and a new flight created using the
command line arguments is written back to the file.
The text in the file is formatted in a particular manner and the project does throw exceptions if a malformed file is provided in the input
command line arguments.
