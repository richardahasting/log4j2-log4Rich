package com.log4rich.performance;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.File;

/**
 * Configuration for legacy log4j 1.x.
 * This provides baseline comparison for legacy applications.
 */
public class Log4j1Configuration implements LoggingConfiguration {
    
    @Override
    public String getName() {
        return "log4j 1.x";
    }
    
    @Override
    public String getDescription() {
        return "Legacy log4j 1.x implementation";
    }
    
    @Override
    public void initialize() throws Exception {
        // Configure log4j 1.x programmatically
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.removeAllAppenders();
        
        // Create file appender
        File logFile = new File("target/log4j1-performance-test.log");
        logFile.getParentFile().mkdirs();
        
        PatternLayout layout = new PatternLayout("%d{HH:mm:ss.SSS} [%t] %p %c{1} - %m%n");
        
        FileAppender fileAppender = new FileAppender(layout, logFile.getAbsolutePath(), false);
        fileAppender.setImmediateFlush(false);
        fileAppender.activateOptions();
        
        rootLogger.addAppender(fileAppender);
        rootLogger.setLevel(Level.DEBUG);
    }
    
    @Override
    public void cleanup() throws Exception {
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.removeAllAppenders();
        
        // Clean up log file
        File logFile = new File("target/log4j1-performance-test.log");
        if (logFile.exists()) {
            logFile.delete();
        }
    }
    
    @Override
    public Object createLogger(String name) {
        return Logger.getLogger(name);
    }
    
    @Override
    public void logMessage(Object logger, String level, String message) {
        Logger log4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                log4jLogger.trace(message);
                break;
            case "DEBUG":
                log4jLogger.debug(message);
                break;
            case "INFO":
                log4jLogger.info(message);
                break;
            case "WARN":
                log4jLogger.warn(message);
                break;
            case "ERROR":
                log4jLogger.error(message);
                break;
            case "FATAL":
                log4jLogger.fatal(message);
                break;
            default:
                log4jLogger.info(message);
        }
    }
    
    @Override
    public void logParameterizedMessage(Object logger, String level, String pattern, Object... args) {
        Logger log4jLogger = (Logger) logger;
        
        // log4j 1.x doesn't have native parameter substitution, so we'll simulate it
        String message = pattern;
        for (Object arg : args) {
            message = message.replaceFirst("\\{\\}", String.valueOf(arg));
        }
        
        logMessage(logger, level, message);
    }
    
    @Override
    public void logWithException(Object logger, String level, String message, Throwable throwable) {
        Logger log4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                log4jLogger.trace(message, throwable);
                break;
            case "DEBUG":
                log4jLogger.debug(message, throwable);
                break;
            case "INFO":
                log4jLogger.info(message, throwable);
                break;
            case "WARN":
                log4jLogger.warn(message, throwable);
                break;
            case "ERROR":
                log4jLogger.error(message, throwable);
                break;
            case "FATAL":
                log4jLogger.fatal(message, throwable);
                break;
            default:
                log4jLogger.error(message, throwable);
        }
    }
    
    @Override
    public boolean isLevelEnabled(Object logger, String level) {
        Logger log4jLogger = (Logger) logger;
        
        switch (level.toUpperCase()) {
            case "TRACE":
                return log4jLogger.isTraceEnabled();
            case "DEBUG":
                return log4jLogger.isDebugEnabled();
            case "INFO":
                return log4jLogger.isInfoEnabled();
            case "WARN":
                return log4jLogger.getLevel().isGreaterOrEqual(Level.WARN);
            case "ERROR":
                return log4jLogger.getLevel().isGreaterOrEqual(Level.ERROR);
            case "FATAL":
                return log4jLogger.getLevel().isGreaterOrEqual(Level.FATAL);
            default:
                return true;
        }
    }
    
    @Override
    public String getConfigurationDetails() {
        return "Legacy log4j 1.x with file appender (immediate flush disabled)";
    }
}