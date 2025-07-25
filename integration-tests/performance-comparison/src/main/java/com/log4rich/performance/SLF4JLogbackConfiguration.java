package com.log4rich.performance;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Configuration for standard SLF4J → Logback setup.
 * This serves as a baseline comparison for SLF4J applications.
 */
public class SLF4JLogbackConfiguration implements LoggingConfiguration {
    
    private LoggerContext loggerContext;
    
    @Override
    public String getName() {
        return "SLF4J → Logback";
    }
    
    @Override
    public String getDescription() {
        return "SLF4J API → Logback Classic implementation (baseline)";
    }
    
    @Override
    public void initialize() throws Exception {
        // Configure Logback programmatically for consistent testing
        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
        
        // Create file appender
        FileAppender fileAppender = new FileAppender();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("FILE");
        
        // Ensure target directory exists
        File logFile = new File("target/logback-performance-test.log");
        logFile.getParentFile().mkdirs();
        fileAppender.setFile(logFile.getAbsolutePath());
        
        // Create pattern encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %level %logger{36} - %msg%n");
        encoder.start();
        
        fileAppender.setEncoder(encoder);
        fileAppender.start();
        
        // Get root logger and configure it
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(
            ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(fileAppender);
        rootLogger.setLevel(ch.qos.logback.classic.Level.DEBUG);
    }
    
    @Override
    public void cleanup() throws Exception {
        if (loggerContext != null) {
            loggerContext.reset();
        }
        
        // Clean up log file
        File logFile = new File("target/logback-performance-test.log");
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
        return "Standard Logback configuration with file appender (immediate flush disabled)";
    }
}