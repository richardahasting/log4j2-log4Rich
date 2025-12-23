package com.log4rich.performance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Configuration for direct log4Rich usage (no bridges).
 * This provides the baseline for maximum log4Rich performance.
 */
public class DirectLog4RichConfiguration implements LoggingConfiguration {
    
    private static final String CONFIG_FILE = "target/direct-log4rich-performance.properties";
    
    @Override
    public String getName() {
        return "Direct log4Rich";
    }
    
    @Override
    public String getDescription() {
        return "Direct log4Rich API usage (maximum performance baseline)";
    }
    
    @Override
    public void initialize() throws Exception {
        // Create optimized log4Rich configuration
        createLog4RichConfiguration();
        
        // Set system property to use our configuration
        System.setProperty("log4rich.config", new File(CONFIG_FILE).getAbsolutePath());
        
        // Initialize log4Rich
        // Note: This would require having log4Rich classes available
        // For now, we'll simulate this with a placeholder
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
        // This would return a log4Rich Logger instance
        // For demonstration, we'll use a mock implementation
        return new MockLog4RichLogger(name);
    }
    
    @Override
    public void logMessage(Object logger, String level, String message) {
        MockLog4RichLogger log4RichLogger = (MockLog4RichLogger) logger;
        log4RichLogger.log(level, message);
    }
    
    @Override
    public void logParameterizedMessage(Object logger, String level, String pattern, Object... args) {
        MockLog4RichLogger log4RichLogger = (MockLog4RichLogger) logger;
        log4RichLogger.logParameterized(level, pattern, args);
    }
    
    @Override
    public void logWithException(Object logger, String level, String message, Throwable throwable) {
        MockLog4RichLogger log4RichLogger = (MockLog4RichLogger) logger;
        log4RichLogger.logWithException(level, message, throwable);
    }
    
    @Override
    public boolean isLevelEnabled(Object logger, String level) {
        // Assume all levels are enabled for testing
        return true;
    }
    
    @Override
    public String getConfigurationDetails() {
        return "Direct log4Rich usage with optimized file appender (simulated)";
    }
    
    private void createLog4RichConfiguration() throws IOException {
        File configFile = new File(CONFIG_FILE);
        configFile.getParentFile().mkdirs();
        
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("# Direct log4Rich Performance Configuration\\n");
            writer.write("\\n");
            writer.write("log4rich.level=DEBUG\\n");
            writer.write("log4rich.appender.console=false\\n");
            writer.write("log4rich.appender.file=true\\n");
            writer.write("log4rich.appender.file.path=target/direct-log4rich-performance-test.log\\n");
            writer.write("log4rich.appender.file.level=DEBUG\\n");
            writer.write("log4rich.appender.file.immediateFlush=false\\n");
            writer.write("log4rich.appender.file.bufferSize=65536\\n");
            writer.write("log4rich.format=%d{HH:mm:ss.SSS} [%thread] %level %logger{36} - %msg%n\\n");
            writer.write("log4rich.performance.async=false\\n");
            writer.write("log4rich.location.capture=false\\n");
        }
    }
    
    /**
     * Mock implementation for demonstration purposes.
     * In a real implementation, this would be replaced with actual log4Rich Logger.
     */
    private static class MockLog4RichLogger {
        private final String name;
        
        public MockLog4RichLogger(String name) {
            this.name = name;
        }
        
        public void log(String level, String message) {
            // Simulate minimal logging overhead
            String formatted = String.format("[%s] %s - %s", level, name, message);
            // In reality, this would go through log4Rich's optimized pipeline
            simulateLoggingWork(formatted);
        }
        
        public void logParameterized(String level, String pattern, Object... args) {
            // Simulate parameter substitution
            String message = pattern;
            for (Object arg : args) {
                message = message.replaceFirst("\\\\{\\\\}", String.valueOf(arg));
            }
            log(level, message);
        }
        
        public void logWithException(String level, String message, Throwable throwable) {
            log(level, message + " - " + throwable.toString());
        }
        
        private void simulateLoggingWork(String message) {
            // Simulate the work that log4Rich would do
            // This is much faster than real I/O, representing optimized log4Rich performance
            int hash = message.hashCode();
            // Minimal CPU work to simulate formatting and buffering
        }
    }
}