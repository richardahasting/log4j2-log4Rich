package com.log4rich.integration.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Kafka message producer that demonstrates SLF4J logging integration.
 * 
 * This class validates:
 * - SLF4J logging from Kafka producer operations
 * - Parameter formatting with Kafka metadata
 * - Exception logging with producer failures
 * - Performance logging for high-volume scenarios
 */
public class KafkaMessageProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageProducer.class);
    
    private final KafkaProducer<String, String> producer;
    private final KafkaConfig config;
    
    public KafkaMessageProducer(KafkaConfig config) {
        this.config = config;
        this.producer = createProducer();
        
        logger.info("Kafka producer initialized with broker: {}", config.getBrokerUrl());
    }
    
    private KafkaProducer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBrokerUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        
        return new KafkaProducer<>(props);
    }
    
    /**
     * Send a message to Kafka topic with comprehensive logging.
     */
    public void sendMessage(String topic, String key, String message) {
        String messageId = generateMessageId();
        MDC.put("messageId", messageId);
        MDC.put("topic", topic);
        MDC.put("key", key);
        
        try {
            logger.debug("Sending message to Kafka: topic={}, key={}, size={} bytes", 
                        topic, key, message.length());
            
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
            
            long startTime = System.nanoTime();
            
            // Send message asynchronously with callback
            producer.send(record, (metadata, exception) -> {
                long endTime = System.nanoTime();
                double durationMs = (endTime - startTime) / 1_000_000.0;
                
                if (exception != null) {
                    logger.error("Failed to send message {}: topic={}, key={}, duration={:.2f}ms", 
                               messageId, topic, key, durationMs, exception);
                } else {
                    logger.debug("Message {} sent successfully: topic={}, partition={}, offset={}, duration={:.2f}ms", 
                               messageId, metadata.topic(), metadata.partition(), metadata.offset(), durationMs);
                    
                    // Test parameter formatting with various data types
                    logger.trace("Message metadata: topic={}, partition={}, offset={}, timestamp={}, serializedKeySize={}, serializedValueSize={}", 
                               metadata.topic(), metadata.partition(), metadata.offset(), 
                               metadata.timestamp(), metadata.serializedKeySize(), metadata.serializedValueSize());
                }
            });
            
        } catch (Exception e) {
            logger.error("Exception during message send: messageId={}, topic={}, key={}", 
                        messageId, topic, key, e);
            throw new RuntimeException("Failed to send message", e);
        } finally {
            MDC.remove("messageId");
            MDC.remove("topic");
            MDC.remove("key");
        }
    }
    
    /**
     * Send a message synchronously for testing purposes.
     */
    public RecordMetadata sendMessageSync(String topic, String key, String message) {
        String messageId = generateMessageId();
        MDC.put("messageId", messageId);
        MDC.put("topic", topic);
        MDC.put("key", key);
        MDC.put("sendType", "synchronous");
        
        try {
            logger.debug("Sending synchronous message: topic={}, key={}, messageId={}", topic, key, messageId);
            
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
            
            long startTime = System.nanoTime();
            RecordMetadata metadata = producer.send(record).get();
            long endTime = System.nanoTime();
            
            double durationMs = (endTime - startTime) / 1_000_000.0;
            
            logger.info("Synchronous message sent: messageId={}, topic={}, partition={}, offset={}, duration={:.2f}ms", 
                       messageId, metadata.topic(), metadata.partition(), metadata.offset(), durationMs);
            
            return metadata;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Synchronous send interrupted: messageId={}", messageId, e);
            throw new RuntimeException("Send interrupted", e);
        } catch (ExecutionException e) {
            logger.error("Synchronous send failed: messageId={}", messageId, e);
            throw new RuntimeException("Send failed", e);
        } finally {
            MDC.remove("messageId");
            MDC.remove("topic");
            MDC.remove("key");
            MDC.remove("sendType");
        }
    }
    
    /**
     * Send batch of messages for performance testing.
     */
    public void sendBatch(String topic, String keyPrefix, String messagePrefix, int batchSize) {
        String batchId = generateMessageId();
        MDC.put("batchId", batchId);
        MDC.put("batchSize", String.valueOf(batchSize));
        
        try {
            logger.info("Starting batch send: batchId={}, topic={}, batchSize={}", batchId, topic, batchSize);
            
            long startTime = System.nanoTime();
            
            for (int i = 0; i < batchSize; i++) {
                String key = keyPrefix + "-" + i;
                String message = messagePrefix + "-" + i + "-" + System.currentTimeMillis();
                
                sendMessage(topic, key, message);
                
                if (i % 100 == 0 && i > 0) {
                    logger.debug("Batch progress: {}/{} messages sent", i, batchSize);
                }
            }
            
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            double messagesPerSecond = batchSize / (durationMs / 1000);
            
            logger.info("Batch send completed: batchId={}, {} messages in {:.2f}ms ({:.0f} msg/s)", 
                       batchId, batchSize, durationMs, messagesPerSecond);
            
        } finally {
            MDC.remove("batchId");
            MDC.remove("batchSize");
        }
    }
    
    /**
     * Test error handling and logging.
     */
    public void testErrorScenarios() {
        logger.info("Testing producer error scenarios");
        
        try {
            // Test invalid topic
            sendMessage("", "test-key", "test message");
        } catch (Exception e) {
            logger.warn("Expected error for empty topic name", e);
        }
        
        try {
            // Test null key (valid scenario but worth logging)
            sendMessage("test-topic", null, "message with null key");
        } catch (Exception e) {
            logger.error("Unexpected error with null key", e);
        }
        
        try {
            // Test very large message
            String largeMessage = "x".repeat(1024 * 1024); // 1MB message
            sendMessage("test-topic", "large-key", largeMessage);
            logger.warn("Large message sent successfully: {} bytes", largeMessage.length());
        } catch (Exception e) {
            logger.info("Large message failed as expected", e);
        }
    }
    
    /**
     * Flush producer and close resources.
     */
    public void close() {
        try {
            logger.info("Flushing and closing Kafka producer");
            producer.flush();
            producer.close(Duration.ofSeconds(5));
            logger.info("Kafka producer closed successfully");
        } catch (Exception e) {
            logger.error("Error closing Kafka producer", e);
        }
    }
    
    private String generateMessageId() {
        return "msg-" + System.currentTimeMillis() + "-" + Math.random();
    }
}