package org.apache.logging.log4j.message;

/**
 * Object-based message implementation.
 */
public class ObjectMessage implements Message {
    
    private final Object object;
    
    public ObjectMessage(Object object) {
        this.object = object;
    }
    
    @Override
    public String getFormattedMessage() {
        return object != null ? object.toString() : "null";
    }
    
    @Override
    public String getFormat() {
        return getFormattedMessage();
    }
    
    @Override
    public Object[] getParameters() {
        return new Object[] { object };
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    @Override
    public String toString() {
        return getFormattedMessage();
    }
}