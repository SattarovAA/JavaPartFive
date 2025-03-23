package org.jpf.exception;

/**
 * Exception to handle entity not found problem.
 */
public class EntityNotFoundException extends RuntimeException {
    /**
     * Create new Object with message.
     *
     * @param message exception message.
     * @see RuntimeException#RuntimeException(String)
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
