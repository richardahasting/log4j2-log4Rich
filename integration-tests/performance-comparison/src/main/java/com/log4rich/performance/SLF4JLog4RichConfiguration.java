package com.log4rich.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Configuration for SLF4J → log4j2 → log4Rich bridge chain.
 * This represents our primary integration path for SLF4J applications.
 */
public class SLF4JLog4RichConfiguration implements LoggingConfiguration {
    
    private static final String CONFIG_FILE = "target/log4rich-performance.properties";
    
    @Override
    public String getName() {
        return "SLF4J → log4j2 → log4Rich";
    }
    
    @Override
    public String getDescription() {
        return "SLF4J API → log4j-slf4j-impl → log4j2-log4Rich bridge → log4Rich backend";
    }
    
    @Override
    public void initialize() throws Exception {
        // Create optimized log4Rich configuration for performance testing
        createLog4RichConfiguration();
        
        // Set system property to use our configuration
        System.setProperty("log4rich.config", new File(CONFIG_FILE).getAbsolutePath());
        
        // Ensure SLF4J uses log4j2 implementation (via log4j-slf4j-impl)
        // This should be automatically detected via classpath
    }
    
    @Override
    public void cleanup() throws Exception {
        // Reset system properties
        System.clearProperty("log4rich.config");
        
        // Clean up configuration file
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            configFile.delete();
        }
    }
    
    @Override
    public Object createLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
    
    @Override
    public void logMessage(Object logger, String level, String message) {
        Logger slf4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                slf4jLogger.trace(message);
                break;
            case "DEBUG":
                slf4jLogger.debug(message);
                break;
            case "INFO":
                slf4jLogger.info(message);
                break;
            case "WARN":
                slf4jLogger.warn(message);
                break;
            case "ERROR":
                slf4jLogger.error(message);
                break;
            default:
                slf4jLogger.info(message);
        }
    }
    
    @Override
    public void logParameterizedMessage(Object logger, String level, String pattern, Object... args) {
        Logger slf4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                slf4jLogger.trace(pattern, args);
                break;
            case "DEBUG":
                slf4jLogger.debug(pattern, args);
                break;
            case "INFO":
                slf4jLogger.info(pattern, args);
                break;
            case "WARN":
                slf4jLogger.warn(pattern, args);
                break;
            case "ERROR":
                slf4jLogger.error(pattern, args);
                break;
            default:
                slf4jLogger.info(pattern, args);
        }
    }
    
    @Override
    public void logWithException(Object logger, String level, String message, Throwable throwable) {
        Logger slf4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                slf4jLogger.trace(message, throwable);
                break;
            case "DEBUG":
                slf4jLogger.debug(message, throwable);
                break;
            case "INFO":
                slf4jLogger.info(message, throwable);
                break;
            case "WARN":
                slf4jLogger.warn(message, throwable);
                break;
            case "ERROR":
                slf4jLogger.error(message, throwable);
                break;
            default:
                slf4jLogger.error(message, throwable);
        }
    }
    
    @Override
    public boolean isLevelEnabled(Object logger, String level) {
        Logger slf4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                return slf4jLogger.isTraceEnabled();
            case "DEBUG":
                return slf4jLogger.isDebugEnabled();
            case "INFO":
                return slf4jLogger.isInfoEnabled();
            case "WARN":
                return slf4jLogger.isWarnEnabled();
            case "ERROR":
                return slf4jLogger.isErrorEnabled();
            default:
                return true;
        }
    }
    
    @Override
    public String getConfigurationDetails() {
        return "SLF4J bridge with optimized log4Rich backend (async disabled for consistent benchmarking)";
    }
    
    private void createLog4RichConfiguration() throws IOException {
        File configFile = new File(CONFIG_FILE);
        configFile.getParentFile().mkdirs();
        
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# log4Rich Performance Testing Configuration\\n");
            writer.write("# Optimized for benchmarking consistency\\n");
            writer.write("\\n");
            
            // Root level - allow all messages for testing
            writer.write("log4rich.level=DEBUG\\n");
            writer.write("\\n");
            
            // Console appender - disabled for performance testing
            writer.write("log4rich.appender.console=false\\n");
            writer.write("\\n");
            
            // File appender - enabled but optimized
            writer.write("log4rich.appender.file=true\\n");
            writer.write("log4rich.appender.file.path=target/performance-test.log\\n");
            writer.write("log4rich.appender.file.level=DEBUG\\n");
            writer.write("log4rich.appender.file.immediateFlush=false\\n");
            writer.write("log4rich.appender.file.bufferSize=65536\\n");
            writer.write("\\n");
            
            // Format - simple for performance
            writer.write("log4rich.format=%d{HH:mm:ss.SSS} [%thread] %level %logger{36} - %msg%n\\n");
            writer.write("\\n");
            
            // Performance settings - disable async for consistent timing
            writer.write("log4rich.performance.async=false\\n");
            writer.write("log4rich.performance.batchEnabled=false\\n");
            writer.write("log4rich.performance.memoryMapped=false\\n");
            writer.write("\\n");
            
            // Location capture - disabled for performance
            writer.write("log4rich.location.capture=false\\n");
            writer.write("\\n");
            
            // Context integration
            writer.write("log4rich.context.mdc=true\\n");
            writer.write("log4rich.context.ndc=true\\n");
        }
    }
}