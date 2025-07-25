package com.log4rich.performance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Configuration for SLF4J → log4j2 (standard) setup.
 * This provides comparison against standard log4j2 without our bridge.
 */
public class SLF4JLog4j2Configuration implements LoggingConfiguration {
    
    private LoggerContext loggerContext;
    
    @Override
    public String getName() {
        return "SLF4J → log4j2";
    }
    
    @Override
    public String getDescription() {
        return "SLF4J API → log4j2 implementation (standard)";
    }
    
    @Override
    public void initialize() throws Exception {
        // Configure log4j2 programmatically
        loggerContext = (LoggerContext) LogManager.getContext(false);
        
        // Create a new configuration
        Configuration config = new DefaultConfiguration();
        config.initialize();
        
        // Create file appender
        File logFile = new File("target/log4j2-performance-test.log");
        logFile.getParentFile().mkdirs();
        
        PatternLayout layout = PatternLayout.newBuilder()
            .withPattern("%d{HH:mm:ss.SSS} [%t] %level %logger{36} - %msg%n")
            .build();
        
        FileAppender fileAppender = FileAppender.newBuilder()
            .withFileName(logFile.getAbsolutePath())
            .withLayout(layout)
            .withName("FILE")
            .withImmediateFlush(false)
            .withBufferSize(65536)
            .build();
        
        fileAppender.start();
        config.addAppender(fileAppender);
        
        // Configure root logger
        config.getRootLogger().addAppender(fileAppender, null, null);
        config.getRootLogger().setLevel(org.apache.logging.log4j.Level.DEBUG);
        
        // Update context
        loggerContext.reconfigure(config);
    }
    
    @Override
    public void cleanup() throws Exception {
        if (loggerContext != null) {
            loggerContext.reconfigure();
        }
        
        // Clean up log file
        File logFile = new File("target/log4j2-performance-test.log");
        if (logFile.exists()) {
            logFile.delete();
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
        return "Standard log4j2 configuration with file appender (immediate flush disabled)";
    }
}