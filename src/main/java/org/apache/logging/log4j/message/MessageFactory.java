package org.apache.logging.log4j.message;

/**
 * Factory for creating log4j2 messages.
 * Provides methods to create different types of messages.
 */
public interface MessageFactory {
    
    /**
     * Creates a new message from an object.
     */
    Message newMessage(Object message);
    
    /**
     * Creates a new parameterized message.
     */
    Message newMessage(String message, Object... params);
}

/**
 * Parameterized message factory implementation.
 * Always creates ParameterizedMessage instances.
 */
class ParameterizedMessageFactory implements MessageFactory {
    
    public static final MessageFactory INSTANCE = new ParameterizedMessageFactory();
    
    @Override
    public Message newMessage(Object message) {
        if (message instanceof String) {
            return new ParameterizedMessage((String) message);
        }
        
        if (message instanceof Message) {
            return (Message) message;
        }
        
        return new ObjectMessage(message);
    }
    
    @Override
    public Message newMessage(String message, Object... params) {
        return new ParameterizedMessage(message, params);
    }
}