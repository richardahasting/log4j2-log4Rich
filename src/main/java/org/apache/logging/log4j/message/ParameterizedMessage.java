package org.apache.logging.log4j.message;

/**
 * Parameterized message implementation using {} placeholders.
 */
public class ParameterizedMessage implements Message {
    
    private final String messagePattern;
    private final Object[] parameters;
    private final Throwable throwable;
    private volatile String formattedMessage;
    
    public ParameterizedMessage(String messagePattern, Object... parameters) {
        this.messagePattern = messagePattern != null ? messagePattern : "";
        
        // Extract throwable if it's the last parameter
        if (parameters != null && parameters.length > 0 && 
            parameters[parameters.length - 1] instanceof Throwable) {
            
            this.throwable = (Throwable) parameters[parameters.length - 1];
            this.parameters = new Object[parameters.length - 1];
            System.arraycopy(parameters, 0, this.parameters, 0, this.parameters.length);
        } else {
            this.throwable = null;
            this.parameters = parameters != null ? parameters.clone() : new Object[0];
        }
    }
    
    @Override
    public String getFormattedMessage() {
        if (formattedMessage == null) {
            formattedMessage = formatMessage(messagePattern, parameters);
        }
        return formattedMessage;
    }
    
    @Override
    public String getFormat() {
        return messagePattern;
    }
    
    @Override
    public Object[] getParameters() {
        return parameters.clone();
    }
    
    @Override
    public Throwable getThrowable() {
        return throwable;
    }
    
    private static String formatMessage(String pattern, Object[] params) {
        if (params == null || params.length == 0) {
            return pattern;
        }
        
        StringBuilder result = new StringBuilder();
        int paramIndex = 0;
        int patternIndex = 0;
        
        while (patternIndex < pattern.length()) {
            int placeholderIndex = pattern.indexOf("{}", patternIndex);
            
            if (placeholderIndex == -1) {
                result.append(pattern.substring(patternIndex));
                break;
            }
            
            result.append(pattern.substring(patternIndex, placeholderIndex));
            
            if (paramIndex < params.length) {
                Object param = params[paramIndex++];
                result.append(param != null ? param.toString() : "null");
            } else {
                result.append("{}");
            }
            
            patternIndex = placeholderIndex + 2;
        }
        
        return result.toString();
    }
    
    @Override
    public String toString() {
        return getFormattedMessage();
    }
}