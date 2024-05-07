package org.vitaliistf.userapi.exception;

/**
 * Exception indicating that an invalid age was provided.
 */
public class InvalidAgeException extends RuntimeException {

    /**
     * Constructs a new InvalidAgeException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidAgeException(String message) {
        super(message);
    }
}
