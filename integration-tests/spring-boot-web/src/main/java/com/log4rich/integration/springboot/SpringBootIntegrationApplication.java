package com.log4rich.integration.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Spring Boot application to test SLF4J → log4j2 → log4Rich bridge integration.
 * 
 * This application demonstrates:
 * - SLF4J logging from Spring Boot components
 * - Parameter formatting with {}
 * - MDC (Mapped Diagnostic Context)
 * - Exception logging
 * - Integration with Spring's logging infrastructure
 */
@SpringBootApplication
public class SpringBootIntegrationApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringBootIntegrationApplication.class);
    
    public static void main(String[] args) {
        logger.info("=== Starting Spring Boot Integration Test ===");
        logger.info("Testing SLF4J → log4j2 → log4Rich bridge chain");
        
        // Set up MDC context
        MDC.put("application", "spring-boot-integration");
        MDC.put("version", "1.0.0");
        MDC.put("test-id", "spring-boot-" + System.currentTimeMillis());
        
        try {
            logger.debug("About to start Spring Boot application");
            SpringApplication.run(SpringBootIntegrationApplication.class, args);
        } catch (Exception e) {
            logger.error("Failed to start Spring Boot application", e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("=== Spring Boot Application Ready ===");
        
        // Set up request context for demonstration
        MDC.put("request-id", "req-" + System.currentTimeMillis());
        MDC.put("user-id", "integration-test-user");
        
        try {
            demonstrateLoggingFeatures();
        } finally {
            MDC.clear();
        }
        
        logger.info("=== Spring Boot Integration Test Completed Successfully ===");
    }
    
    private void demonstrateLoggingFeatures() {
        logger.info("Demonstrating SLF4J logging features through log4Rich bridge");
        
        // Test all log levels
        logger.trace("TRACE level message - detailed debugging information");
        logger.debug("DEBUG level message - debugging information");
        logger.info("INFO level message - general application flow");
        logger.warn("WARN level message - warning about potential issues");
        logger.error("ERROR level message - error condition occurred");
        
        // Test parameter formatting
        String username = "testUser";
        int sessionCount = 42;
        double responseTime = 123.45;
        
        logger.info("User {} has {} active sessions", username, sessionCount);
        logger.info("Request processed in {} ms for user {}", responseTime, username);
        logger.info("Multiple parameters: user={}, sessions={}, responseTime={}", 
                   username, sessionCount, responseTime);
        
        // Test array parameter
        String[] permissions = {"READ", "WRITE", "ADMIN"};
        logger.info("User permissions: {}", (Object) permissions);
        
        // Test exception logging
        try {
            simulateBusinessLogic();
        } catch (Exception e) {
            logger.error("Business logic failed for user {}", username, e);
        }
        
        // Test MDC context
        logger.info("Current MDC context should include request-id and user-id");
        
        // Test performance with many log statements
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            logger.debug("Performance test iteration {} with timestamp {}", i, System.nanoTime());
        }
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        logger.info("Logged 1000 messages in {:.2f} ms ({:.0f} messages/second)", 
                   durationMs, 1000 / (durationMs / 1000));
    }
    
    private void simulateBusinessLogic() throws Exception {
        logger.debug("Starting business logic simulation");
        
        // Simulate some processing
        Thread.sleep(10);
        
        // Simulate an error condition
        if (Math.random() > 0.5) {
            throw new RuntimeException("Simulated business logic error for integration testing");
        }
        
        logger.debug("Business logic completed successfully");
    }
}