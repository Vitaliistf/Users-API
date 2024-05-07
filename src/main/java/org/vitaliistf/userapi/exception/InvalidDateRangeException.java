package org.vitaliistf.userapi.exception;

/**
 * Exception indicating that an invalid date range was provided.
 */
public class InvalidDateRangeException extends RuntimeException {

    /**
     * Constructs a new InvalidDateRangeException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
