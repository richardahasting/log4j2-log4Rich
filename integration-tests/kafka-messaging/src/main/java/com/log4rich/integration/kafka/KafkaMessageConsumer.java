package com.log4rich.integration.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Kafka message consumer that demonstrates SLF4J logging integration.
 * 
 * This class validates:
 * - SLF4J logging from Kafka consumer operations
 * - Parameter formatting with consumer metadata
 * - Exception logging with consumer failures
 * - Performance logging for high-volume consumption
 */
public class KafkaMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    
    private final KafkaConsumer<String, String> consumer;
    private final KafkaConfig config;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong messagesConsumed = new AtomicLong(0);
    private Thread consumerThread;
    
    public KafkaMessageConsumer(KafkaConfig config) {
        this.config = config;
        this.consumer = createConsumer();
        
        logger.info("Kafka consumer initialized with broker: {}", config.getBrokerUrl());
    }
    
    private KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBrokerUrl());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "integration-test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        
        return new KafkaConsumer<>(props);
    }
    
    /**
     * Start consuming messages from all test topics.
     */
    public void start() {
        if (running.compareAndSet(false, true)) {
            logger.info("Starting Kafka consumer");
            
            // Subscribe to all test topics
            String[] topics = {
                "test-topic", 
                "high-volume-topic", 
                "concurrent-topic", 
                "performance-topic",
                "input-stream-topic",
                "output-stream-topic"
            };
            
            consumer.subscribe(Arrays.asList(topics));
            logger.info("Subscribed to topics: {}", Arrays.toString(topics));
            
            consumerThread = new Thread(this::consumeMessages);
            consumerThread.setName("kafka-consumer-thread");
            consumerThread.start();
            
            logger.info("Kafka consumer started successfully");
        } else {
            logger.warn("Kafka consumer is already running");
        }
    }
    
    /**
     * Stop consuming messages and close consumer.
     */
    public void stop() {
        if (running.compareAndSet(true, false)) {
            logger.info("Stopping Kafka consumer");
            
            if (consumerThread != null) {
                try {
                    consumerThread.join(5000);
                    logger.info("Consumer thread stopped");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Interrupted while waiting for consumer thread to stop", e);
                }
            }
            
            try {
                consumer.close(Duration.ofSeconds(5));
                logger.info("Kafka consumer closed successfully");
            } catch (Exception e) {
                logger.error("Error closing Kafka consumer", e);
            }
            
            logger.info("Total messages consumed: {}", messagesConsumed.get());
        }
    }
    
    private void consumeMessages() {
        MDC.put("thread-role", "consumer");
        MDC.put("consumer-group", "integration-test-group");
        
        try {
            logger.info("Consumer thread started");
            
            while (running.get()) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    
                    if (!records.isEmpty()) {
                        processRecords(records);
                    }
                    
                } catch (Exception e) {
                    logger.error("Error during message consumption", e);
                    
                    // Continue consuming after error
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.info("Consumer thread interrupted during error recovery");
                        break;
                    }
                }
            }
            
        } finally {
            logger.info("Consumer thread finished. Total messages consumed: {}", messagesConsumed.get());
            MDC.clear();
        }
    }
    
    private void processRecords(ConsumerRecords<String, String> records) {
        long batchStartTime = System.nanoTime();
        int recordCount = records.count();
        
        logger.debug("Processing batch of {} messages", recordCount);
        
        for (ConsumerRecord<String, String> record : records) {
            processRecord(record);
        }
        
        long batchEndTime = System.nanoTime();
        double batchDurationMs = (batchEndTime - batchStartTime) / 1_000_000.0;
        double recordsPerSecond = recordCount / (batchDurationMs / 1000);
        
        messagesConsumed.addAndGet(recordCount);
        
        logger.debug("Batch processed: {} records in {:.2f}ms ({:.0f} records/s)", 
                    recordCount, batchDurationMs, recordsPerSecond);
        
        // Periodic progress logging
        long totalConsumed = messagesConsumed.get();
        if (totalConsumed % 1000 == 0) {
            logger.info("Consumer progress: {} total messages consumed", totalConsumed);
        }
    }
    
    private void processRecord(ConsumerRecord<String, String> record) {
        String messageId = generateMessageId();
        MDC.put("messageId", messageId);
        MDC.put("topic", record.topic());
        MDC.put("partition", String.valueOf(record.partition()));
        MDC.put("offset", String.valueOf(record.offset()));
        
        try {
            // Log message metadata with SLF4J parameter formatting
            logger.trace("Processing message: topic={}, partition={}, offset={}, key={}, valueSize={} bytes, timestamp={}", 
                        record.topic(), record.partition(), record.offset(), 
                        record.key(), record.value().length(), record.timestamp());
            
            // Simulate message processing
            processMessageContent(record);
            
            // Test different log levels based on message content
            if (record.value().contains("error")) {
                logger.error("Error message detected: key={}, content={}", record.key(), record.value());
            } else if (record.value().contains("warning")) {
                logger.warn("Warning message detected: key={}, topic={}", record.key(), record.topic());
            } else if (record.value().contains("high-volume")) {
                logger.debug("High-volume message processed: offset={}", record.offset());
            } else {
                logger.trace("Regular message processed: messageId={}", messageId);
            }
            
        } catch (Exception e) {
            logger.error("Failed to process message: topic={}, partition={}, offset={}, key={}", 
                        record.topic(), record.partition(), record.offset(), record.key(), e);
        } finally {
            MDC.remove("messageId");
            MDC.remove("topic");
            MDC.remove("partition");
            MDC.remove("offset");
        }
    }
    
    private void processMessageContent(ConsumerRecord<String, String> record) {
        // Simulate different types of message processing
        switch (record.topic()) {
            case "test-topic":
                processTestMessage(record);
                break;
            case "high-volume-topic":
                processHighVolumeMessage(record);
                break;
            case "concurrent-topic":
                processConcurrentMessage(record);
                break;
            case "performance-topic":
                processPerformanceMessage(record);
                break;
            default:
                processGenericMessage(record);
        }
    }
    
    private void processTestMessage(ConsumerRecord<String, String> record) {
        logger.debug("Processing test message: key={}, size={} bytes", record.key(), record.value().length());
        
        // Simulate basic processing
        if (record.value().startsWith("Basic test message")) {
            logger.info("Basic test message processed successfully: {}", record.key());
        }
    }
    
    private void processHighVolumeMessage(ConsumerRecord<String, String> record) {
        // Only log every 100th message to avoid overwhelming logs
        if (record.offset() % 100 == 0) {
            logger.debug("High-volume message checkpoint: offset={}, key={}", record.offset(), record.key());
        }
    }
    
    private void processConcurrentMessage(ConsumerRecord<String, String> record) {
        // Extract thread info from message key
        if (record.key() != null && record.key().contains("concurrent-key")) {
            String[] parts = record.key().split("-");
            if (parts.length >= 3) {
                String threadId = parts[2];
                logger.trace("Processing concurrent message from thread {}: offset={}", threadId, record.offset());
            }
        }
    }
    
    private void processPerformanceMessage(ConsumerRecord<String, String> record) {
        // Minimal processing for performance testing
        logger.trace("Performance message: offset={}", record.offset());
    }
    
    private void processGenericMessage(ConsumerRecord<String, String> record) {
        logger.trace("Processing generic message: topic={}, offset={}", record.topic(), record.offset());
    }
    
    /**
     * Get the number of messages consumed so far.
     */
    public long getMessagesConsumed() {
        return messagesConsumed.get();
    }
    
    /**
     * Check if consumer is currently running.
     */
    public boolean isRunning() {
        return running.get();
    }
    
    private String generateMessageId() {
        return "consumer-msg-" + System.currentTimeMillis() + "-" + Math.random();
    }
}