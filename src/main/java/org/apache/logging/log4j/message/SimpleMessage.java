package org.apache.logging.log4j.message;

/**
 * Simple string-based message implementation.
 */
public class SimpleMessage implements Message {
    
    private final String message;
    
    public SimpleMessage(String message) {
        this.message = message != null ? message : "";
    }
    
    @Override
    public String getFormattedMessage() {
        return message;
    }
    
    @Override
    public String getFormat() {
        return message;
    }
    
    @Override
    public Object[] getParameters() {
        return new Object[0];
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    @Override
    public String toString() {
        return message;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SimpleMessage that = (SimpleMessage) obj;
        return message.equals(that.message);
    }
    
    @Override
    public int hashCode() {
        return message.hashCode();
    }
}