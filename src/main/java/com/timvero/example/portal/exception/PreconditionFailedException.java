package com.timvero.example.portal.exception;

/**
 * Exception thrown when a precondition for the operation is not met
 */

public class PreconditionFailedException extends RuntimeException {

    public PreconditionFailedException(String message) {
        super(message);
    }

    public PreconditionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreconditionFailedException(String message, Object... args) {
        super(String.format(message, args));
    }
}