package com.log4rich.integration.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Kafka Integration Application for testing SLF4J → log4j2 → log4Rich bridge.
 * 
 * This application demonstrates:
 * - High-volume logging from Kafka producers and consumers
 * - SLF4J parameter formatting with Kafka metadata
 * - MDC context preservation across Kafka operations
 * - Performance validation with realistic messaging workloads
 */
public class KafkaIntegrationApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaIntegrationApplication.class);
    
    public static void main(String[] args) {
        MDC.put("application", "kafka-integration");
        MDC.put("startTime", String.valueOf(System.currentTimeMillis()));
        
        try {
            logger.info("=== Kafka Integration Application Starting ===");
            logger.info("Testing SLF4J → log4j2 → log4Rich bridge with Kafka messaging");
            
            // Parse command line arguments
            KafkaConfig config = parseArguments(args);
            logger.info("Configuration: brokerUrl={}, messageCount={}, batchSize={}", 
                       config.getBrokerUrl(), config.getMessageCount(), config.getBatchSize());
            
            // Initialize Kafka components
            KafkaMessageProducer producer = new KafkaMessageProducer(config);
            KafkaMessageConsumer consumer = new KafkaMessageConsumer(config);
            KafkaStreamsProcessor streamsProcessor = new KafkaStreamsProcessor(config);
            
            logger.info("Kafka components initialized successfully");
            
            // Test basic producer/consumer functionality
            testBasicMessaging(producer, consumer, config);
            
            // Test high-volume messaging
            testHighVolumeMessaging(producer, consumer, config);
            
            // Test Kafka Streams processing
            testStreamProcessing(streamsProcessor, producer, config);
            
            // Test concurrent operations
            testConcurrentOperations(producer, consumer, config);
            
            // Performance validation
            validatePerformance(producer, consumer, config);
            
            logger.info("=== Kafka Integration Test Completed Successfully ===");
            
        } catch (Exception e) {
            logger.error("Kafka integration test failed", e);
            System.exit(1);
        } finally {
            MDC.clear();
        }
    }
    
    private static KafkaConfig parseArguments(String[] args) {
        String brokerUrl = "localhost:9092";
        int messageCount = 1000;
        int batchSize = 100;
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--broker.url":
                    if (i + 1 < args.length) brokerUrl = args[++i];
                    break;
                case "--message.count":
                    if (i + 1 < args.length) messageCount = Integer.parseInt(args[++i]);
                    break;
                case "--batch.size":
                    if (i + 1 < args.length) batchSize = Integer.parseInt(args[++i]);
                    break;
                default:
                    logger.warn("Unknown argument: {}", args[i]);
            }
        }
        
        return new KafkaConfig(brokerUrl, messageCount, batchSize);
    }
    
    private static void testBasicMessaging(KafkaMessageProducer producer, 
                                         KafkaMessageConsumer consumer, 
                                         KafkaConfig config) {
        logger.info("Testing basic Kafka messaging functionality");
        
        MDC.put("test-phase", "basic-messaging");
        
        try {
            // Start consumer
            consumer.start();
            
            // Send test messages
            int testMessages = 10;
            for (int i = 0; i < testMessages; i++) {
                String message = "Basic test message " + i;
                producer.sendMessage("test-topic", "key-" + i, message);
                logger.debug("Sent message {}: {}", i, message);
            }
            
            // Wait for consumption
            Thread.sleep(2000);
            
            logger.info("Basic messaging test completed: {} messages sent", testMessages);
            
        } catch (Exception e) {
            logger.error("Basic messaging test failed", e);
            throw new RuntimeException(e);
        } finally {
            MDC.remove("test-phase");
        }
    }
    
    private static void testHighVolumeMessaging(KafkaMessageProducer producer, 
                                              KafkaMessageConsumer consumer, 
                                              KafkaConfig config) {
        logger.info("Testing high-volume Kafka messaging");
        
        MDC.put("test-phase", "high-volume");
        MDC.put("target-messages", String.valueOf(config.getMessageCount()));
        
        try {
            long startTime = System.nanoTime();
            
            // Send high volume of messages
            for (int i = 0; i < config.getMessageCount(); i++) {
                String message = String.format("High-volume message %d at %d", i, System.currentTimeMillis());
                producer.sendMessage("high-volume-topic", "hv-key-" + i, message);
                
                if (i % 1000 == 0 && i > 0) {
                    logger.info("High-volume progress: {} messages sent", i);
                }
            }
            
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            double messagesPerSecond = config.getMessageCount() / (durationMs / 1000);
            
            logger.info("High-volume messaging completed: {} messages in {:.2f} ms ({:.0f} msg/s)", 
                       config.getMessageCount(), durationMs, messagesPerSecond);
            
            // Wait for consumption
            Thread.sleep(5000);
            
        } catch (Exception e) {
            logger.error("High-volume messaging test failed", e);
            throw new RuntimeException(e);
        } finally {
            MDC.remove("test-phase");
            MDC.remove("target-messages");
        }
    }
    
    private static void testStreamProcessing(KafkaStreamsProcessor streamsProcessor, 
                                           KafkaMessageProducer producer, 
                                           KafkaConfig config) {
        logger.info("Testing Kafka Streams processing");
        
        MDC.put("test-phase", "streams-processing");
        
        try {
            // Start streams processor
            streamsProcessor.start();
            
            // Send messages for stream processing
            int streamMessages = 500;
            for (int i = 0; i < streamMessages; i++) {
                String message = String.format("{\"id\":%d,\"value\":%d,\"timestamp\":%d}", 
                                              i, i * 10, System.currentTimeMillis());
                producer.sendMessage("input-stream-topic", "stream-key-" + i, message);
                
                if (i % 100 == 0) {
                    logger.debug("Stream processing: {} messages sent to input topic", i);
                }
            }
            
            // Wait for stream processing
            Thread.sleep(3000);
            
            logger.info("Kafka Streams processing test completed: {} messages processed", streamMessages);
            
        } catch (Exception e) {
            logger.error("Kafka Streams processing test failed", e);
            throw new RuntimeException(e);
        } finally {
            streamsProcessor.stop();
            MDC.remove("test-phase");
        }
    }
    
    private static void testConcurrentOperations(KafkaMessageProducer producer, 
                                               KafkaMessageConsumer consumer, 
                                               KafkaConfig config) {
        logger.info("Testing concurrent Kafka operations");
        
        MDC.put("test-phase", "concurrent-operations");
        
        try {
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
                            String message = String.format("Concurrent message from thread %d: %d", threadId, i);
                            producer.sendMessage("concurrent-topic", "concurrent-key-" + threadId + "-" + i, message);
                            
                            if (i % 50 == 0) {
                                logger.debug("Thread {} sent {} messages", threadId, i);
                            }
                        }
                        
                        logger.info("Thread {} completed: {} messages sent", threadId, messagesPerThread);
                        
                    } catch (Exception e) {
                        logger.error("Concurrent operation failed for thread {}", threadId, e);
                    } finally {
                        MDC.remove("thread-id");
                        MDC.remove("thread-name");
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
            
            logger.info("Concurrent operations completed: {} threads, {} total messages, " +
                       "{:.2f} seconds, {:.0f} msg/s", 
                       threadCount, totalMessages, durationSeconds, messagesPerSecond);
            
        } catch (Exception e) {
            logger.error("Concurrent operations test failed", e);
            throw new RuntimeException(e);
        } finally {
            MDC.remove("test-phase");
        }
    }
    
    private static void validatePerformance(KafkaMessageProducer producer, 
                                          KafkaMessageConsumer consumer, 
                                          KafkaConfig config) {
        logger.info("Validating Kafka integration performance");
        
        MDC.put("test-phase", "performance-validation");
        
        try {
            int performanceMessages = 10000;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < performanceMessages; i++) {
                logger.trace("Performance validation message {} with Kafka metadata: topic={}, partition={}, offset={}", 
                           i, "performance-topic", i % 3, i);
            }
            
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            double messagesPerSecond = performanceMessages / (durationMs / 1000);
            
            logger.info("Performance validation results: {} log messages in {:.2f} ms ({:.0f} msg/s)", 
                       performanceMessages, durationMs, messagesPerSecond);
            
            // Validate minimum performance threshold
            if (messagesPerSecond < 5000) {
                logger.warn("Performance below threshold: {:.0f} msg/s (minimum: 5000)", messagesPerSecond);
            } else {
                logger.info("Performance validation passed: {:.0f} msg/s", messagesPerSecond);
            }
            
        } finally {
            MDC.remove("test-phase");
        }
    }
}