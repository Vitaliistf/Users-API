package org.vitaliistf.userapi.exception;

/**
 * Exception indicating that a requested resource was not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message The detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
