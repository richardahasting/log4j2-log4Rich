# Spring Boot Web Integration Test

This module tests the SLF4J → log4j2 → log4Rich bridge integration with Spring Boot applications.

## Test Coverage

### Logging Framework Chain
```
Spring Boot Application (uses SLF4J)
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

1. **Spring Boot Startup**
   - Application context initialization
   - Auto-configuration with custom logging
   - Component scanning and bean creation

2. **Web Layer**
   - REST Controller logging
   - Request/response logging
   - Error handling and logging
   - MDC context in web requests

3. **Service Layer**
   - Business logic logging
   - Synchronous operations
   - Asynchronous operations with CompletableFuture
   - Bulk logging operations

4. **Data Layer**
   - Spring Data JPA logging
   - Hibernate SQL logging (configured to WARN)
   - Database connection logging

5. **Performance Testing**
   - High-volume logging scenarios
   - Concurrent logging from multiple threads
   - Logging performance measurement

### Features Validated

#### SLF4J Features
- ✅ All log levels (TRACE, DEBUG, INFO, WARN, ERROR)
- ✅ Parameter formatting with {} placeholders
- ✅ Exception logging with stack traces
- ✅ MDC (Mapped Diagnostic Context)
- ✅ Logger hierarchy and levels

#### Spring Boot Integration
- ✅ Auto-configuration works with custom logging
- ✅ Web MVC logging integration
- ✅ Async processing logging
- ✅ Error handling and logging
- ✅ Application lifecycle logging

#### Performance Characteristics
- ✅ Acceptable logging overhead
- ✅ Thread-safe concurrent logging
- ✅ High-throughput logging scenarios
- ✅ Memory usage optimization

## Running the Tests

### Prerequisites
```bash
# Ensure log4Rich is available (install to local Maven repo)
cd ../../log4Rich
mvn clean install

# Ensure log4j2-log4Rich bridge is built
cd ../log4j2-log4Rich
mvn clean install
```

### Run Integration Tests
```bash
# Run all tests
mvn clean test

# Run specific test
mvn test -Dtest=SpringBootIntegrationTest

# Run with debug logging
mvn test -Dspring.profiles.active=debug

# Run performance tests only
mvn test -Dtest=*PerformanceTest
```

### Run Application
```bash
# Start the Spring Boot application
mvn spring-boot:run

# Application will be available at http://localhost:8080
```

### Test Endpoints

Once the application is running, you can test the endpoints:

```bash
# Test basic endpoint
curl http://localhost:8080/api/test/hello?name=TestUser

# Test data processing
curl -X POST http://localhost:8080/api/test/data \
  -H "Content-Type: application/json" \
  -d '{"name":"test","value":123,"flag":true}'

# Test performance endpoint
curl http://localhost:8080/api/test/performance?messageCount=1000

# Test error handling
curl http://localhost:8080/api/test/hello?name=error
```

## Configuration

### Application Configuration
- `application.properties`: Spring Boot configuration
- `log4rich.properties`: log4Rich logging configuration

### Logging Configuration
The application uses log4Rich configuration that:
- Logs to both console and file
- Includes MDC context in log format
- Sets appropriate levels for different components
- Optimizes for performance testing

### Key Configuration Points

#### Excluding Default Logging
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
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

## Expected Results

### Successful Test Output
```
2024-01-15 10:30:45.123 [main] INFO SpringBootIntegrationApplication [req-123] [] - Starting Spring Boot Integration Test
2024-01-15 10:30:45.125 [main] INFO SpringBootIntegrationApplication [req-123] [] - Testing SLF4J → log4j2 → log4Rich bridge chain
...
2024-01-15 10:30:47.456 [main] INFO SpringBootIntegrationTest [] [] - ✓ All integration tests passed
```

### Performance Benchmarks
Expected performance characteristics:
- **Startup Time**: < 5 seconds
- **Logging Throughput**: > 10,000 messages/second
- **Memory Overhead**: < 10% additional heap usage
- **CPU Overhead**: < 5% additional CPU usage

### Validation Checklist
- ✅ Application starts without errors
- ✅ All HTTP endpoints respond correctly
- ✅ Logging appears in both console and file
- ✅ MDC context appears in log messages
- ✅ Exception stack traces are complete
- ✅ Performance meets minimum thresholds
- ✅ No memory leaks during extended runs
- ✅ Concurrent logging works correctly

## Troubleshooting

### Common Issues

**Issue**: Application fails to start
**Solution**: Check that log4Rich dependency is available and bridge is built

**Issue**: No log output appears
**Solution**: Verify log4rich.properties configuration and file paths

**Issue**: Performance tests fail
**Solution**: Check system resources and adjust thresholds if needed

**Issue**: MDC context missing
**Solution**: Verify MDC configuration in log4rich.properties

### Debug Mode

Enable debug logging:
```properties
log4rich.logger.com.log4rich.integration.springboot=TRACE
log4rich.logger.org.springframework.web=DEBUG
```

### Logs Location
- **Console**: Standard output with INFO+ messages
- **File**: `logs/spring-boot-integration.log` with DEBUG+ messages
- **Spring Boot**: Application logs include request IDs and operation IDs

## Integration Validation

This test proves that:

1. **SLF4J Integration Works**: Spring Boot uses SLF4J, which successfully delegates to our log4j2 bridge, which then uses log4Rich
2. **Performance Is Acceptable**: The bridge overhead is minimal and performance meets requirements
3. **Features Are Preserved**: All SLF4J features (MDC, parameters, levels) work correctly
4. **Real-World Compatibility**: Common Spring Boot patterns and libraries work without issues
5. **Thread Safety**: Concurrent logging from multiple threads works correctly

This validates that existing Spring Boot applications can adopt the log4j2-log4Rich bridge with minimal configuration changes and gain the performance benefits of log4Rich.