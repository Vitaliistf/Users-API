package org.vitaliistf.userapi.exception;

/**
 * Exception indicating that a phone number already exists in the system.
 */
public class PhoneNumberAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new PhoneNumberAlreadyExistsException with the specified detail message.
     *
     * @param message The detail message.
     */
    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }
}

