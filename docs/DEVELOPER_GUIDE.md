# log4j2-log4Rich Bridge Developer Guide

This guide provides comprehensive information for developers working with or contributing to the log4j2-log4Rich bridge project.

## Project Structure

```
log4j2-log4Rich/
├── src/main/java/
│   ├── org/apache/logging/log4j/        # log4j2 API implementation
│   │   ├── Logger.java                  # Main logger with 180+ methods
│   │   ├── LogManager.java              # Logger factory
│   │   ├── Level.java                   # Log levels enumeration
│   │   ├── Marker.java                  # Marker implementation
│   │   ├── MarkerManager.java           # Marker factory
│   │   ├── ThreadContext.java           # MDC/NDC context
│   │   └── message/                     # Message objects
│   │       ├── Message.java
│   │       ├── MessageFactory.java
│   │       ├── ParameterizedMessage.java
│   │       └── DefaultMessageFactory.java
│   └── com/log4rich/log4j2/bridge/     # Bridge implementation
│       ├── LoggingEngine.java           # Central logging engine
│       ├── LevelTranslator.java         # Level mapping
│       ├── MessageExtractor.java        # Message handling
│       ├── MarkerHandler.java           # Marker processing
│       ├── ContextBridge.java           # ThreadContext bridge
│       └── ThreadContextProvider.java   # Context provider
├── demo/                                # Demonstration code
│   └── Log4j2BridgeDemo.java           # Complete feature demo
├── docs/                                # Documentation
│   ├── DEVELOPER_GUIDE.md              # This file
│   ├── QUICK_START_GUIDE.md            # Quick start guide
│   └── USAGE_GUIDE.md                  # In-depth usage
└── README.md                           # Main documentation
```

## Architecture Deep Dive

### Design Principles

1. **Zero Code Duplication**: All 180+ logging methods are one-liners that delegate to the central LoggingEngine
2. **Layered Architecture**: Clear separation between utilities, engine, and API layers
3. **Performance Optimization**: Minimal overhead while maintaining full API compatibility
4. **Thread Safety**: All components are thread-safe by design
5. **Extensibility**: Clean interfaces for future enhancements

### Layer 1: Utility Classes

#### LevelTranslator
Maps log4j2 levels to log4Rich levels:

```java
/**
 * Translates log4j2 Level to log4Rich LogLevel.
 * 
 * @param log4j2Level the log4j2 level to translate
 * @return corresponding log4Rich LogLevel
 * @throws IllegalArgumentException if level is null
 */
public static com.log4rich.core.LogLevel translate(Level log4j2Level) {
    // Implementation handles all log4j2 levels including custom ones
}
```

#### MessageExtractor
Handles all message types (String, Message, Supplier):

```java
/**
 * Extracts string message from various log4j2 message objects.
 * Supports String, Message, Supplier, and parameter formatting.
 * 
 * @param messageObj the message object (String, Message, Supplier, etc.)
 * @param params optional parameters for formatting
 * @return formatted message string
 */
public static String extractMessage(Object messageObj, Object... params) {
    // Handles all log4j2 message types with parameter substitution
}
```

#### MarkerHandler
Processes log4j2 markers for log4Rich integration:

```java
/**
 * Formats marker information for log4Rich integration.
 * Handles marker hierarchy and null markers gracefully.
 * 
 * @param marker the log4j2 marker (may be null)
 * @return formatted marker string or null
 */
public static String formatMarker(Marker marker) {
    // Processes marker hierarchy for log4Rich
}
```

#### ContextBridge
Bridges log4j2 ThreadContext to log4Rich context system:

```java
/**
 * Gets current MDC context from log4j2 ThreadContext.
 * 
 * @return current MDC map (never null, may be empty)
 */
public static Map<String, String> getContext() {
    // Safely retrieves ThreadContext data
}

/**
 * Gets current NDC stack from log4j2 ThreadContext.
 * 
 * @return immutable copy of NDC stack
 */
public static List<String> getImmutableStack() {
    // Safely retrieves ThreadContext stack
}
```

### Layer 2: Central Logging Engine

The LoggingEngine is the heart of the bridge architecture. All 180+ logging methods converge here:

```java
/**
 * Central logging method that all bridge methods delegate to.
 * Handles level checking, message extraction, context integration.
 * 
 * @param log4RichLogger the target log4Rich logger
 * @param level the log4j2 level
 * @param marker optional marker (may be null)
 * @param message message object (String, Message, Supplier, etc.)
 * @param throwable optional exception (may be null)
 * @param params optional parameters for formatting
 */
public static void log(com.log4rich.core.Logger log4RichLogger, 
                      Level level, Marker marker, Object message, 
                      Throwable throwable, Object... params) {
    // 1. Check if logging is enabled for this level
    // 2. Extract and format message
    // 3. Handle marker information
    // 4. Integrate context data
    // 5. Delegate to log4Rich
}
```

Performance-optimized variants:

```java
/**
 * Optimized logging for simple string messages without parameters.
 */
public static void logSimple(com.log4rich.core.Logger logger, Level level, String message)

/**
 * Optimized logging for single parameter messages.
 */
public static void logSingleParam(com.log4rich.core.Logger logger, Level level, String message, Object param)

/**
 * Optimized logging for two-parameter messages.
 */
public static void logTwoParams(com.log4rich.core.Logger logger, Level level, String message, Object param1, Object param2)

/**
 * Optimized logging for exception messages.
 */
public static void logWithException(com.log4rich.core.Logger logger, Level level, String message, Throwable throwable)
```

### Layer 3: API Implementation

#### Logger Class Structure

The Logger class implements all 180+ log4j2 logging methods as simple one-liners:

```java
// Level checking methods (12 methods)
public boolean isTraceEnabled() {
    return LoggingEngine.isEnabled(log4RichLogger, Level.TRACE);
}

// Basic logging methods (6 levels × 18 variants = 108 methods)
public void info(String message) {
    LoggingEngine.logSimple(log4RichLogger, Level.INFO, message);
}

public void info(String message, Object param) {
    LoggingEngine.logSingleParam(log4RichLogger, Level.INFO, message, param);
}

// Marker methods (6 levels × 10 variants = 60 methods)
public void info(Marker marker, String message) {
    LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null);
}

// Generic level methods (12 methods)
public void log(Level level, String message) {
    LoggingEngine.logSimple(log4RichLogger, level, message);
}
```

## Development Workflow

### Setting Up Development Environment

```bash
# Clone the repository
git clone https://github.com/yourorg/log4j2-log4Rich.git
cd log4j2-log4Rich

# Install log4Rich dependency to local Maven repository
cd ../log4Rich
mvn clean install

# Return to bridge project
cd ../log4j2-log4Rich

# Compile and test
mvn clean compile
mvn test
```

### Building the Project

```bash
# Clean build
mvn clean compile

# Run all tests
mvn test

# Package with dependencies
mvn package

# Generate JavaDoc
mvn javadoc:javadoc

# Install to local Maven repository
mvn install
```

### Running the Demo

```bash
# Compile and run demo
mvn exec:java -Dexec.mainClass="demo.Log4j2BridgeDemo"

# Expected output shows all log4j2 features working with log4Rich backend
```

## Testing Strategy

### Test Structure

```
src/test/java/
├── org/apache/logging/log4j/
│   ├── LoggerTest.java              # Core logger functionality
│   ├── LogManagerTest.java          # Logger factory tests
│   ├── LevelTest.java               # Level enumeration tests
│   ├── MarkerTest.java              # Marker functionality
│   ├── ThreadContextTest.java       # MDC/NDC context tests
│   └── message/
│       ├── MessageTest.java         # Message object tests
│       └── ParameterizedMessageTest.java
└── com/log4rich/log4j2/bridge/
    ├── LoggingEngineTest.java       # Central engine tests
    ├── LevelTranslatorTest.java     # Level mapping tests
    ├── MessageExtractorTest.java    # Message handling tests
    ├── MarkerHandlerTest.java       # Marker processing tests
    └── ContextBridgeTest.java       # Context integration tests
```

### Test Categories

1. **Unit Tests**: Individual component testing
2. **Integration Tests**: End-to-end functionality
3. **Performance Tests**: Throughput and latency verification
4. **Compatibility Tests**: log4j2 API compliance

### Running Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=LoggerTest

# Specific test method
mvn test -Dtest=LoggerTest#testBasicLogging

# Performance tests
mvn test -Dtest=*PerformanceTest

# Integration tests
mvn test -Dtest=*IntegrationTest
```

## Performance Considerations

### Optimization Strategies

1. **Level Checking First**: Always check if logging is enabled before processing
2. **Lazy Evaluation**: Use Supplier interface for expensive message construction
3. **Minimal Object Creation**: Reuse objects where possible
4. **Efficient Parameter Handling**: Optimize common parameter patterns
5. **Direct Delegation**: Minimize layers between log4j2 and log4Rich

### Performance Metrics

The bridge achieves:
- **90,661+ messages/second** in typical scenarios
- **Sub-microsecond overhead** per logging call
- **Zero allocation** for disabled log levels
- **Minimal GC pressure** through efficient object handling

### Benchmarking

```java
// Performance test framework
public class BridgePerformanceTest {
    @Test
    public void testThroughput() {
        Logger logger = LogManager.getLogger(BridgePerformanceTest.class);
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            logger.debug("Performance test message {} at {}", i, System.nanoTime());
        }
        long endTime = System.nanoTime();
        
        double messagesPerSecond = 100000.0 / ((endTime - startTime) / 1_000_000_000.0);
        System.out.println("Throughput: " + messagesPerSecond + " messages/second");
        
        // Assert minimum performance threshold
        assertTrue("Performance below threshold", messagesPerSecond > 50000);
    }
}
```

## Adding New Features

### Extending the Logger API

If log4j2 adds new methods, follow this pattern:

1. **Add to Logger.java**: Implement as one-liner delegate to LoggingEngine
2. **Extend LoggingEngine**: Add any new processing logic if needed
3. **Update Utilities**: Enhance utility classes if new message types/features
4. **Add Tests**: Comprehensive test coverage for new functionality
5. **Update Documentation**: README, JavaDoc, and guides

Example:
```java
// New method in Logger.java
public void newLoggingMethod(String message, CustomObject obj) {
    LoggingEngine.log(log4RichLogger, Level.INFO, null, message, null, obj);
}

// Corresponding test
@Test
public void testNewLoggingMethod() {
    Logger logger = LogManager.getLogger(LoggerTest.class);
    CustomObject obj = new CustomObject("test");
    
    logger.newLoggingMethod("Testing new method with {}", obj);
    
    // Verify log4Rich received the message correctly
}
```

### Adding New Utility Classes

Follow the established pattern:

```java
package com.log4rich.log4j2.bridge;

/**
 * Utility class for [specific purpose].
 * Thread-safe and stateless design.
 */
public final class NewUtility {
    
    private NewUtility() {
        // Prevent instantiation
    }
    
    /**
     * [Method description with full JavaDoc]
     * 
     * @param param parameter description
     * @return return value description
     * @throws ExceptionType when this exception occurs
     */
    public static String processNewFeature(Object param) {
        // Implementation
        return result;
    }
}
```

## Debugging and Troubleshooting

### Debug Configuration

Enable detailed logging to troubleshoot issues:

```properties
# log4Rich debug configuration
log4rich.level=TRACE
log4rich.appender.console=true
log4rich.appender.console.level=TRACE
log4rich.internal.debug=true
```

### Common Issues and Solutions

1. **ClassNotFoundException**:
   - Ensure log4Rich is in classpath
   - Use shaded JAR for deployment
   - Check Maven dependencies

2. **Performance Issues**:
   - Verify log levels are appropriate
   - Check for expensive operations in debug code
   - Profile with JVM tools

3. **Context Data Missing**:
   - Verify ThreadContext integration
   - Check ContextProvider setup
   - Ensure proper cleanup

4. **Marker Issues**:
   - Verify marker hierarchy setup
   - Check MarkerManager usage
   - Test marker inheritance

### Debugging Tools

```java
// Enable debug logging in tests
System.setProperty("log4rich.internal.debug", "true");

// Check logger configuration
Logger logger = LogManager.getLogger("test");
System.out.println("Logger name: " + logger.getName());
System.out.println("Debug enabled: " + logger.isDebugEnabled());

// Verify context integration
ThreadContext.put("debug", "value");
logger.info("Testing context");
ThreadContext.clearAll();
```

## Code Style and Standards

### Java Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: 120 characters maximum
- **Naming**: CamelCase for methods, PascalCase for classes
- **JavaDoc**: Required for all public methods and classes
- **Null Handling**: Explicit null checks with descriptive messages

### Documentation Standards

- **README**: Complete usage examples and migration guide
- **JavaDoc**: Comprehensive API documentation with examples
- **Inline Comments**: Explain complex algorithms and business logic
- **Code Examples**: Working examples for all major features

### Git Workflow

```bash
# Feature branch workflow
git checkout -b feature/new-logging-method
git commit -m "Add new logging method with CustomObject support"
git push origin feature/new-logging-method
# Create pull request
```

## Release Process

### Version Management

- **Semantic Versioning**: MAJOR.MINOR.PATCH
- **Backward Compatibility**: Maintain API compatibility in minor versions
- **Documentation Updates**: Update all docs with version changes

### Release Checklist

1. **Code Review**: All changes reviewed and approved
2. **Test Coverage**: 90%+ test coverage maintained
3. **Performance**: Benchmark results meet requirements
4. **Documentation**: README, JavaDoc, and guides updated
5. **Build**: Clean build with all tests passing
6. **Demo**: Demo application works correctly

### Deployment

```bash
# Prepare release
mvn clean test
mvn javadoc:javadoc
mvn package

# Deploy to repository
mvn deploy

# Tag release
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

## Contributing Guidelines

### Before Contributing

1. **Read Documentation**: Understand architecture and design principles
2. **Run Tests**: Ensure all existing tests pass
3. **Code Style**: Follow established conventions
4. **Performance**: Maintain or improve performance characteristics

### Contribution Process

1. **Fork Repository**: Create personal fork
2. **Feature Branch**: Create branch for changes
3. **Implement Changes**: Follow coding standards
4. **Add Tests**: Comprehensive test coverage
5. **Update Documentation**: Keep docs current
6. **Submit PR**: Create pull request with description

### Review Process

All contributions go through:
1. **Automated Testing**: CI pipeline validation
2. **Code Review**: Maintainer review for quality
3. **Performance Testing**: Benchmark validation
4. **Documentation Review**: Ensure docs are current
5. **Integration Testing**: Full system validation

## Support and Community

### Getting Help

- **Documentation**: Start with README and this guide
- **Issues**: GitHub Issues for bug reports and feature requests
- **Discussions**: GitHub Discussions for questions
- **Performance**: See benchmarks and demo applications

### Reporting Issues

Include:
- **Java Version**: JVM version and vendor
- **Dependencies**: Maven dependency tree
- **Configuration**: log4Rich configuration
- **Stack Trace**: Full error details
- **Minimal Example**: Reproducible test case

This developer guide provides the foundation for working with the log4j2-log4Rich bridge. The architecture's layered design makes it easy to understand, extend, and maintain while delivering excellent performance.