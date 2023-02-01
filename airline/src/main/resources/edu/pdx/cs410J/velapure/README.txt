Assignment Name: CS510 Advance Java Project 1
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
    -print          Prints the description of the newly added flight
    -README         Prints a README for this project and exits
