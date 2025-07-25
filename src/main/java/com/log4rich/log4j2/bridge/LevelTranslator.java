package com.log4rich.log4j2.bridge;

import org.apache.logging.log4j.Level;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Translates log4j2 levels to log4Rich levels.
 * Thread-safe with caching for performance.
 */
public class LevelTranslator {
    
    private static final Map<Level, com.log4rich.core.LogLevel> LEVEL_CACHE = new ConcurrentHashMap<>();
    
    static {
        // Pre-populate cache with standard levels
        LEVEL_CACHE.put(Level.TRACE, com.log4rich.core.LogLevel.TRACE);
        LEVEL_CACHE.put(Level.DEBUG, com.log4rich.core.LogLevel.DEBUG);
        LEVEL_CACHE.put(Level.INFO, com.log4rich.core.LogLevel.INFO);
        LEVEL_CACHE.put(Level.WARN, com.log4rich.core.LogLevel.WARN);
        LEVEL_CACHE.put(Level.ERROR, com.log4rich.core.LogLevel.ERROR);
        LEVEL_CACHE.put(Level.FATAL, com.log4rich.core.LogLevel.FATAL);
        LEVEL_CACHE.put(Level.ALL, com.log4rich.core.LogLevel.ALL);
        LEVEL_CACHE.put(Level.OFF, com.log4rich.core.LogLevel.OFF);
    }
    
    /**
     * Translates a log4j2 level to the corresponding log4Rich level.
     * Uses caching for performance with custom levels.
     */
    public static com.log4rich.core.LogLevel translate(Level log4j2Level) {
        if (log4j2Level == null) {
            return com.log4rich.core.LogLevel.INFO; // Default fallback
        }
        
        return LEVEL_CACHE.computeIfAbsent(log4j2Level, level -> {
            // Handle custom levels by mapping based on integer value
            int intLevel = level.intLevel();
            
            if (intLevel >= Level.FATAL.intLevel()) {
                return com.log4rich.core.LogLevel.FATAL;
            } else if (intLevel >= Level.ERROR.intLevel()) {
                return com.log4rich.core.LogLevel.ERROR;
            } else if (intLevel >= Level.WARN.intLevel()) {
                return com.log4rich.core.LogLevel.WARN;
            } else if (intLevel >= Level.INFO.intLevel()) {
                return com.log4rich.core.LogLevel.INFO;
            } else if (intLevel >= Level.DEBUG.intLevel()) {
                return com.log4rich.core.LogLevel.DEBUG;
            } else {
                return com.log4rich.core.LogLevel.TRACE;
            }
        });
    }
    
    /**
     * Reverse translation from log4Rich to log4j2 levels.
     */
    public static Level translateReverse(com.log4rich.core.LogLevel log4RichLevel) {
        if (log4RichLevel == null) {
            return Level.INFO;
        }
        
        switch (log4RichLevel.toString().toUpperCase()) {
            case "TRACE": return Level.TRACE;
            case "DEBUG": return Level.DEBUG;
            case "INFO": return Level.INFO;
            case "WARN": return Level.WARN;
            case "ERROR": return Level.ERROR;
            case "FATAL": return Level.FATAL;
            case "ALL": return Level.ALL;
            case "OFF": return Level.OFF;
            default: return Level.INFO;
        }
    }
}