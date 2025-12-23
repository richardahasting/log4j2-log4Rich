package org.apache.logging.log4j;

/**
 * Exception thrown when an error occurs during logging operations.
 */
public class LoggingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LoggingException() {
        super();
    }

    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggingException(Throwable cause) {
        super(cause);
    }
}
