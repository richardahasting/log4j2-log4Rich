package com.log4rich.integration.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class to test SLF4J logging in Spring service components.
 * Demonstrates logging in business logic and async operations.
 */
@Service
public class LoggingTestService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingTestService.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    public void performSynchronousOperation(String operationId) {
        MDC.put("operationId", operationId);
        MDC.put("operationType", "synchronous");
        
        try {
            logger.info("Starting synchronous operation: {}", operationId);
            
            // Simulate multi-step operation
            step1(operationId);
            step2(operationId);
            step3(operationId);
            
            logger.info("Synchronous operation completed successfully: {}", operationId);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Synchronous operation interrupted: {}", operationId, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Synchronous operation failed: {}", operationId, e);
            throw new RuntimeException(e);
        } finally {
            MDC.remove("operationId");
            MDC.remove("operationType");
        }
    }
    
    public CompletableFuture<String> performAsynchronousOperation(String operationId) {
        logger.info("Initiating asynchronous operation: {}", operationId);
        
        return CompletableFuture.supplyAsync(() -> {
            // Set up MDC for async thread
            MDC.put("operationId", operationId);
            MDC.put("operationType", "asynchronous");
            MDC.put("thread", Thread.currentThread().getName());
            
            try {
                logger.info("Starting asynchronous operation in thread: {}", Thread.currentThread().getName());
                
                // Simulate async work
                Thread.sleep(50);
                
                // Test logging from async context
                logger.debug("Async operation step 1 completed for: {}", operationId);
                Thread.sleep(25);
                
                logger.debug("Async operation step 2 completed for: {}", operationId);
                Thread.sleep(25);
                
                logger.info("Asynchronous operation completed successfully: {}", operationId);
                return "Result for " + operationId;
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Asynchronous operation interrupted: {}", operationId, e);
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.error("Asynchronous operation failed: {}", operationId, e);
                throw new RuntimeException(e);
            } finally {
                MDC.clear();
            }
        }, executorService);
    }
    
    public void performBulkLogging(int messageCount) {
        String operationId = "bulk-" + System.currentTimeMillis();
        MDC.put("operationId", operationId);
        MDC.put("operationType", "bulk-logging");
        MDC.put("messageCount", String.valueOf(messageCount));
        
        try {
            logger.info("Starting bulk logging operation with {} messages", messageCount);
            
            long startTime = System.nanoTime();
            
            for (int i = 0; i < messageCount; i++) {
                // Mix different log levels
                if (i % 100 == 0) {
                    logger.info("Bulk logging checkpoint: {} of {} messages processed", i, messageCount);
                } else if (i % 50 == 0) {
                    logger.debug("Bulk logging progress: {} messages processed", i);
                } else {
                    logger.trace("Processing bulk message {} with data: {}", i, "sample-data-" + i);
                }
            }
            
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            double messagesPerSecond = messageCount / (durationMs / 1000);
            
            logger.info("Bulk logging completed: {} messages in {:.2f} ms ({:.0f} msg/s)", 
                       messageCount, durationMs, messagesPerSecond);
            
        } catch (Exception e) {
            logger.error("Bulk logging operation failed for {} messages", messageCount, e);
            throw e;
        } finally {
            MDC.remove("operationId");
            MDC.remove("operationType");
            MDC.remove("messageCount");
        }
    }
    
    private void step1(String operationId) throws InterruptedException {
        logger.debug("Executing step 1 for operation: {}", operationId);
        Thread.sleep(10);
        
        if (Math.random() < 0.1) { // 10% chance of warning
            logger.warn("Step 1 completed with warnings for operation: {}", operationId);
        } else {
            logger.debug("Step 1 completed successfully for operation: {}", operationId);
        }
    }
    
    private void step2(String operationId) throws InterruptedException {
        logger.debug("Executing step 2 for operation: {}", operationId);
        Thread.sleep(15);
        
        // Simulate conditional logging
        boolean complexProcessing = operationId.contains("complex");
        if (complexProcessing) {
            logger.info("Complex processing detected for operation: {}", operationId);
            logger.debug("Applying complex processing rules");
        }
        
        logger.debug("Step 2 completed for operation: {}", operationId);
    }
    
    private void step3(String operationId) throws InterruptedException {
        logger.debug("Executing step 3 for operation: {}", operationId);
        Thread.sleep(5);
        
        // Simulate error condition
        if (operationId.contains("error")) {
            logger.error("Step 3 detected error condition for operation: {}", operationId);
            throw new RuntimeException("Simulated error in step 3");
        }
        
        logger.debug("Step 3 completed successfully for operation: {}", operationId);
    }
}