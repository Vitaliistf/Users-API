package org.vitaliistf.userapi.exception;

/**
 * Exception indicating that an email already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new EmailAlreadyExistsException with the specified detail message.
     *
     * @param message The detail message.
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
