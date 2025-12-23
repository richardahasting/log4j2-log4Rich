package com.log4rich.log4j2.bridge;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;

/**
 * Central logging engine - ALL log4j2 logging methods funnel through here.
 * This is the single point of truth for logging logic, ensuring consistency
 * and making debugging/maintenance much easier.
 */
public class LoggingEngine {
    
    /**
     * THE central logging method - all 180+ log4j2 methods funnel through here.
     * This method handles the complete logging pipeline:
     * 1. Level checking (performance optimization)
     * 2. Marker filtering
     * 3. Message extraction and formatting
     * 4. Context application
     * 5. Final log4Rich delegation
     */
    public static void log(com.log4rich.core.Logger log4RichLogger, 
                          Level level, 
                          Marker marker, 
                          Object message, 
                          Throwable throwable, 
                          Object... params) {
        
        // Step 1: Early return if logging is disabled (critical for performance)
        com.log4rich.core.LogLevel richLevel = LevelTranslator.translate(level);
        if (!log4RichLogger.isLevelEnabled(richLevel)) {
            return;
        }
        
        // Step 2: Check marker-based filtering
        if (!MarkerHandler.shouldLog(marker, level)) {
            return;
        }
        
        // Step 3: Extract and format the final message
        String finalMessage = MessageExtractor.extractMessage(message, params);
        
        // Step 4: Add marker information if present
        if (marker != null) {
            String markerPrefix = MarkerHandler.formatMarker(marker);
            if (markerPrefix != null) {
                finalMessage = markerPrefix + finalMessage;
            }
        }
        
        // Step 5: Apply thread context
        ContextBridge.applyThreadContext();
        
        // Step 6: Log to log4Rich (single delegation point)
        if (throwable != null) {
            log4RichLogger.log(richLevel, finalMessage, throwable);
        } else {
            log4RichLogger.log(richLevel, finalMessage);
        }
    }
    
    /**
     * Optimized version for simple string messages (most common case).
     * Bypasses parameter formatting when no parameters are present.
     */
    public static void logSimple(com.log4rich.core.Logger log4RichLogger,
                                Level level,
                                String message) {
        
        com.log4rich.core.LogLevel richLevel = LevelTranslator.translate(level);
        if (!log4RichLogger.isLevelEnabled(richLevel)) {
            return;
        }
        
        ContextBridge.applyThreadContext();
        log4RichLogger.log(richLevel, message);
    }
    
    /**
     * Optimized version for single parameter messages (very common case).
     */
    public static void logSingleParam(com.log4rich.core.Logger log4RichLogger,
                                     Level level,
                                     String message,
                                     Object param) {
        
        com.log4rich.core.LogLevel richLevel = LevelTranslator.translate(level);
        if (!log4RichLogger.isLevelEnabled(richLevel)) {
            return;
        }
        
        String finalMessage = MessageExtractor.formatSingleParameter(message, param);
        ContextBridge.applyThreadContext();
        log4RichLogger.log(richLevel, finalMessage);
    }
    
    /**
     * Optimized version for two parameter messages (also very common).
     */
    public static void logTwoParams(com.log4rich.core.Logger log4RichLogger,
                                   Level level,
                                   String message,
                                   Object param1,
                                   Object param2) {
        
        com.log4rich.core.LogLevel richLevel = LevelTranslator.translate(level);
        if (!log4RichLogger.isLevelEnabled(richLevel)) {
            return;
        }
        
        String finalMessage = MessageExtractor.formatTwoParameters(message, param1, param2);
        ContextBridge.applyThreadContext();
        log4RichLogger.log(richLevel, finalMessage);
    }
    
    /**
     * Optimized version for exception logging without parameters.
     */
    public static void logWithException(com.log4rich.core.Logger log4RichLogger,
                                       Level level,
                                       String message,
                                       Throwable throwable) {
        
        com.log4rich.core.LogLevel richLevel = LevelTranslator.translate(level);
        if (!log4RichLogger.isLevelEnabled(richLevel)) {
            return;
        }
        
        ContextBridge.applyThreadContext();
        log4RichLogger.log(richLevel, message, throwable);
    }
    
    /**
     * Check if logging is enabled for a specific level.
     * Used by isDebugEnabled(), isInfoEnabled(), etc.
     */
    public static boolean isEnabled(com.log4rich.core.Logger log4RichLogger, Level level) {
        com.log4rich.core.LogLevel richLevel = LevelTranslator.translate(level);
        return log4RichLogger.isLevelEnabled(richLevel);
    }
    
    /**
     * Check if logging is enabled for a specific level with marker consideration.
     */
    public static boolean isEnabled(com.log4rich.core.Logger log4RichLogger, 
                                   Level level, 
                                   Marker marker) {
        // First check basic level
        if (!isEnabled(log4RichLogger, level)) {
            return false;
        }
        
        // Then check marker filtering
        return MarkerHandler.shouldLog(marker, level);
    }
}