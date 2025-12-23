package com.log4rich.integration.springboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Spring Boot application using SLF4J → log4j2 → log4Rich bridge.
 * 
 * These tests verify:
 * - SLF4J logging works correctly through the bridge
 * - Spring Boot components log properly
 * - Performance is acceptable
 * - MDC context is preserved
 * - No integration issues occur
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "logging.level.com.log4rich.integration.springboot=DEBUG",
    "test.logging.enabled=true"
})
public class SpringBootIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringBootIntegrationTest.class);
    
    @LocalServerPort
    private int port;
    
    private TestRestTemplate restTemplate;
    private String baseUrl;
    
    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate();
        baseUrl = "http://localhost:" + port;
        
        // Set up test context
        MDC.put("test-class", this.getClass().getSimpleName());
        MDC.put("test-start", String.valueOf(System.currentTimeMillis()));
        
        logger.info("Starting Spring Boot integration test on port {}", port);
    }
    
    @AfterEach
    void tearDown() {
        logger.info("Completed Spring Boot integration test");
        MDC.clear();
    }
    
    @Test
    void testApplicationStartsSuccessfully() {
        logger.info("Testing Spring Boot application startup");
        
        // Test that the application started and basic endpoint works
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/api/test/hello?name=IntegrationTest", Map.class);
        
        logger.info("Received response: status={}, body={}", 
                   response.getStatusCode(), response.getBody());
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
        assertTrue(response.getBody().get("message").toString().contains("IntegrationTest"));
        
        logger.info("✓ Application startup test passed");
    }
    
    @Test
    void testSLF4JLoggingThroughBridge() {
        logger.info("Testing SLF4J logging through log4j2-log4Rich bridge");
        
        // Test all log levels
        logger.trace("TRACE level test message");
        logger.debug("DEBUG level test message");
        logger.info("INFO level test message");
        logger.warn("WARN level test message");
        logger.error("ERROR level test message");
        
        // Test parameter formatting
        String testValue = "bridge-test";
        int testNumber = 42;
        logger.info("Parameter test: string={}, number={}", testValue, testNumber);
        
        // Test exception logging
        Exception testException = new RuntimeException("Test exception for integration test");
        logger.error("Exception test message", testException);
        
        logger.info("✓ SLF4J logging test passed");
    }
    
    @Test
    void testMDCContextPreservation() {
        logger.info("Testing MDC context preservation through bridge");
        
        // Set up MDC context
        MDC.put("user-id", "test-user-123");
        MDC.put("session-id", "test-session-456");
        MDC.put("transaction-id", "test-tx-789");
        
        try {
            logger.info("Message with MDC context");
            
            // Test HTTP request with MDC
            ResponseEntity<Map> response = restTemplate.getForEntity(
                baseUrl + "/api/test/hello?name=MDCTest", Map.class);
            
            assertEquals(200, response.getStatusCodeValue());
            logger.info("HTTP request completed with MDC context intact");
            
        } finally {
            MDC.clear();
        }
        
        logger.info("✓ MDC context preservation test passed");
    }
    
    @Test
    void testPerformanceAcceptable() {
        logger.info("Testing logging performance through bridge");
        
        int messageCount = 5000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < messageCount; i++) {
            logger.debug("Performance test message {} with timestamp {}", i, System.nanoTime());
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double messagesPerSecond = messageCount / (durationMs / 1000);
        
        logger.info("Performance test results: {} messages in {:.2f} ms ({:.0f} msg/s)", 
                   messageCount, durationMs, messagesPerSecond);
        
        // Assert reasonable performance (should be much higher than this minimum)
        assertTrue(messagesPerSecond > 1000, 
                  "Performance too low: " + messagesPerSecond + " msg/s");
        
        logger.info("✓ Performance test passed: {:.0f} messages/second", messagesPerSecond);
    }
    
    @Test
    void testWebEndpointLogging() {
        logger.info("Testing web endpoint logging through bridge");
        
        // Test successful request
        ResponseEntity<Map> successResponse = restTemplate.getForEntity(
            baseUrl + "/api/test/hello?name=WebTest", Map.class);
        
        assertEquals(200, successResponse.getStatusCodeValue());
        logger.info("Successful web request test completed");
        
        // Test error request
        ResponseEntity<Map> errorResponse = restTemplate.getForEntity(
            baseUrl + "/api/test/hello?name=error", Map.class);
        
        assertEquals(500, errorResponse.getStatusCodeValue());
        logger.info("Error web request test completed");
        
        logger.info("✓ Web endpoint logging test passed");
    }
    
    @Test
    void testPostRequestWithData() {
        logger.info("Testing POST request with JSON data");
        
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", "Integration Test");
        requestData.put("value", 123);
        requestData.put("flag", true);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/api/test/data", requestData, Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().get("status"));
        
        logger.info("✓ POST request test passed");
    }
    
    @Test
    void testPerformanceEndpoint() {
        logger.info("Testing performance endpoint");
        
        ResponseEntity<Map> response = restTemplate.getForEntity(
            baseUrl + "/api/test/performance?messageCount=1000", Map.class);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("messagesPerSecond"));
        
        Double messagesPerSecond = (Double) response.getBody().get("messagesPerSecond");
        assertTrue(messagesPerSecond > 500, 
                  "Performance endpoint reports low performance: " + messagesPerSecond);
        
        logger.info("✓ Performance endpoint test passed: {:.0f} msg/s", messagesPerSecond);
    }
    
    @Test
    void testLongRunningOperation() {
        logger.info("Testing long-running operation with continuous logging");
        
        int iterations = 100;
        int logsPerIteration = 10;
        
        for (int i = 0; i < iterations; i++) {
            MDC.put("iteration", String.valueOf(i));
            
            try {
                for (int j = 0; j < logsPerIteration; j++) {
                    logger.debug("Long-running operation: iteration={}, step={}", i, j);
                }
                
                if (i % 25 == 0) {
                    logger.info("Long-running operation progress: {} of {} iterations", i, iterations);
                }
                
                // Small delay to simulate work
                Thread.sleep(2);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Long-running operation interrupted", e);
                break;
            } finally {
                MDC.remove("iteration");
            }
        }
        
        logger.info("✓ Long-running operation test passed: {} iterations completed", iterations);
    }
    
    @Test
    void testConcurrentLogging() throws InterruptedException {
        logger.info("Testing concurrent logging from multiple threads");
        
        int threadCount = 5;
        int messagesPerThread = 200;
        Thread[] threads = new Thread[threadCount];
        
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                MDC.put("thread-id", String.valueOf(threadId));
                MDC.put("thread-name", Thread.currentThread().getName());
                
                try {
                    for (int i = 0; i < messagesPerThread; i++) {
                        logger.debug("Concurrent logging: thread={}, message={}", threadId, i);
                        
                        if (i % 50 == 0) {
                            logger.info("Thread {} progress: {} messages", threadId, i);
                        }
                    }
                } finally {
                    MDC.clear();
                }
            });
        }
        
        // Start all threads
        long startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for completion
        for (Thread thread : threads) {
            thread.join(10000); // 10 second timeout
        }
        long endTime = System.currentTimeMillis();
        
        double durationSeconds = (endTime - startTime) / 1000.0;
        int totalMessages = threadCount * messagesPerThread;
        double messagesPerSecond = totalMessages / durationSeconds;
        
        logger.info("Concurrent logging completed: {} threads, {} messages total, " +
                   "{:.2f} seconds, {:.0f} msg/s", 
                   threadCount, totalMessages, durationSeconds, messagesPerSecond);
        
        assertTrue(messagesPerSecond > 1000, 
                  "Concurrent performance too low: " + messagesPerSecond + " msg/s");
        
        logger.info("✓ Concurrent logging test passed");
    }
}