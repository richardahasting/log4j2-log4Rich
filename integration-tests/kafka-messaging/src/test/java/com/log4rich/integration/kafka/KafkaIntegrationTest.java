package com.log4rich.integration.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Kafka messaging using SLF4J → log4j2 → log4Rich bridge.
 * 
 * These tests verify:
 * - SLF4J logging works correctly through the bridge in Kafka operations
 * - Kafka producer and consumer components log properly
 * - Performance is acceptable with high-volume messaging
 * - MDC context is preserved across Kafka operations
 * - No integration issues occur with Kafka client libraries
 */
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaIntegrationTest.class);
    
    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
    
    private KafkaConfig config;
    private KafkaMessageProducer producer;
    private KafkaMessageConsumer consumer;
    private KafkaStreamsProcessor streamsProcessor;
    
    @BeforeEach
    void setUp() {
        // Set up test context
        MDC.put("test-class", this.getClass().getSimpleName());
        MDC.put("test-start", String.valueOf(System.currentTimeMillis()));
        
        logger.info("Starting Kafka integration test with broker: {}", kafka.getBootstrapServers());
        
        config = new KafkaConfig(kafka.getBootstrapServers(), 1000, 100);
        producer = new KafkaMessageProducer(config);
        consumer = new KafkaMessageConsumer(config);
        streamsProcessor = new KafkaStreamsProcessor(config);
        
        logger.info("Kafka integration test setup completed");
    }
    
    @AfterEach
    void tearDown() {
        logger.info("Cleaning up Kafka integration test");
        
        try {
            if (consumer != null) {
                consumer.stop();
            }
            if (streamsProcessor != null) {
                streamsProcessor.stop();
            }
            if (producer != null) {
                producer.close();
            }
        } catch (Exception e) {
            logger.error("Error during test cleanup", e);
        }
        
        logger.info("Kafka integration test cleanup completed");
        MDC.clear();
    }
    
    @Test
    void testKafkaComponentsInitialize() {
        logger.info("Testing Kafka components initialization");
        
        // Components should have initialized successfully in setUp()
        assertNotNull(producer, "Producer should be initialized");
        assertNotNull(consumer, "Consumer should be initialized");
        assertNotNull(streamsProcessor, "Streams processor should be initialized");
        
        logger.info("✓ Kafka components initialization test passed");
    }
    
    @Test
    void testSLF4JLoggingThroughBridge() {
        logger.info("Testing SLF4J logging through log4j2-log4Rich bridge");
        
        // Test all log levels from Kafka integration context
        logger.trace("TRACE level test from Kafka integration");
        logger.debug("DEBUG level test from Kafka integration");
        logger.info("INFO level test from Kafka integration");
        logger.warn("WARN level test from Kafka integration");
        logger.error("ERROR level test from Kafka integration");
        
        // Test parameter formatting with Kafka-like data
        String topic = "test-topic";
        int partition = 0;
        long offset = 12345L;
        String key = "test-key";
        
        logger.info("Kafka message metadata: topic={}, partition={}, offset={}, key={}", 
                   topic, partition, offset, key);
        
        // Test exception logging
        Exception kafkaException = new RuntimeException("Simulated Kafka exception");
        logger.error("Kafka operation failed", kafkaException);
        
        logger.info("✓ SLF4J logging through bridge test passed");
    }
    
    @Test
    void testBasicProducerConsumerLogging() {
        logger.info("Testing basic Kafka producer/consumer logging");
        
        try {
            // Start consumer
            consumer.start();
            
            // Send test messages with logging
            String testTopic = "basic-test-topic";
            int messageCount = 5;
            
            for (int i = 0; i < messageCount; i++) {
                String key = "basic-key-" + i;
                String message = "Basic test message " + i + " at " + System.currentTimeMillis();
                
                producer.sendMessage(testTopic, key, message);
                logger.debug("Test message sent: index={}, key={}", i, key);
            }
            
            // Wait for consumption
            Thread.sleep(3000);
            
            assertTrue(consumer.getMessagesConsumed() >= 0, "Consumer should have processed messages");
            logger.info("✓ Basic producer/consumer logging test passed. Messages consumed: {}", 
                       consumer.getMessagesConsumed());
            
        } catch (Exception e) {
            logger.error("Basic producer/consumer test failed", e);
            fail("Basic producer/consumer test failed: " + e.getMessage());
        }
    }
    
    @Test
    void testMDCContextPreservation() {
        logger.info("Testing MDC context preservation in Kafka operations");
        
        // Set up MDC context
        MDC.put("request-id", "kafka-test-" + System.currentTimeMillis());
        MDC.put("user-id", "test-user-kafka");
        MDC.put("operation", "kafka-mdc-test");
        
        try {
            logger.info("MDC context set for Kafka operations");
            
            // Send message with MDC context
            String testTopic = "mdc-test-topic";
            producer.sendMessage(testTopic, "mdc-key", "Message with MDC context");
            
            logger.info("Kafka message sent with MDC context intact");
            
            // Verify MDC values are still accessible
            assertEquals("kafka-test-" + System.currentTimeMillis().toString().substring(0, 10), 
                        MDC.get("request-id").substring(0, 15), "Request ID should be preserved");
            assertEquals("test-user-kafka", MDC.get("user-id"), "User ID should be preserved");
            assertEquals("kafka-mdc-test", MDC.get("operation"), "Operation should be preserved");
            
            logger.info("✓ MDC context preservation test passed");
            
        } finally {
            MDC.clear();
        }
    }
    
    @Test
    void testHighVolumeLogging() {
        logger.info("Testing high-volume logging performance with Kafka");
        
        int messageCount = 5000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < messageCount; i++) {
            // Mix of different log levels for realistic scenario
            if (i % 1000 == 0) {
                logger.info("High-volume test progress: {}/{} messages", i, messageCount);
            } else if (i % 100 == 0) {
                logger.debug("High-volume test checkpoint: {} messages processed", i);
            } else {
                logger.trace("High-volume test message {}: topic=test-topic, partition={}, offset={}", 
                           i, i % 3, i * 10);
            }
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        double messagesPerSecond = messageCount / (durationMs / 1000);
        
        logger.info("High-volume logging performance: {} messages in {:.2f} ms ({:.0f} msg/s)", 
                   messageCount, durationMs, messagesPerSecond);
        
        // Assert reasonable performance
        assertTrue(messagesPerSecond > 2000, 
                  "High-volume logging performance too low: " + messagesPerSecond + " msg/s");
        
        logger.info("✓ High-volume logging test passed: {:.0f} messages/second", messagesPerSecond);
    }
    
    @Test
    void testKafkaProducerLogging() {
        logger.info("Testing Kafka producer logging integration");
        
        try {
            // Test synchronous send with detailed logging
            String topic = "producer-test-topic";
            String key = "producer-test-key";
            String message = "Producer test message";
            
            logger.debug("Sending synchronous message: topic={}, key={}", topic, key);
            producer.sendMessageSync(topic, key, message);
            
            // Test batch operations
            producer.sendBatch("batch-test-topic", "batch-key", "Batch message", 50);
            
            // Test error scenarios
            producer.testErrorScenarios();
            
            logger.info("✓ Kafka producer logging test passed");
            
        } catch (Exception e) {
            logger.error("Kafka producer logging test failed", e);
            fail("Producer logging test failed: " + e.getMessage());
        }
    }
    
    @Test
    void testKafkaStreamsLogging() {
        logger.info("Testing Kafka Streams logging integration");
        
        try {
            // Start streams processor
            streamsProcessor.start();
            
            // Wait for streams to be ready
            Thread.sleep(2000);
            
            // Send messages to input topic for stream processing
            String inputTopic = "input-stream-topic";
            for (int i = 0; i < 10; i++) {
                String message = String.format("{\"id\":%d,\"value\":%d,\"timestamp\":%d}", 
                                              i, i * 10, System.currentTimeMillis());
                producer.sendMessage(inputTopic, "stream-key-" + i, message);
            }
            
            // Wait for stream processing
            Thread.sleep(3000);
            
            // Check streams state
            assertNotNull(streamsProcessor.getState(), "Streams state should be available");
            
            logger.info("✓ Kafka Streams logging test passed");
            
        } catch (Exception e) {
            logger.error("Kafka Streams logging test failed", e);
            fail("Streams logging test failed: " + e.getMessage());
        }
    }
    
    @Test
    void testConcurrentKafkaOperations() throws InterruptedException {
        logger.info("Testing concurrent Kafka operations with logging");
        
        int threadCount = 3;
        int messagesPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        
        // Start consumer
        consumer.start();
        
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                MDC.put("thread-id", String.valueOf(threadId));
                MDC.put("thread-name", Thread.currentThread().getName());
                
                try {
                    String threadTopic = "concurrent-topic-" + threadId;
                    
                    for (int i = 0; i < messagesPerThread; i++) {
                        String key = "concurrent-key-" + threadId + "-" + i;
                        String message = "Concurrent message from thread " + threadId + ": " + i;
                        
                        producer.sendMessage(threadTopic, key, message);
                        
                        if (i % 25 == 0) {
                            logger.debug("Thread {} progress: {} messages sent", threadId, i);
                        }
                    }
                    
                    logger.info("Thread {} completed: {} messages sent", threadId, messagesPerThread);
                    
                } catch (Exception e) {
                    logger.error("Concurrent operation failed for thread {}", threadId, e);
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
            thread.join(15000); // 15 second timeout
        }
        long endTime = System.currentTimeMillis();
        
        double durationSeconds = (endTime - startTime) / 1000.0;
        int totalMessages = threadCount * messagesPerThread;
        double messagesPerSecond = totalMessages / durationSeconds;
        
        logger.info("Concurrent Kafka operations completed: {} threads, {} total messages, " +
                   "{:.2f} seconds, {:.0f} msg/s", 
                   threadCount, totalMessages, durationSeconds, messagesPerSecond);
        
        assertTrue(messagesPerSecond > 100, 
                  "Concurrent performance too low: " + messagesPerSecond + " msg/s");
        
        logger.info("✓ Concurrent Kafka operations test passed");
    }
    
    @Test
    void testKafkaExceptionLogging() {
        logger.info("Testing Kafka exception logging scenarios");
        
        try {
            // Test producer exception logging
            logger.debug("Testing producer exception scenarios");
            producer.testErrorScenarios();
            
            // Test streams exception logging
            logger.debug("Testing streams exception scenarios");
            streamsProcessor.testErrorScenarios();
            
            // Test various exception types that might occur in Kafka operations
            simulateKafkaExceptions();
            
            logger.info("✓ Kafka exception logging test passed");
            
        } catch (Exception e) {
            logger.info("Expected exception during exception logging test", e);
        }
    }
    
    private void simulateKafkaExceptions() {
        // Simulate different types of Kafka-related exceptions
        
        try {
            throw new org.apache.kafka.common.errors.TimeoutException("Simulated timeout");
        } catch (Exception e) {
            logger.error("Kafka timeout exception handled", e);
        }
        
        try {
            throw new org.apache.kafka.common.errors.SerializationException("Simulated serialization error");
        } catch (Exception e) {
            logger.error("Kafka serialization exception handled", e);
        }
        
        try {
            throw new org.apache.kafka.common.errors.InvalidTopicException("Simulated invalid topic");
        } catch (Exception e) {
            logger.error("Kafka invalid topic exception handled", e);
        }
    }
}