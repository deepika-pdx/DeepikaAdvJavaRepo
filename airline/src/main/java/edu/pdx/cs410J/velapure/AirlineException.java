package edu.pdx.cs410J.velapure;

public class AirlineException extends Exception {

    private String message;

    AirlineException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}