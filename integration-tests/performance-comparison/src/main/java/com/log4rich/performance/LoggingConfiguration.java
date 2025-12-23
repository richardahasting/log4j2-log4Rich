package com.log4rich.performance;

/**
 * Base interface for different logging framework configurations.
 * Each implementation represents a specific logging setup to be benchmarked.
 */
public interface LoggingConfiguration {
    
    /**
     * Get a descriptive name for this logging configuration.
     */
    String getName();
    
    /**
     * Get a description of this configuration including the logging chain.
     */
    String getDescription();
    
    /**
     * Initialize this logging configuration.
     * This may involve setting system properties, configuring appenders, etc.
     */
    void initialize() throws Exception;
    
    /**
     * Clean up this logging configuration.
     * This should reset any global state to avoid interference between tests.
     */
    void cleanup() throws Exception;
    
    /**
     * Create a logger instance for performance testing.
     * The returned object should support basic logging operations.
     */
    Object createLogger(String name);
    
    /**
     * Log a simple message using the provided logger.
     */
    void logMessage(Object logger, String level, String message);
    
    /**
     * Log a parameterized message using the provided logger.
     */
    void logParameterizedMessage(Object logger, String level, String pattern, Object... args);
    
    /**
     * Log a message with an exception using the provided logger.
     */
    void logWithException(Object logger, String level, String message, Throwable throwable);
    
    /**
     * Check if a specific log level is enabled for the given logger.
     */
    boolean isLevelEnabled(Object logger, String level);
    
    /**
     * Get configuration-specific settings or properties.
     */
    default String getConfigurationDetails() {
        return "Default configuration";
    }
}