package org.apache.logging.log4j.message;

/**
 * Base interface for all log4j2 messages.
 * Provides formatted message content and parameter access.
 */
public interface Message {
    
    /**
     * Gets the formatted message.
     */
    String getFormattedMessage();
    
    /**
     * Gets the message format (pattern).
     */
    String getFormat();
    
    /**
     * Gets the message parameters.
     */
    Object[] getParameters();
    
    /**
     * Gets any throwable associated with the message.
     */
    Throwable getThrowable();
}

