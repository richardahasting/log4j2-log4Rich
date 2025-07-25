package com.log4rich.integration.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller to test SLF4J logging in Spring MVC components.
 * Demonstrates logging in typical web application scenarios.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello(@RequestParam(defaultValue = "World") String name) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("endpoint", "/api/test/hello");
        
        try {
            logger.info("Processing hello request for name: {}", name);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Hello, " + name + "!");
            response.put("timestamp", System.currentTimeMillis());
            response.put("requestId", requestId);
            
            logger.debug("Response prepared: {}", response);
            
            if ("error".equalsIgnoreCase(name)) {
                logger.warn("Error name detected, simulating error response");
                throw new RuntimeException("Simulated error for name: " + name);
            }
            
            logger.info("Hello request completed successfully for name: {}", name);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to process hello request for name: {}", name, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal server error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("requestId", requestId);
            
            return ResponseEntity.internalServerError().body(errorResponse);
        } finally {
            MDC.clear();
        }
    }
    
    @PostMapping("/data")
    public ResponseEntity<Map<String, Object>> processData(@RequestBody Map<String, Object> data) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("endpoint", "/api/test/data");
        MDC.put("dataSize", String.valueOf(data.size()));
        
        try {
            logger.info("Processing data with {} fields", data.size());
            logger.debug("Input data: {}", data);
            
            // Simulate data processing
            processBusinessData(data);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("processed", data.size());
            response.put("requestId", requestId);
            
            logger.info("Data processing completed successfully, processed {} fields", data.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to process data with {} fields", data.size(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Data processing failed");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("requestId", requestId);
            
            return ResponseEntity.internalServerError().body(errorResponse);
        } finally {
            MDC.clear();
        }
    }
    
    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> performanceTest(@RequestParam(defaultValue = "1000") int messageCount) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("endpoint", "/api/test/performance");
        MDC.put("messageCount", String.valueOf(messageCount));
        
        try {
            logger.info("Starting performance test with {} messages", messageCount);
            
            long startTime = System.nanoTime();
            
            for (int i = 0; i < messageCount; i++) {
                logger.debug("Performance test message {} with timestamp {} and request {}", 
                           i, System.nanoTime(), requestId);
            }
            
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            double messagesPerSecond = messageCount / (durationMs / 1000);
            
            logger.info("Performance test completed: {} messages in {:.2f} ms ({:.0f} msg/s)", 
                       messageCount, durationMs, messagesPerSecond);
            
            Map<String, Object> response = new HashMap<>();
            response.put("messageCount", messageCount);
            response.put("durationMs", durationMs);
            response.put("messagesPerSecond", messagesPerSecond);
            response.put("requestId", requestId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Performance test failed with {} messages", messageCount, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Performance test failed");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("requestId", requestId);
            
            return ResponseEntity.internalServerError().body(errorResponse);
        } finally {
            MDC.clear();
        }
    }
    
    private void processBusinessData(Map<String, Object> data) throws Exception {
        logger.debug("Starting business data processing");
        
        // Simulate validation
        if (data.containsKey("invalid")) {
            logger.warn("Invalid data detected: {}", data.get("invalid"));
            throw new IllegalArgumentException("Data contains invalid field");
        }
        
        // Simulate processing delay
        Thread.sleep(5);
        
        // Simulate processing steps
        logger.trace("Step 1: Data validation completed");
        logger.trace("Step 2: Data transformation in progress");
        logger.trace("Step 3: Data persistence simulation");
        
        logger.debug("Business data processing completed successfully");
    }
}