package com.log4rich.integration.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Standalone demo showing SLF4J → log4j2 → log4Rich bridge working with Kafka-style logging.
 * This runs without requiring an actual Kafka broker.
 */
public class KafkaLoggingDemo {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaLoggingDemo.class);
    
    public static void main(String[] args) {
        MDC.put("application", "kafka-logging-demo");
        MDC.put("startTime", String.valueOf(System.currentTimeMillis()));
        
        try {
            logger.info("=== Kafka SLF4J Logging Bridge Demo Starting ===");
            logger.info("Testing SLF4J → log4j2 → log4Rich bridge with Kafka-style logging patterns");
            
            // Test different SLF4J features commonly used in Kafka applications
            testBasicLogging();
            testParameterFormatting();
            testMDCContextLogging();
            testErrorLogging();
            testHighVolumeLogging();
            
            logger.info("=== Kafka SLF4J Logging Bridge Demo Completed Successfully ===");
            
        } catch (Exception e) {
            logger.error("Demo failed", e);
            System.exit(1);
        } finally {
            MDC.clear();
        }
    }
    
    private static void testBasicLogging() {
        logger.info("Testing basic SLF4J logging levels");
        
        // Test all log levels typically used in Kafka applications
        logger.trace("TRACE: Detailed message processing trace");
        logger.debug("DEBUG: Producer/consumer debug information");
        logger.info("INFO: Kafka operation completed successfully");
        logger.warn("WARN: Non-critical Kafka warning");
        logger.error("ERROR: Kafka operation error");
        
        logger.info("✓ Basic logging test completed");
    }
    
    private static void testParameterFormatting() {
        logger.info("Testing SLF4J parameter formatting with Kafka metadata");
        
        // Simulate Kafka message metadata
        String topic = "user-events";
        int partition = 0;
        long offset = 12345L;
        String key = "user-123";
        int messageSize = 256;
        long timestamp = System.currentTimeMillis();
        
        // Test parameter formatting patterns common in Kafka logging
        logger.info("Message sent: topic={}, partition={}, offset={}, key={}", 
                   topic, partition, offset, key);
        
        logger.debug("Message details: topic={}, partition={}, offset={}, key={}, size={} bytes, timestamp={}", 
                    topic, partition, offset, key, messageSize, timestamp);
        
        // Test with arrays (common for partition assignments)
        int[] partitions = {0, 1, 2};
        logger.info("Consumer assigned to partitions: {}", (Object) partitions);
        
        // Test with maps (common for consumer metadata)
        logger.debug("Consumer metrics: lag={}, processed={}, errors={}", 
                    50L, 1000L, 0L);
        
        logger.info("✓ Parameter formatting test completed");
    }
    
    private static void testMDCContextLogging() {
        logger.info("Testing MDC context preservation in Kafka operations");
        
        // Set up typical Kafka operation context
        MDC.put("operation", "message-processing");
        MDC.put("topic", "user-events");
        MDC.put("consumer-group", "analytics-service");
        MDC.put("thread", Thread.currentThread().getName());
        
        try {
            logger.info("Starting message batch processing");
            
            // Simulate processing multiple messages
            for (int i = 0; i < 5; i++) {
                MDC.put("message-id", "msg-" + i);
                MDC.put("offset", String.valueOf(i * 100));
                
                logger.debug("Processing message from Kafka");
                
                // Simulate some processing work
                Thread.sleep(10);
                
                logger.trace("Message processing completed");
                
                MDC.remove("message-id");
                MDC.remove("offset");
            }
            
            logger.info("Message batch processing completed");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Message processing interrupted", e);
        } finally {
            MDC.remove("operation");
            MDC.remove("topic");
            MDC.remove("consumer-group");
            MDC.remove("thread");
        }
        
        logger.info("✓ MDC context test completed");
    }
    
    private static void testErrorLogging() {
        logger.info("Testing exception logging with Kafka scenarios");
        
        try {
            // Simulate Kafka connection error
            throw new RuntimeException("Connection to Kafka broker failed");
        } catch (Exception e) {
            logger.error("Kafka connection error", e);
        }
        
        try {
            // Simulate serialization error
            throw new IllegalArgumentException("Message serialization failed: invalid format");
        } catch (Exception e) {
            logger.error("Message serialization error: topic={}, key={}", "user-events", "user-123", e);
        }
        
        try {
            // Simulate timeout error
            throw new RuntimeException("Consumer poll timeout exceeded");
        } catch (Exception e) {
            logger.warn("Consumer timeout warning: group={}, timeout={}ms", "analytics-service", 30000, e);
        }
        
        logger.info("✓ Exception logging test completed");
    }
    
    private static void testHighVolumeLogging() {
        logger.info("Testing high-volume logging performance");
        
        int messageCount = 5000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < messageCount; i++) {
            // Mix of log levels for realistic Kafka application logging
            if (i % 1000 == 0) {
                logger.info("High-volume test progress: {}/{} messages", i, messageCount);
            } else if (i % 100 == 0) {
                logger.debug("Processing checkpoint: {} messages completed", i);
            } else {
                // Simulate typical Kafka trace logging
                logger.trace("Message {}: topic=high-volume-topic, partition={}, offset={}, key=hv-key-{}", 
                           i, i % 3, i * 10, i);
            }
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double messagesPerSecond = messageCount / (durationMs / 1000);
        
        logger.info("High-volume logging performance: {} messages in {:.2f} ms ({:.0f} msg/s)", 
                   messageCount, durationMs, messagesPerSecond);
        
        // Validate performance
        if (messagesPerSecond > 10000) {
            logger.info("✓ High-volume logging test passed: excellent performance");
        } else if (messagesPerSecond > 5000) {
            logger.info("✓ High-volume logging test passed: good performance");
        } else {
            logger.warn("High-volume logging performance below optimal: {:.0f} msg/s", messagesPerSecond);
        }
    }
}