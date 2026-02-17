package org.apache.logging.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * log4j2 Level implementation that delegates to log4Rich.
 * Provides all standard levels plus support for custom levels.
 */
public class Level implements Comparable<Level> {
    
    // Standard log4j2 levels with their integer values
    public static final Level OFF = new Level("OFF", 0);
    public static final Level FATAL = new Level("FATAL", 100);
    public static final Level ERROR = new Level("ERROR", 200);
    public static final Level WARN = new Level("WARN", 300);
    public static final Level INFO = new Level("INFO", 400);
    public static final Level DEBUG = new Level("DEBUG", 500);
    public static final Level TRACE = new Level("TRACE", 600);
    public static final Level ALL = new Level("ALL", Integer.MAX_VALUE);
    
    // Cache for custom levels
    private static final ConcurrentMap<String, Level> LEVELS = new ConcurrentHashMap<>();
    
    static {
        // Pre-register standard levels
        LEVELS.put(OFF.name, OFF);
        LEVELS.put(FATAL.name, FATAL);
        LEVELS.put(ERROR.name, ERROR);
        LEVELS.put(WARN.name, WARN);
        LEVELS.put(INFO.name, INFO);
        LEVELS.put(DEBUG.name, DEBUG);
        LEVELS.put(TRACE.name, TRACE);
        LEVELS.put(ALL.name, ALL);
    }
    
    private final String name;
    private final int intLevel;
    
    private Level(String name, int intLevel) {
        this.name = name;
        this.intLevel = intLevel;
    }
    
    /**
     * Gets or creates a level with the specified name and integer value.
     */
    public static Level forName(String name, int intValue) {
        if (name == null) {
            throw new IllegalArgumentException("Level name cannot be null");
        }
        
        return LEVELS.computeIfAbsent(name.toUpperCase(), 
            key -> new Level(key, intValue));
    }
    
    /**
     * Gets a level by name, creating it with a default value if necessary.
     */
    public static Level getLevel(String name) {
        if (name == null) {
            return null;
        }
        
        Level level = LEVELS.get(name.toUpperCase());
        if (level != null) {
            return level;
        }
        
        // For unknown levels, try to map to standard levels
        String upperName = name.toUpperCase();
        switch (upperName) {
            case "SEVERE": return ERROR;  // java.util.logging compatibility
            case "WARNING": return WARN;  // java.util.logging compatibility
            case "CONFIG": return INFO;   // java.util.logging compatibility
            case "FINE": return DEBUG;    // java.util.logging compatibility
            case "FINER": 
            case "FINEST": return TRACE;  // java.util.logging compatibility
            default:
                // Create custom level with INFO priority
                return forName(upperName, INFO.intLevel);
        }
    }
    
    /**
     * Converts string to Level, with fallback to INFO if invalid.
     */
    public static Level toLevel(String name) {
        Level level = getLevel(name);
        return level != null ? level : INFO;
    }
    
    /**
     * Converts string to Level with specified default.
     */
    public static Level toLevel(String name, Level defaultLevel) {
        Level level = getLevel(name);
        return level != null ? level : defaultLevel;
    }
    
    /**
     * Gets the level name.
     */
    public String name() {
        return name;
    }
    
    /**
     * Gets the integer level value.
     */
    public int intLevel() {
        return intLevel;
    }
    
    /**
     * Checks if this level is more specific than the specified level.
     */
    public boolean isMoreSpecificThan(Level level) {
        return this.intLevel <= level.intLevel;
    }

    /**
     * Checks if this level is less specific than the specified level.
     */
    public boolean isLessSpecificThan(Level level) {
        return this.intLevel >= level.intLevel;
    }
    
    /**
     * Checks if this level is in the specified range.
     */
    public boolean isInRange(Level minLevel, Level maxLevel) {
        return this.intLevel >= minLevel.intLevel && this.intLevel <= maxLevel.intLevel;
    }
    
    @Override
    public int compareTo(Level other) {
        return Integer.compare(this.intLevel, other.intLevel);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Level level = (Level) obj;
        return intLevel == level.intLevel && name.equals(level.name);
    }
    
    @Override
    public int hashCode() {
        return 31 * name.hashCode() + intLevel;
    }
    
    @Override
    public String toString() {
        return name;
    }
}