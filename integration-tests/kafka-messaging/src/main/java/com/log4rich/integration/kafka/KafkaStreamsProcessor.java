package com.log4rich.integration.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.Properties;

/**
 * Kafka Streams processor that demonstrates SLF4J logging integration.
 * 
 * This class validates:
 * - SLF4J logging from Kafka Streams operations
 * - Parameter formatting with stream metadata
 * - Exception logging with stream processing failures
 * - Performance logging for stream transformations
 */
public class KafkaStreamsProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsProcessor.class);
    
    private final KafkaConfig config;
    private KafkaStreams streams;
    
    public KafkaStreamsProcessor(KafkaConfig config) {
        this.config = config;
        logger.info("Kafka Streams processor initialized with broker: {}", config.getBrokerUrl());
    }
    
    /**
     * Start the Kafka Streams processing topology.
     */
    public void start() {
        MDC.put("component", "kafka-streams");
        
        try {
            logger.info("Starting Kafka Streams processor");
            
            Properties props = createStreamsProperties();
            StreamsBuilder builder = new StreamsBuilder();
            
            // Build the processing topology
            buildTopology(builder);
            
            streams = new KafkaStreams(builder.build(), props);
            
            // Set up exception handlers
            streams.setUncaughtExceptionHandler(new StreamsUncaughtExceptionHandler() {
                @Override
                public StreamThreadExceptionResponse handle(Throwable exception) {
                    logger.error("Kafka Streams uncaught exception", exception);
                    return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
                }
            });
            
            streams.setStateListener((newState, oldState) -> {
                logger.info("Kafka Streams state transition: {} -> {}", oldState, newState);
                
                if (newState == KafkaStreams.State.ERROR) {
                    logger.error("Kafka Streams entered ERROR state");
                } else if (newState == KafkaStreams.State.RUNNING) {
                    logger.info("Kafka Streams is now RUNNING");
                }
            });
            
            streams.start();
            logger.info("Kafka Streams processor started successfully");
            
        } catch (Exception e) {
            logger.error("Failed to start Kafka Streams processor", e);
            throw new RuntimeException("Kafka Streams startup failed", e);
        } finally {
            MDC.remove("component");
        }
    }
    
    /**
     * Stop the Kafka Streams processor.
     */
    public void stop() {
        MDC.put("component", "kafka-streams");
        
        try {
            if (streams != null) {
                logger.info("Stopping Kafka Streams processor");
                streams.close(Duration.ofSeconds(10));
                logger.info("Kafka Streams processor stopped successfully");
            }
        } catch (Exception e) {
            logger.error("Error stopping Kafka Streams processor", e);
        } finally {
            MDC.remove("component");
        }
    }
    
    private Properties createStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "log4rich-integration-test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBrokerUrl());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0); // Disable caching for testing
        
        return props;
    }
    
    private void buildTopology(StreamsBuilder builder) {
        logger.info("Building Kafka Streams topology");
        
        // Input stream
        KStream<String, String> inputStream = builder.stream("input-stream-topic");
        
        // Log all incoming messages
        inputStream.peek((key, value) -> {
            MDC.put("stream-operation", "input-peek");
            MDC.put("key", key);
            try {
                logger.debug("Input stream message: key={}, valueSize={} bytes", key, value.length());
            } finally {
                MDC.remove("stream-operation");
                MDC.remove("key");
            }
        });
        
        // Filter messages (test conditional logging)
        KStream<String, String> filteredStream = inputStream.filter((key, value) -> {
            boolean shouldProcess = !value.contains("skip");
            
            MDC.put("stream-operation", "filter");
            MDC.put("key", key);
            try {
                if (shouldProcess) {
                    logger.trace("Message passed filter: key={}", key);
                } else {
                    logger.debug("Message filtered out: key={}, reason=contains 'skip'", key);
                }
            } finally {
                MDC.remove("stream-operation");
                MDC.remove("key");
            }
            
            return shouldProcess;
        });
        
        // Transform messages (test parameter formatting)
        KStream<String, String> transformedStream = filteredStream.mapValues((key, value) -> {
            MDC.put("stream-operation", "transform");
            MDC.put("key", key);
            try {
                String transformed = transformMessage(value);
                logger.trace("Message transformed: key={}, originalSize={}, transformedSize={}", 
                           key, value.length(), transformed.length());
                return transformed;
            } catch (Exception e) {
                logger.error("Transformation failed: key={}, value={}", key, value, e);
                return "ERROR: " + e.getMessage();
            } finally {
                MDC.remove("stream-operation");
                MDC.remove("key");
            }
        });
        
        // Branch streams based on content (test multiple logging paths)
        KStream<String, String>[] branches = transformedStream.branch(
            (key, value) -> {
                boolean isHighPriority = value.contains("priority");
                if (isHighPriority) {
                    logger.debug("High priority message detected: key={}", key);
                }
                return isHighPriority;
            },
            (key, value) -> {
                boolean isNormal = !value.contains("ERROR");
                if (!isNormal) {
                    logger.warn("Error message in stream: key={}", key);
                }
                return isNormal;
            }
        );
        
        // Process high priority messages
        if (branches.length > 0) {
            branches[0]
                .peek((key, value) -> {
                    MDC.put("stream-branch", "high-priority");
                    MDC.put("key", key);
                    try {
                        logger.info("Processing high priority message: key={}", key);
                    } finally {
                        MDC.remove("stream-branch");
                        MDC.remove("key");
                    }
                })
                .to("output-stream-topic-priority");
        }
        
        // Process normal messages
        if (branches.length > 1) {
            branches[1]
                .peek((key, value) -> {
                    MDC.put("stream-branch", "normal");
                    MDC.put("key", key);
                    try {
                        logger.trace("Processing normal message: key={}", key);
                    } finally {
                        MDC.remove("stream-branch");
                        MDC.remove("key");
                    }
                })
                .to("output-stream-topic");
        }
        
        // Aggregate for performance testing
        inputStream
            .groupByKey()
            .count()
            .toStream()
            .peek((key, count) -> {
                MDC.put("stream-operation", "count-aggregate");
                MDC.put("key", key);
                try {
                    logger.debug("Message count for key {}: {}", key, count);
                } finally {
                    MDC.remove("stream-operation");
                    MDC.remove("key");
                }
            });
        
        logger.info("Kafka Streams topology built successfully");
    }
    
    private String transformMessage(String input) {
        // Simple transformation that we can log
        if (input.startsWith("{")) {
            // Assume JSON, add timestamp
            return input.replace("}", ",\"processed_at\":" + System.currentTimeMillis() + "}");
        } else {
            // Add prefix to regular messages
            return "PROCESSED: " + input;
        }
    }
    
    /**
     * Test exception scenarios in stream processing.
     */
    public void testErrorScenarios() {
        logger.info("Testing stream processing error scenarios");
        
        // This would be called by unit tests to validate error handling
        try {
            // Simulate a processing error
            throw new RuntimeException("Simulated stream processing error");
        } catch (Exception e) {
            logger.error("Stream processing error handled correctly", e);
        }
    }
    
    /**
     * Get current state of the streams processor.
     */
    public KafkaStreams.State getState() {
        if (streams != null) {
            KafkaStreams.State state = streams.state();
            logger.debug("Current Kafka Streams state: {}", state);
            return state;
        } else {
            logger.debug("Kafka Streams not initialized");
            return KafkaStreams.State.NOT_RUNNING;
        }
    }
}