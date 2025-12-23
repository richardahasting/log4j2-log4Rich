package com.log4rich.log4j2.bridge;

import org.apache.logging.log4j.message.Message;
import java.util.function.Supplier;

/**
 * Extracts and formats messages from various log4j2 message types.
 * Handles: String, Message objects, Suppliers, and parameterized messages.
 */
public class MessageExtractor {
    
    /**
     * Central message extraction method - handles all log4j2 message types.
     */
    public static String extractMessage(Object messageObj, Object... params) {
        if (messageObj == null) {
            return "null";
        }
        
        if (messageObj instanceof String) {
            return formatParameterizedMessage((String) messageObj, params);
        } else if (messageObj instanceof Message) {
            return ((Message) messageObj).getFormattedMessage();
        } else if (messageObj instanceof Supplier) {
            try {
                Object supplied = ((Supplier<?>) messageObj).get();
                return supplied != null ? supplied.toString() : "null";
            } catch (Exception e) {
                return "Supplier threw exception: " + e.getMessage();
            }
        } else {
            // For any other object type, use toString()
            return messageObj.toString();
        }
    }
    
    /**
     * Formats parameterized messages using {} placeholders.
     * Example: "User {} has {} items" with ["john", 5] → "User john has 5 items"
     */
    private static String formatParameterizedMessage(String pattern, Object... params) {
        if (params == null || params.length == 0) {
            return pattern;
        }
        
        StringBuilder result = new StringBuilder();
        int paramIndex = 0;
        int patternIndex = 0;
        
        while (patternIndex < pattern.length()) {
            int placeholderIndex = pattern.indexOf("{}", patternIndex);
            
            if (placeholderIndex == -1) {
                // No more placeholders, append rest of pattern
                result.append(pattern.substring(patternIndex));
                break;
            }
            
            // Append text before placeholder
            result.append(pattern.substring(patternIndex, placeholderIndex));
            
            // Replace placeholder with parameter if available
            if (paramIndex < params.length) {
                Object param = params[paramIndex++];
                result.append(formatParameter(param));
            } else {
                // No more parameters, keep the placeholder
                result.append("{}");
            }
            
            patternIndex = placeholderIndex + 2; // Skip "{}"
        }
        
        return result.toString();
    }
    
    /**
     * Formats individual parameters, handling special cases.
     */
    private static String formatParameter(Object param) {
        if (param == null) {
            return "null";
        }
        
        if (param instanceof String) {
            return (String) param;
        }
        
        if (param instanceof Object[]) {
            // Handle array parameters
            Object[] array = (Object[]) param;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < array.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(formatParameter(array[i]));
            }
            sb.append("]");
            return sb.toString();
        }
        
        return param.toString();
    }
    
    /**
     * Optimization for single parameter case (very common).
     */
    public static String formatSingleParameter(String pattern, Object param) {
        if (param == null) {
            return pattern.replace("{}", "null");
        }
        return pattern.replace("{}", formatParameter(param));
    }
    
    /**
     * Optimization for two parameter case (also very common).
     */
    public static String formatTwoParameters(String pattern, Object param1, Object param2) {
        String result = pattern;
        
        int firstIndex = result.indexOf("{}");
        if (firstIndex != -1) {
            result = result.substring(0, firstIndex) + 
                    formatParameter(param1) + 
                    result.substring(firstIndex + 2);
            
            int secondIndex = result.indexOf("{}");
            if (secondIndex != -1) {
                result = result.substring(0, secondIndex) + 
                        formatParameter(param2) + 
                        result.substring(secondIndex + 2);
            }
        }
        
        return result;
    }
}