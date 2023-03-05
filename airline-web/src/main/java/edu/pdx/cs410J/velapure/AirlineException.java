package edu.pdx.cs410J.velapure;

/**
 * This is <code>AirlineException</code> class for Project.
 */
public class AirlineException extends RuntimeException {

    /**
     * Holds the error message of the exception.
     */
    private String message;

    /**
     * Creates a new <code>AirlineException</code>
     *
     * @param message
     *         Holds the error message of the exception.
     */
    AirlineException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * This method returns the error message.
     *
     * @return error message.
     */
    @Override
    public String toString() {
        return message;
    }
}
