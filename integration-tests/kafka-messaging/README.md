# Kafka Messaging Integration Test

This module tests the SLF4J → log4j2 → log4Rich bridge integration with Apache Kafka messaging applications.

## Test Coverage

### Logging Framework Chain
```
Kafka Application (uses SLF4J)
    ↓
SLF4J API (slf4j-api-1.7.36.jar)
    ↓
log4j2 SLF4J Binding (log4j-slf4j-impl-2.20.0.jar)
    ↓
log4j2 API (our bridge implementation)
    ↓
log4Rich Backend
```

### Components Tested

1. **Kafka Producer Operations**
   - Message sending with SLF4J logging
   - Batch operations logging
   - Asynchronous and synchronous send logging
   - Error handling and exception logging
   - Performance monitoring

2. **Kafka Consumer Operations**
   - Message consumption logging
   - Batch processing logging
   - Consumer group coordination logging
   - Offset management logging
   - Error recovery logging

3. **Kafka Streams Processing**
   - Stream topology logging
   - Message transformation logging
   - Aggregation operations logging
   - State store operations logging
   - Error handling in streams

4. **High-Volume Scenarios**
   - Performance testing with thousands of messages
   - Concurrent producer/consumer operations
   - Memory usage optimization
   - Throughput measurement

5. **Real-World Integration**
   - Testcontainers for embedded Kafka testing
   - Multiple topic scenarios
   - Different serialization formats
   - Production-like configurations

### Features Validated

#### SLF4J Features
- ✅ All log levels (TRACE, DEBUG, INFO, WARN, ERROR)
- ✅ Parameter formatting with {} placeholders
- ✅ Exception logging with Kafka-specific exceptions
- ✅ MDC (Mapped Diagnostic Context) with message metadata
- ✅ Logger hierarchy and levels

#### Kafka Integration
- ✅ Producer callback logging
- ✅ Consumer poll loop logging
- ✅ Kafka Streams processor logging
- ✅ Error handling and retry logging
- ✅ Performance metrics logging

#### Performance Characteristics
- ✅ High-throughput message logging
- ✅ Concurrent producer/consumer logging
- ✅ Memory-efficient logging operations
- ✅ Minimal impact on Kafka performance

## Running the Tests

### Prerequisites
```bash
# Ensure log4Rich is available (install to local Maven repo)
cd ../../log4Rich
mvn clean install

# Ensure log4j2-log4Rich bridge is built
cd ../log4j2-log4Rich
mvn clean install

# Docker must be running for Testcontainers
docker --version
```

### Run Integration Tests
```bash
# Run all tests (requires Docker for Testcontainers)
mvn clean test

# Run specific test
mvn test -Dtest=KafkaIntegrationTest

# Run with debug logging
mvn test -Dlog4rich.level=DEBUG

# Run performance tests only
mvn test -Dtest=*PerformanceTest
```

### Run Application
```bash
# Start with embedded Kafka (requires Docker)
mvn exec:java -Dexec.mainClass="com.log4rich.integration.kafka.KafkaIntegrationApplication"

# Start with external Kafka
mvn exec:java -Dexec.mainClass="com.log4rich.integration.kafka.KafkaIntegrationApplication" \
  -Dexec.args="--broker.url localhost:9092 --message.count 10000"
```

## Configuration

### Application Configuration
- `log4rich.properties`: log4Rich logging configuration optimized for Kafka

### Logging Configuration
The application uses log4Rich configuration that:
- Logs to both console and file with high-volume settings
- Includes MDC context for message tracing
- Sets appropriate levels for Kafka client libraries
- Enables async logging for performance
- Configures larger buffer sizes for high throughput

### Key Configuration Points

#### Excluding Conflicting Logging
```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

#### Adding Bridge Dependencies
```xml
<!-- SLF4J to log4j2 bridge -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
</dependency>

<!-- Our log4j2 to log4Rich bridge -->
<dependency>
    <groupId>com.log4rich</groupId>
    <artifactId>log4j2-log4Rich</artifactId>
</dependency>
```

#### High-Performance Logging Configuration
```properties
# Async logging for high throughput
log4rich.performance.async=true
log4rich.performance.bufferSize=16384
log4rich.performance.flushInterval=1000

# Kafka-specific logger levels
log4rich.logger.org.apache.kafka=INFO
log4rich.logger.org.apache.kafka.clients=WARN
log4rich.logger.org.apache.kafka.streams=INFO
```

## Expected Results

### Successful Test Output
```
2024-01-15 10:30:45.123 [main] INFO KafkaIntegrationApplication [kafka-test] [test-topic] [] - Starting Kafka Integration Test
2024-01-15 10:30:45.125 [main] INFO KafkaMessageProducer [msg-123] [high-volume-topic] [] - Message sent: partition=0, offset=12345
2024-01-15 10:30:45.126 [consumer-thread] DEBUG KafkaMessageConsumer [msg-456] [high-volume-topic] [] - Processing message: key=test-key
...
2024-01-15 10:30:47.456 [main] INFO KafkaIntegrationTest [] [] [] - ✓ All Kafka integration tests passed
```

### Performance Benchmarks
Expected performance characteristics:
- **Message Throughput**: > 50,000 messages/second
- **Logging Throughput**: > 10,000 log messages/second
- **Memory Overhead**: < 15% additional heap usage
- **CPU Overhead**: < 10% additional CPU usage
- **Latency Impact**: < 1ms additional latency per message

### Validation Checklist
- ✅ Kafka clients start without errors
- ✅ Producer sends messages successfully
- ✅ Consumer receives messages correctly
- ✅ Kafka Streams processes messages
- ✅ All logging appears in console and file
- ✅ MDC context flows through operations
- ✅ Exception stack traces are complete
- ✅ Performance meets minimum thresholds
- ✅ No memory leaks during extended runs
- ✅ Concurrent operations work correctly

## Test Scenarios

### High-Volume Messaging
```java
// Test high-throughput message production and consumption
for (int i = 0; i < 10000; i++) {
    producer.sendMessage("high-volume-topic", "key-" + i, "message-" + i);
    logger.debug("Message {} sent to partition {}", i, i % partitionCount);
}
```

### Concurrent Operations
```java
// Test concurrent producers and consumers with logging
ExecutorService executor = Executors.newFixedThreadPool(10);
for (int i = 0; i < 10; i++) {
    executor.submit(() -> {
        MDC.put("thread-id", Thread.currentThread().getName());
        producer.sendBatch("concurrent-topic", "thread-key", "message", 1000);
    });
}
```

### Stream Processing
```java
// Test Kafka Streams with comprehensive logging
KStream<String, String> stream = builder.stream("input-topic");
stream
    .peek((key, value) -> logger.debug("Processing message: key={}", key))
    .filter((key, value) -> {
        boolean valid = isValid(value);
        logger.trace("Message validation: key={}, valid={}", key, valid);
        return valid;
    })
    .to("output-topic");
```

## Troubleshooting

### Common Issues

**Issue**: Tests fail with Docker connection error
**Solution**: Ensure Docker is running and accessible

**Issue**: Kafka clients fail to connect
**Solution**: Check Testcontainers Kafka setup and port mappings

**Issue**: High memory usage during tests
**Solution**: Adjust log4rich buffer settings and async configuration

**Issue**: Poor performance in high-volume tests
**Solution**: Enable async logging and increase buffer sizes

### Debug Mode

Enable debug logging for Kafka operations:
```properties
log4rich.logger.com.log4rich.integration.kafka=TRACE
log4rich.logger.org.apache.kafka.clients.producer=DEBUG
log4rich.logger.org.apache.kafka.clients.consumer=DEBUG
```

### Logs Location
- **Console**: Standard output with INFO+ messages
- **File**: `logs/kafka-integration.log` with DEBUG+ messages
- **Kafka Operations**: Include message IDs, topics, and partitions

## Integration Validation

This test proves that:

1. **Kafka SLF4J Integration Works**: Kafka clients use SLF4J, which successfully delegates to our log4j2 bridge, which then uses log4Rich
2. **High-Volume Performance**: The bridge handles high-throughput Kafka workloads with acceptable overhead
3. **Context Preservation**: MDC context flows correctly through async Kafka operations
4. **Error Handling**: Kafka-specific exceptions are logged properly with full context
5. **Production Readiness**: Performance and stability meet enterprise Kafka deployment requirements

This validates that existing Kafka applications can adopt the log4j2-log4Rich bridge for improved logging performance while maintaining full compatibility with Kafka's SLF4J usage patterns.

## Message Flow Example

```
Producer → Kafka Cluster → Consumer
    ↓           ↓           ↓
SLF4J      SLF4J       SLF4J
    ↓           ↓           ↓
log4j2     log4j2      log4j2
    ↓           ↓           ↓
log4Rich   log4Rich    log4Rich

All components log through the same bridge chain:
- Message production metrics
- Cluster coordination events  
- Message consumption metrics
- Stream processing operations
- Error handling and retries
```