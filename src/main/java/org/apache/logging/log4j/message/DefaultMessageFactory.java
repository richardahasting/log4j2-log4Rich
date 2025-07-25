package org.apache.logging.log4j.message;

/**
 * Default message factory implementation.
 * Creates appropriate message types based on input.
 */
public class DefaultMessageFactory implements MessageFactory {
    
    public static final MessageFactory INSTANCE = new DefaultMessageFactory();
    
    @Override
    public Message newMessage(Object message) {
        if (message == null) {
            return new SimpleMessage("null");
        }
        
        if (message instanceof String) {
            return new SimpleMessage((String) message);
        }
        
        if (message instanceof Message) {
            return (Message) message;
        }
        
        return new ObjectMessage(message);
    }
    
    @Override
    public Message newMessage(String message, Object... params) {
        if (params == null || params.length == 0) {
            return new SimpleMessage(message);
        }
        
        return new ParameterizedMessage(message, params);
    }
}