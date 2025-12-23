package com.log4rich.performance;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Configuration for Java Util Logging (JUL).
 * This provides baseline comparison for applications using the built-in Java logging.
 */
public class JULConfiguration implements LoggingConfiguration {
    
    private FileHandler fileHandler;
    
    @Override
    public String getName() {
        return "Java Util Logging";
    }
    
    @Override
    public String getDescription() {
        return "Built-in Java Util Logging (java.util.logging)";
    }
    
    @Override
    public void initialize() throws Exception {
        // Configure JUL programmatically
        Logger rootLogger = Logger.getLogger("");
        
        // Remove default handlers
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        
        // Create file handler
        File logFile = new File("target/jul-performance-test.log");
        logFile.getParentFile().mkdirs();
        
        try {
            fileHandler = new FileHandler(logFile.getAbsolutePath(), false);
            SimpleFormatter formatter = new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%1$tH:%1$tM:%1$tS.%1$tL [%2$s] %3$s %4$s - %5$s%n",
                        record.getMillis(),
                        Thread.currentThread().getName(),
                        record.getLevel(),
                        record.getLoggerName(),
                        record.getMessage()
                    );
                }
            };
            fileHandler.setFormatter(formatter);
            
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.ALL);
            
        } catch (IOException e) {
            throw new Exception("Failed to create JUL file handler", e);
        }
    }
    
    @Override
    public void cleanup() throws Exception {
        if (fileHandler != null) {
            fileHandler.close();
        }
        
        // Remove all handlers from root logger
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        
        // Clean up log file
        File logFile = new File("target/jul-performance-test.log");
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
        Logger julLogger = (Logger) logger;
        Level julLevel = convertLevel(level);
        julLogger.log(julLevel, message);
    }
    
    @Override
    public void logParameterizedMessage(Object logger, String level, String pattern, Object... args) {
        Logger julLogger = (Logger) logger;
        Level julLevel = convertLevel(level);
        
        // JUL doesn't have SLF4J-style {} parameters, so we'll simulate it
        String message = pattern;
        for (Object arg : args) {
            message = message.replaceFirst("\\{\\}", String.valueOf(arg));
        }
        
        julLogger.log(julLevel, message);
    }
    
    @Override
    public void logWithException(Object logger, String level, String message, Throwable throwable) {
        Logger julLogger = (Logger) logger;
        Level julLevel = convertLevel(level);
        julLogger.log(julLevel, message, throwable);
    }
    
    @Override
    public boolean isLevelEnabled(Object logger, String level) {
        Logger julLogger = (Logger) logger;
        Level julLevel = convertLevel(level);
        return julLogger.isLoggable(julLevel);
    }
    
    @Override
    public String getConfigurationDetails() {
        return "Java Util Logging with file handler and custom formatter";
    }
    
    private Level convertLevel(String level) {
        switch (level.toUpperCase()) {
            case "TRACE":
                return Level.FINEST;
            case "DEBUG":
                return Level.FINE;
            case "INFO":
                return Level.INFO;
            case "WARN":
                return Level.WARNING;
            case "ERROR":
            case "FATAL":
                return Level.SEVERE;
            default:
                return Level.INFO;
        }
    }
}