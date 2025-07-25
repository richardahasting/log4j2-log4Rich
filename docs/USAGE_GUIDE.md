# log4j2-log4Rich Bridge Usage Guide

This comprehensive guide covers all features and capabilities of the log4j2-log4Rich bridge, from basic usage to advanced configurations and performance optimization.

## Table of Contents

1. [Core Logging Features](#core-logging-features)
2. [Message Formatting](#message-formatting)
3. [Markers and Hierarchy](#markers-and-hierarchy)
4. [Thread Context (MDC/NDC)](#thread-context-mdcndc)
5. [Lambda Expressions and Suppliers](#lambda-expressions-and-suppliers)
6. [Message Objects](#message-objects)
7. [Level Management](#level-management)
8. [Performance Optimization](#performance-optimization)
9. [Configuration Reference](#configuration-reference)
10. [Advanced Usage](#advanced-usage)
11. [Integration Patterns](#integration-patterns)
12. [Troubleshooting](#troubleshooting)

## Core Logging Features

### Basic Logging Methods

The bridge supports all log4j2 logging levels with identical syntax:

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicLoggingExample {
    private static final Logger logger = LogManager.getLogger(BasicLoggingExample.class);
    
    public void demonstrateBasicLogging() {
        logger.trace("Detailed trace information");
        logger.debug("Debug information for developers");
        logger.info("General application flow information");
        logger.warn("Warning - something might be wrong");
        logger.error("Error occurred - needs attention");
        logger.fatal("Fatal error - application may crash");
    }
}
```

### Logger Creation Patterns

Multiple ways to create loggers:

```java
// By class (recommended)
private static final Logger logger = LogManager.getLogger(MyClass.class);

// By name
private static final Logger logger = LogManager.getLogger("com.myapp.service");

// Root logger
private static final Logger rootLogger = LogManager.getRootLogger();

// Runtime logger creation
public void dynamicLoggerExample(String category) {
    Logger dynamicLogger = LogManager.getLogger(category);
    dynamicLogger.info("Dynamic logger for category: {}", category);
}
```

### Exception Logging

Comprehensive exception handling support:

```java
public class ExceptionLoggingExample {
    private static final Logger logger = LogManager.getLogger(ExceptionLoggingExample.class);
    
    public void demonstrateExceptionLogging() {
        try {
            riskyOperation();
        } catch (IOException e) {
            // Exception only
            logger.error("File operation failed", e);
            
            // Exception with parameters
            logger.error("Failed to process file {} for user {}", fileName, userId, e);
            
            // Different log levels with exceptions
            logger.warn("Retrying operation due to error", e);
            logger.fatal("Critical system failure", e);
        }
    }
    
    private void riskyOperation() throws IOException {
        throw new IOException("Simulated file error");
    }
}
```

## Message Formatting

### Parameter Substitution

The bridge supports log4j2's `{}` placeholder syntax:

```java
public class MessageFormattingExample {
    private static final Logger logger = LogManager.getLogger(MessageFormattingExample.class);
    
    public void demonstrateParameterFormatting() {
        String user = "alice";
        int count = 42;
        double value = 3.14159;
        
        // Single parameter
        logger.info("User {} logged in", user);
        
        // Multiple parameters
        logger.info("User {} processed {} items worth ${}", user, count, value);
        
        // Parameter with exception
        try {
            processUser(user);
        } catch (Exception e) {
            logger.error("Failed to process user {}", user, e);
        }
    }
    
    public void demonstrateComplexFormatting() {
        // Array parameters
        String[] items = {"apple", "banana", "cherry"};
        logger.info("Processing items: {}", (Object) items);
        
        // Object parameters
        User user = new User("bob", 25);
        logger.debug("User details: {}", user);
        
        // Null parameter handling
        String nullValue = null;
        logger.info("Null value handled gracefully: {}", nullValue);
        
        // Mixed types
        logger.info("Mixed types: string={}, int={}, double={}, boolean={}", 
                   "test", 123, 45.67, true);
    }
}
```

### Parameter Edge Cases

The bridge handles various edge cases correctly:

```java
public void demonstrateEdgeCases() {
    // More placeholders than parameters
    logger.info("Too many placeholders: {} {} {}", "param1");
    
    // More parameters than placeholders
    logger.info("Too few placeholders: {}", "param1", "param2", "param3");
    
    // Special characters in parameters
    logger.info("Special chars: {}", "String with {} braces");
    
    // Large numbers
    logger.info("Large number: {}", Long.MAX_VALUE);
    
    // Performance with many parameters
    logger.debug("Many params: {} {} {} {} {} {} {} {} {} {}", 
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
}
```

## Markers and Hierarchy

### Basic Marker Usage

Markers provide a way to categorize and filter log messages:

```java
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class MarkerExample {
    private static final Logger logger = LogManager.getLogger(MarkerExample.class);
    
    // Create markers
    private static final Marker PERFORMANCE = MarkerManager.getMarker("PERFORMANCE");
    private static final Marker SECURITY = MarkerManager.getMarker("SECURITY");
    private static final Marker AUDIT = MarkerManager.getMarker("AUDIT");
    
    public void demonstrateBasicMarkers() {
        logger.info(PERFORMANCE, "Operation completed in {} ms", 123);
        logger.warn(SECURITY, "Failed login attempt for user {}", "baduser");
        logger.info(AUDIT, "User {} accessed resource {}", "alice", "/admin");
    }
}
```

### Marker Hierarchy

Create marker hierarchies for flexible filtering:

```java
public class MarkerHierarchyExample {
    private static final Logger logger = LogManager.getLogger(MarkerHierarchyExample.class);
    
    public void setupMarkerHierarchy() {
        // Create parent markers
        Marker performance = MarkerManager.getMarker("PERFORMANCE");
        Marker database = MarkerManager.getMarker("DATABASE");
        Marker security = MarkerManager.getMarker("SECURITY");
        
        // Create child markers with parents
        Marker dbPerformance = MarkerManager.getMarker("DB_PERFORMANCE");
        dbPerformance.addParents(performance, database);
        
        Marker securityAudit = MarkerManager.getMarker("SECURITY_AUDIT");
        securityAudit.addParents(security);
        
        // Use hierarchical markers
        logger.info(dbPerformance, "Query executed in {} ms", 45);
        logger.warn(securityAudit, "Suspicious activity detected");
        
        // Test hierarchy
        logger.debug("DB_PERFORMANCE is instance of PERFORMANCE: {}", 
                    dbPerformance.isInstanceOf("PERFORMANCE"));
        logger.debug("DB_PERFORMANCE is instance of DATABASE: {}", 
                    dbPerformance.isInstanceOf("DATABASE"));
    }
}
```

### Marker Management

Advanced marker operations:

```java
public class MarkerManagementExample {
    private static final Logger logger = LogManager.getLogger(MarkerManagementExample.class);
    
    public void demonstrateMarkerManagement() {
        // Check if marker exists
        boolean exists = MarkerManager.exists("PERFORMANCE");
        logger.debug("PERFORMANCE marker exists: {}", exists);
        
        // Get all markers
        Marker[] allMarkers = MarkerManager.getMarkers();
        logger.info("Total markers: {}", allMarkers.length);
        
        // Marker operations
        Marker parent = MarkerManager.getMarker("PARENT");
        Marker child = MarkerManager.getMarker("CHILD");
        
        // Add parent
        child.addParents(parent);
        logger.debug("Child has parents: {}", child.hasParents());
        
        // Remove parent
        boolean removed = child.removeParent(parent);
        logger.debug("Parent removed: {}", removed);
        
        // Get parents
        Marker[] parents = child.getParents();
        logger.debug("Child parent count: {}", parents.length);
    }
}
```

## Thread Context (MDC/NDC)

### Mapped Diagnostic Context (MDC)

MDC provides a way to associate contextual information with log messages:

```java
import org.apache.logging.log4j.ThreadContext;

public class MDCExample {
    private static final Logger logger = LogManager.getLogger(MDCExample.class);
    
    public void demonstrateMDC() {
        // Set context values
        ThreadContext.put("userId", "12345");
        ThreadContext.put("sessionId", "abcdef-123456");
        ThreadContext.put("requestId", "req-" + System.currentTimeMillis());
        
        logger.info("Processing user request");
        
        // Nested operations maintain context
        processOrder();
        
        // Update context
        ThreadContext.put("phase", "validation");
        logger.debug("Validating user input");
        
        // Remove specific key
        ThreadContext.remove("phase");
        
        // Clear all context
        ThreadContext.clearAll();
    }
    
    private void processOrder() {
        // Context is automatically available
        logger.info("Processing order for current user");
        
        // Add operation-specific context
        ThreadContext.put("operation", "processOrder");
        logger.debug("Order processing started");
        ThreadContext.remove("operation");
    }
    
    public void demonstrateMDCOperations() {
        // Set values
        ThreadContext.put("key1", "value1");
        ThreadContext.put("key2", "value2");
        
        // Get specific value
        String value = ThreadContext.get("key1");
        logger.debug("Retrieved value: {}", value);
        
        // Get all context
        Map<String, String> context = ThreadContext.getContext();
        logger.info("Context size: {}", context.size());
        
        // Check if empty
        boolean isEmpty = ThreadContext.isEmpty();
        logger.debug("Context is empty: {}", isEmpty);
        
        // Clear only map (preserve stack)
        ThreadContext.clearMap();
    }
}
```

### Nested Diagnostic Context (NDC)

NDC provides a stack-based context for nested operations:

```java
public class NDCExample {
    private static final Logger logger = LogManager.getLogger(NDCExample.class);
    
    public void demonstrateNDC() {
        // Push context onto stack
        ThreadContext.push("UserService");
        logger.info("Entered user service");
        
        try {
            authenticateUser("alice");
        } finally {
            // Always pop in finally block
            ThreadContext.pop();
        }
    }
    
    private void authenticateUser(String username) {
        ThreadContext.push("authenticateUser");
        logger.info("Authenticating user: {}", username);
        
        try {
            validateCredentials(username);
        } finally {
            ThreadContext.pop();
        }
    }
    
    private void validateCredentials(String username) {
        ThreadContext.push("validateCredentials");
        logger.debug("Validating credentials for: {}", username);
        
        // Simulate some work
        logger.trace("Checking password policy");
        logger.trace("Verifying against database");
        
        ThreadContext.pop();
    }
    
    public void demonstrateNDCOperations() {
        // Build nested context
        ThreadContext.push("level1");
        ThreadContext.push("level2");
        ThreadContext.push("level3");
        
        // Check depth
        int depth = ThreadContext.getDepth();
        logger.debug("Current depth: {}", depth);
        
        // Peek at top without removing
        String top = ThreadContext.peek();
        logger.debug("Top of stack: {}", top);
        
        // Pop and check return value
        String popped = ThreadContext.pop();
        logger.debug("Popped value: {}", popped);
        
        // Get immutable copy of stack
        List<String> stack = ThreadContext.getImmutableStack();
        logger.debug("Stack contents: {}", stack);
        
        // Clear entire context (map and stack)
        ThreadContext.clearAll();
    }
}
```

### Context Integration Patterns

Real-world context usage patterns:

```java
public class ContextIntegrationExample {
    private static final Logger logger = LogManager.getLogger(ContextIntegrationExample.class);
    
    // Web request context pattern
    public void handleWebRequest(HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        String userId = getUserId(request);
        String sessionId = request.getSession().getId();
        
        ThreadContext.put("requestId", requestId);
        ThreadContext.put("userId", userId);
        ThreadContext.put("sessionId", sessionId);
        ThreadContext.push("WebRequestHandler");
        
        try {
            logger.info("Processing web request");
            processRequest(request);
        } finally {
            ThreadContext.clearAll();  // Clean up after request
        }
    }
    
    // Database transaction context pattern
    public void executeTransaction(String transactionType) {
        String txId = "tx-" + System.currentTimeMillis();
        ThreadContext.put("transactionId", txId);
        ThreadContext.put("transactionType", transactionType);
        ThreadContext.push("DatabaseTransaction");
        
        try {
            logger.info("Starting database transaction");
            
            // Begin transaction
            ThreadContext.put("phase", "begin");
            logger.debug("Transaction begun");
            
            // Execute operations
            ThreadContext.put("phase", "execute");
            executeOperations();
            
            // Commit
            ThreadContext.put("phase", "commit");
            logger.info("Transaction committed successfully");
            
        } catch (Exception e) {
            ThreadContext.put("phase", "rollback");
            logger.error("Transaction failed, rolling back", e);
        } finally {
            ThreadContext.clearAll();
        }
    }
    
    // Background job context pattern
    public void processBackgroundJob(Job job) {
        ThreadContext.put("jobId", job.getId());
        ThreadContext.put("jobType", job.getType());
        ThreadContext.put("jobPriority", String.valueOf(job.getPriority()));
        ThreadContext.push("BackgroundJobProcessor");
        
        try {
            logger.info("Starting background job processing");
            
            for (int i = 0; i < job.getSteps().size(); i++) {
                ThreadContext.put("step", String.valueOf(i + 1));
                ThreadContext.put("stepName", job.getSteps().get(i).getName());
                
                logger.debug("Executing job step");
                executeJobStep(job.getSteps().get(i));
                
                ThreadContext.remove("stepName");
            }
            
            logger.info("Background job completed successfully");
            
        } finally {
            ThreadContext.clearAll();
        }
    }
}
```

## Lambda Expressions and Suppliers

### Lazy Message Evaluation

Use lambda expressions for expensive message construction:

```java
import java.util.function.Supplier;

public class LambdaExample {
    private static final Logger logger = LogManager.getLogger(LambdaExample.class);
    
    public void demonstrateLazyEvaluation() {
        // Lambda is only evaluated if DEBUG is enabled
        logger.debug(() -> "Expensive computation result: " + expensiveComputation());
        
        // Traditional approach - always evaluated
        logger.debug("Expensive computation result: " + expensiveComputation());
        
        // Lambda with multiple operations
        logger.trace(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Complex data: ");
            for (int i = 0; i < 1000; i++) {
                sb.append(i).append(" ");
            }
            return sb.toString();
        });
    }
    
    public void demonstrateLambdaWithException() {
        try {
            riskyOperation();
        } catch (Exception e) {
            // Lambda with exception
            logger.error(() -> "Error processing at " + System.currentTimeMillis(), e);
            
            // Lambda with complex error information
            logger.error(() -> "Detailed error info: " + gatherErrorDetails(), e);
        }
    }
    
    public void demonstrateMarkerWithLambda() {
        Marker performance = MarkerManager.getMarker("PERFORMANCE");
        
        // Marker with lambda
        logger.debug(performance, () -> "Performance data: " + gatherPerformanceData());
        
        // Marker with lambda and exception
        try {
            performanceTest();
        } catch (Exception e) {
            logger.error(performance, () -> "Performance test failed: " + getTestDetails(), e);
        }
    }
    
    private String expensiveComputation() {
        // Simulate expensive operation
        try {
            Thread.sleep(100);
            return "computed_value_" + System.nanoTime();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "interrupted";
        }
    }
    
    private String gatherErrorDetails() {
        return "Error details gathered at " + System.currentTimeMillis();
    }
    
    private String gatherPerformanceData() {
        return "CPU: 45%, Memory: 78%, Disk: 23%";
    }
}
```

### Performance Comparison

Compare performance between lambda and traditional approaches:

```java
public class PerformanceComparisonExample {
    private static final Logger logger = LogManager.getLogger(PerformanceComparisonExample.class);
    
    public void comparePerformance() {
        // When DEBUG is disabled, lambda approach is much faster
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 100000; i++) {
            // Lambda approach - not evaluated when DEBUG disabled
            logger.debug(() -> "Expensive operation " + expensiveString());
        }
        
        long lambdaTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < 100000; i++) {
            // Traditional approach - always evaluated
            logger.debug("Expensive operation " + expensiveString());
        }
        
        long traditionalTime = System.nanoTime() - startTime;
        
        logger.info("Lambda time: {} ms", lambdaTime / 1_000_000);
        logger.info("Traditional time: {} ms", traditionalTime / 1_000_000);
        logger.info("Performance improvement: {}x", traditionalTime / (double) lambdaTime);
    }
    
    private String expensiveString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("expensive_").append(i);
        }
        return sb.toString();
    }
}
```

## Message Objects

### ParameterizedMessage

Use log4j2's ParameterizedMessage for advanced formatting:

```java
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.Message;

public class MessageObjectExample {
    private static final Logger logger = LogManager.getLogger(MessageObjectExample.class);
    
    public void demonstrateParameterizedMessage() {
        // Create parameterized message
        ParameterizedMessage msg = new ParameterizedMessage(
            "User {} performed {} actions", "alice", 42);
        logger.info(msg);
        
        // Message with exception
        try {
            riskyOperation();
        } catch (Exception e) {
            ParameterizedMessage errorMsg = new ParameterizedMessage(
                "Operation {} failed for user {}", "backup", "bob", e);
            logger.error(errorMsg);
        }
    }
    
    public void demonstrateMessageInterface() {
        // Custom message implementation
        Message customMsg = new Message() {
            @Override
            public String getFormattedMessage() {
                return "Custom formatted message at " + System.currentTimeMillis();
            }
            
            @Override
            public String getFormat() {
                return "Custom format";
            }
            
            @Override
            public Object[] getParameters() {
                return new Object[]{"param1", "param2"};
            }
            
            @Override
            public Throwable getThrowable() {
                return null;
            }
        };
        
        logger.info(customMsg);
    }
    
    public void demonstrateMessageWithMarker() {
        Marker audit = MarkerManager.getMarker("AUDIT");
        
        ParameterizedMessage auditMsg = new ParameterizedMessage(
            "User {} accessed resource {} at {}", 
            "admin", "/secure-data", System.currentTimeMillis());
        
        logger.info(audit, auditMsg);
    }
}
```

### Custom Message Factory

Create custom message factories for specialized formatting:

```java
import org.apache.logging.log4j.message.MessageFactory;

public class CustomMessageFactoryExample {
    
    public void demonstrateCustomMessageFactory() {
        // Custom message factory
        MessageFactory customFactory = new MessageFactory() {
            @Override
            public Message newMessage(Object message) {
                return new ParameterizedMessage("[CUSTOM] " + message);
            }
            
            @Override
            public Message newMessage(String message) {
                return new ParameterizedMessage("[CUSTOM] " + message);
            }
            
            @Override
            public Message newMessage(String message, Object... params) {
                return new ParameterizedMessage("[CUSTOM] " + message, params);
            }
        };
        
        // Logger with custom message factory
        Logger customLogger = LogManager.getLogger("CustomLogger", customFactory);
        
        customLogger.info("This message will be prefixed");
        customLogger.info("User {} logged in", "alice");
    }
}
```

## Level Management

### Level Checking

Optimize performance with level checking:

```java
public class LevelCheckingExample {
    private static final Logger logger = LogManager.getLogger(LevelCheckingExample.class);
    
    public void demonstrateLevelChecking() {
        // Basic level checks
        if (logger.isTraceEnabled()) {
            logger.trace("Detailed trace: {}", gatherTraceInfo());
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Debug info: {}", calculateDebugData());
        }
        
        if (logger.isInfoEnabled()) {
            logger.info("Info message");
        }
        
        // Marker-specific level checks
        Marker performance = MarkerManager.getMarker("PERFORMANCE");
        if (logger.isDebugEnabled(performance)) {
            logger.debug(performance, "Performance data: {}", gatherPerformanceData());
        }
    }
    
    public void demonstrateGenericLevelChecking() {
        // Generic level checking
        if (logger.isEnabled(Level.ERROR)) {
            logger.error("Error level is enabled");
        }
        
        if (logger.isEnabled(Level.WARN)) {
            logger.warn("Warn level is enabled");
        }
        
        // Marker with generic level
        Marker security = MarkerManager.getMarker("SECURITY");
        if (logger.isEnabled(Level.INFO, security)) {
            logger.info(security, "Security info logging enabled");
        }
    }
    
    private String gatherTraceInfo() {
        return "Expensive trace information";
    }
    
    private String calculateDebugData() {
        return "Expensive debug calculation";
    }
    
    private String gatherPerformanceData() {
        return "Performance metrics";
    }
}
```

### Dynamic Level Changes

While log4Rich supports runtime level changes, the bridge provides access through configuration:

```java
public class DynamicLevelExample {
    private static final Logger logger = LogManager.getLogger(DynamicLevelExample.class);
    
    public void demonstrateLevelChecking() {
        // Test current levels
        logger.debug("Current debug enabled: {}", logger.isDebugEnabled());
        logger.info("Current info enabled: {}", logger.isInfoEnabled());
        logger.warn("Current warn enabled: {}", logger.isWarnEnabled());
        logger.error("Current error enabled: {}", logger.isErrorEnabled());
    }
    
    // Note: Dynamic level changes depend on log4Rich configuration
    // See log4Rich documentation for runtime configuration changes
}
```

## Performance Optimization

### Best Practices for High Performance

```java
public class PerformanceOptimizationExample {
    private static final Logger logger = LogManager.getLogger(PerformanceOptimizationExample.class);
    
    public void demonstrateOptimizations() {
        // 1. Use level checking for expensive operations
        if (logger.isDebugEnabled()) {
            logger.debug("Expensive debug: {}", expensiveOperation());
        }
        
        // 2. Use lambda suppliers for complex formatting
        logger.debug(() -> "Complex formatting: " + buildComplexString());
        
        // 3. Cache frequently used objects
        Marker cachedMarker = MarkerManager.getMarker("CACHED");
        for (int i = 0; i < 1000; i++) {
            logger.debug(cachedMarker, "Cached marker usage: {}", i);
        }
        
        // 4. Minimize parameter objects
        String userId = "user123";
        int count = 42;
        logger.info("User {} processed {} items", userId, count);  // Good
        
        // Avoid creating unnecessary objects
        // logger.info("User {} processed {} items", new User("user123"), new Count(42));  // Avoid
    }
    
    public void demonstrateHighThroughputPattern() {
        // High-throughput logging pattern
        long startTime = System.nanoTime();
        int messageCount = 100000;
        
        for (int i = 0; i < messageCount; i++) {
            // Efficient logging with parameters
            logger.debug("Message {} at timestamp {}", i, System.nanoTime());
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000.0;
        double messagesPerSecond = messageCount / duration;
        
        logger.info("Logged {} messages in {:.3f} seconds", messageCount, duration);
        logger.info("Throughput: {:.0f} messages/second", messagesPerSecond);
    }
    
    private String expensiveOperation() {
        // Simulate expensive operation
        return "expensive_result_" + System.currentTimeMillis();
    }
    
    private String buildComplexString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("item_").append(i).append(" ");
        }
        return sb.toString();
    }
}
```

### Memory Management

```java
public class MemoryManagementExample {
    private static final Logger logger = LogManager.getLogger(MemoryManagementExample.class);
    
    public void demonstrateMemoryOptimizations() {
        // 1. Avoid string concatenation in log parameters
        String user = "alice";
        int id = 123;
        
        // Good - parameters are only processed if logging enabled
        logger.debug("Processing user {} with id {}", user, id);
        
        // Avoid - string always created
        // logger.debug("Processing user " + user + " with id " + id);
        
        // 2. Use primitive types when possible
        logger.info("Count: {}, Rate: {}, Active: {}", 42, 3.14, true);
        
        // 3. Clean up context after use
        ThreadContext.put("operation", "memory-test");
        try {
            logger.info("Memory test operation");
        } finally {
            ThreadContext.clearAll();  // Important for memory cleanup
        }
        
        // 4. Avoid large object arrays in parameters
        int[] smallArray = {1, 2, 3};
        logger.debug("Small array: {}", Arrays.toString(smallArray));
        
        // For large arrays, consider size limits or summaries
        int[] largeArray = new int[10000];
        logger.debug("Large array size: {}, first 5: {}", 
                    largeArray.length, 
                    Arrays.toString(Arrays.copyOf(largeArray, 5)));
    }
}
```

## Configuration Reference

### Complete Configuration Options

```properties
# Basic Configuration
log4rich.level=INFO                    # Root log level
log4rich.appender.console=true         # Enable console output
log4rich.appender.file=true            # Enable file output

# Console Configuration
log4rich.appender.console.level=DEBUG  # Console-specific level
log4rich.appender.console.target=STDOUT # STDOUT or STDERR

# File Configuration
log4rich.appender.file.path=logs/app.log      # File path
log4rich.appender.file.level=INFO             # File-specific level
log4rich.appender.file.maxSize=100MB          # Max file size
log4rich.appender.file.maxBackups=10          # Number of backup files

# Format Configuration
log4rich.format=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{36} - %msg%n

# Context Configuration
log4rich.context.mdc=true              # Enable MDC integration
log4rich.context.ndc=true              # Enable NDC integration

# Performance Configuration
log4rich.performance.async=true        # Enable async logging
log4rich.performance.bufferSize=8192   # Buffer size for async

# Logger-specific Configuration
log4rich.logger.com.myapp.service=DEBUG     # Package-specific level
log4rich.logger.root=INFO                   # Root logger level
```

### Environment-Based Configuration

```bash
# Override configuration with environment variables
export LOG4RICH_LEVEL=DEBUG
export LOG4RICH_APPENDER_CONSOLE=true
export LOG4RICH_APPENDER_FILE_PATH=/var/log/myapp.log
export LOG4RICH_FORMAT='%d{ISO8601} [%thread] %level %logger - %msg%n'

# Run application with environment overrides
java -jar myapp.jar
```

### Configuration Patterns

```java
public class ConfigurationPatternExample {
    private static final Logger logger = LogManager.getLogger(ConfigurationPatternExample.class);
    
    public void demonstrateConfigurationAwareness() {
        // Check current configuration state
        logger.info("Debug enabled: {}", logger.isDebugEnabled());
        logger.info("Info enabled: {}", logger.isInfoEnabled());
        logger.info("Warn enabled: {}", logger.isWarnEnabled());
        
        // Configuration-dependent logic
        if (logger.isTraceEnabled()) {
            logger.trace("Trace logging is enabled - running detailed diagnostics");
            runDetailedDiagnostics();
        } else {
            logger.debug("Trace logging disabled - running basic diagnostics");
            runBasicDiagnostics();
        }
    }
    
    private void runDetailedDiagnostics() {
        logger.trace("Detailed diagnostic step 1");
        logger.trace("Detailed diagnostic step 2");
        logger.trace("Detailed diagnostic step 3");
    }
    
    private void runBasicDiagnostics() {
        logger.debug("Basic diagnostic completed");
    }
}
```

## Advanced Usage

### Custom Context Providers

Create custom context providers for specialized environments:

```java
// Custom context provider implementation
public class CustomContextProvider implements ContextProvider {
    private final ThreadLocal<Map<String, String>> customContext = new ThreadLocal<>();
    private final ThreadLocal<List<String>> customStack = new ThreadLocal<>();
    
    @Override
    public Map<String, String> getMDC() {
        Map<String, String> context = customContext.get();
        if (context == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(context);
    }
    
    @Override
    public List<String> getNDC() {
        List<String> stack = customStack.get();
        if (stack == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(stack);
    }
    
    @Override
    public boolean hasContext() {
        return (customContext.get() != null && !customContext.get().isEmpty()) ||
               (customStack.get() != null && !customStack.get().isEmpty());
    }
    
    // Custom methods for managing context
    public void putCustom(String key, String value) {
        Map<String, String> context = customContext.get();
        if (context == null) {
            context = new HashMap<>();
            customContext.set(context);
        }
        context.put(key, value);
    }
    
    public void pushCustom(String message) {
        List<String> stack = customStack.get();
        if (stack == null) {
            stack = new ArrayList<>();
            customStack.set(stack);
        }
        stack.add(message);
    }
}
```

### Integration with Frameworks

#### Spring Integration

```java
@Component
public class SpringLoggingService {
    private static final Logger logger = LogManager.getLogger(SpringLoggingService.class);
    
    @EventListener
    public void handleApplicationEvent(ApplicationEvent event) {
        ThreadContext.put("eventType", event.getClass().getSimpleName());
        ThreadContext.put("timestamp", String.valueOf(event.getTimestamp()));
        
        logger.info("Handling application event");
        
        ThreadContext.clearAll();
    }
    
    @Transactional
    public void performTransactionalOperation(String operationId) {
        ThreadContext.put("operationId", operationId);
        ThreadContext.put("transactional", "true");
        ThreadContext.push("TransactionalOperation");
        
        try {
            logger.info("Starting transactional operation");
            // Business logic here
            logger.info("Transactional operation completed");
        } finally {
            ThreadContext.clearAll();
        }
    }
}
```

#### Servlet Filter Integration

```java
public class LoggingFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(LoggingFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Set up request context
        String requestId = UUID.randomUUID().toString();
        ThreadContext.put("requestId", requestId);
        ThreadContext.put("method", httpRequest.getMethod());
        ThreadContext.put("uri", httpRequest.getRequestURI());
        ThreadContext.put("userAgent", httpRequest.getHeader("User-Agent"));
        ThreadContext.push("WebRequest");
        
        long startTime = System.currentTimeMillis();
        
        try {
            logger.info("Processing web request");
            chain.doFilter(request, response);
            
            long duration = System.currentTimeMillis() - startTime;
            ThreadContext.put("duration", String.valueOf(duration));
            logger.info("Web request completed");
            
        } catch (Exception e) {
            logger.error("Web request failed", e);
            throw e;
        } finally {
            ThreadContext.clearAll();
        }
    }
}
```

## Integration Patterns

### Microservices Logging

```java
public class MicroserviceLoggingExample {
    private static final Logger logger = LogManager.getLogger(MicroserviceLoggingExample.class);
    
    public void handleServiceRequest(ServiceRequest request) {
        // Set up service context
        ThreadContext.put("serviceId", "user-service");
        ThreadContext.put("version", "1.2.3");
        ThreadContext.put("requestId", request.getRequestId());
        ThreadContext.put("correlationId", request.getCorrelationId());
        ThreadContext.put("userId", request.getUserId());
        ThreadContext.push("ServiceRequestHandler");
        
        Marker serviceMarker = MarkerManager.getMarker("SERVICE");
        
        try {
            logger.info(serviceMarker, "Processing service request");
            
            // Call downstream services
            callUserValidationService(request.getUserId());
            callDataProcessingService(request.getData());
            
            logger.info(serviceMarker, "Service request completed successfully");
            
        } catch (Exception e) {
            logger.error(serviceMarker, "Service request failed", e);
            throw e;
        } finally {
            ThreadContext.clearAll();
        }
    }
    
    private void callUserValidationService(String userId) {
        ThreadContext.put("downstreamService", "user-validation");
        ThreadContext.push("DownstreamCall");
        
        try {
            logger.debug("Calling user validation service");
            // Simulate service call
            logger.debug("User validation service response received");
        } finally {
            ThreadContext.pop();
            ThreadContext.remove("downstreamService");
        }
    }
    
    private void callDataProcessingService(Object data) {
        ThreadContext.put("downstreamService", "data-processing");
        ThreadContext.push("DownstreamCall");
        
        try {
            logger.debug("Calling data processing service");
            // Simulate service call
            logger.debug("Data processing service response received");
        } finally {
            ThreadContext.pop();
            ThreadContext.remove("downstreamService");
        }
    }
}
```

### Distributed Tracing Integration

```java
public class DistributedTracingExample {
    private static final Logger logger = LogManager.getLogger(DistributedTracingExample.class);
    
    public void processWithTracing(String traceId, String spanId, String parentSpanId) {
        // Set up distributed tracing context
        ThreadContext.put("traceId", traceId);
        ThreadContext.put("spanId", spanId);
        if (parentSpanId != null) {
            ThreadContext.put("parentSpanId", parentSpanId);
        }
        
        Marker tracingMarker = MarkerManager.getMarker("TRACING");
        
        try {
            logger.info(tracingMarker, "Starting traced operation");
            
            // Create child span for database operation
            String dbSpanId = generateSpanId();
            executeWithChildSpan("database-query", dbSpanId, () -> {
                queryDatabase();
            });
            
            // Create child span for external API call
            String apiSpanId = generateSpanId();
            executeWithChildSpan("external-api", apiSpanId, () -> {
                callExternalAPI();
            });
            
            logger.info(tracingMarker, "Traced operation completed");
            
        } finally {
            // Clean up tracing context
            ThreadContext.remove("traceId");
            ThreadContext.remove("spanId");
            ThreadContext.remove("parentSpanId");
        }
    }
    
    private void executeWithChildSpan(String operationName, String spanId, Runnable operation) {
        String parentSpanId = ThreadContext.get("spanId");
        ThreadContext.put("spanId", spanId);
        ThreadContext.put("parentSpanId", parentSpanId);
        ThreadContext.put("operationName", operationName);
        ThreadContext.push("ChildSpan");
        
        try {
            logger.debug("Starting child span: {}", operationName);
            operation.run();
            logger.debug("Child span completed: {}", operationName);
        } finally {
            ThreadContext.pop();
            ThreadContext.put("spanId", parentSpanId);
            ThreadContext.remove("operationName");
        }
    }
    
    private String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    private void queryDatabase() {
        logger.debug("Executing database query");
    }
    
    private void callExternalAPI() {
        logger.debug("Calling external API");
    }
}
```

## Troubleshooting

### Common Issues and Solutions

#### 1. Performance Issues

```java
public class PerformanceTroubleshootingExample {
    private static final Logger logger = LogManager.getLogger(PerformanceTroubleshootingExample.class);
    
    // Problem: Slow logging performance
    public void problematicLogging() {
        // AVOID: String concatenation always executed
        logger.debug("Expensive operation: " + expensiveOperation());
        
        // AVOID: Complex object creation always executed
        logger.debug("Complex data: " + new ComplexObject().generateReport());
    }
    
    // Solution: Use level checking and lazy evaluation
    public void optimizedLogging() {
        // GOOD: Check level first
        if (logger.isDebugEnabled()) {
            logger.debug("Expensive operation: {}", expensiveOperation());
        }
        
        // BETTER: Use lambda for lazy evaluation
        logger.debug(() -> "Complex data: " + new ComplexObject().generateReport());
    }
    
    private String expensiveOperation() {
        // Simulate expensive operation
        return "expensive_result";
    }
    
    private static class ComplexObject {
        public String generateReport() {
            return "complex_report";
        }
    }
}
```

#### 2. Memory Leaks

```java
public class MemoryLeakTroubleshootingExample {
    private static final Logger logger = LogManager.getLogger(MemoryLeakTroubleshootingExample.class);
    
    // Problem: ThreadContext not cleaned up
    public void problematicContextUsage() {
        ThreadContext.put("operation", "example");
        ThreadContext.push("OperationStack");
        
        logger.info("Processing operation");
        
        // MISSING: ThreadContext cleanup - causes memory leak
    }
    
    // Solution: Always clean up ThreadContext
    public void correctContextUsage() {
        ThreadContext.put("operation", "example");
        ThreadContext.push("OperationStack");
        
        try {
            logger.info("Processing operation");
            // Operation logic here
        } finally {
            // IMPORTANT: Always clean up in finally block
            ThreadContext.clearAll();
        }
    }
    
    // Pattern for nested context cleanup
    public void nestedContextUsage() {
        ThreadContext.put("level1", "value1");
        
        try {
            processLevel1();
        } finally {
            ThreadContext.clearAll();
        }
    }
    
    private void processLevel1() {
        ThreadContext.put("level2", "value2");
        ThreadContext.push("Level2");
        
        try {
            logger.info("Processing level 2");
            processLevel2();
        } finally {
            ThreadContext.pop();
            ThreadContext.remove("level2");
        }
    }
    
    private void processLevel2() {
        logger.debug("In level 2 processing");
    }
}
```

#### 3. Configuration Issues

```java
public class ConfigurationTroubleshootingExample {
    private static final Logger logger = LogManager.getLogger(ConfigurationTroubleshootingExample.class);
    
    public void diagnoseConfiguration() {
        // Check if logging is working at all
        logger.error("ERROR level test - this should always appear");
        logger.warn("WARN level test");
        logger.info("INFO level test");
        logger.debug("DEBUG level test");
        logger.trace("TRACE level test");
        
        // Check level configuration
        logger.info("=== Level Configuration ===");
        logger.info("TRACE enabled: {}", logger.isTraceEnabled());
        logger.info("DEBUG enabled: {}", logger.isDebugEnabled());
        logger.info("INFO enabled: {}", logger.isInfoEnabled());
        logger.info("WARN enabled: {}", logger.isWarnEnabled());
        logger.info("ERROR enabled: {}", logger.isErrorEnabled());
        logger.info("FATAL enabled: {}", logger.isFatalEnabled());
        
        // Check marker configuration
        Marker testMarker = MarkerManager.getMarker("TEST");
        logger.info("=== Marker Configuration ===");
        logger.info("DEBUG with marker enabled: {}", logger.isDebugEnabled(testMarker));
        logger.info(testMarker, "Test message with marker");
        
        // Check context configuration
        logger.info("=== Context Configuration ===");
        ThreadContext.put("testKey", "testValue");
        ThreadContext.push("TestStack");
        
        logger.info("Message with context");
        logger.info("Context empty: {}", ThreadContext.isEmpty());
        logger.info("Context depth: {}", ThreadContext.getDepth());
        
        ThreadContext.clearAll();
    }
}
```

### Debug Techniques

#### Enable Debug Logging

```properties
# Enable comprehensive debug logging
log4rich.level=TRACE
log4rich.appender.console=true
log4rich.appender.console.level=TRACE
log4rich.internal.debug=true

# Verbose format for debugging
log4rich.format=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{50} %method:%line - %msg%n
```

#### Runtime Diagnostics

```java
public class RuntimeDiagnosticsExample {
    private static final Logger logger = LogManager.getLogger(RuntimeDiagnosticsExample.class);
    
    public void runDiagnostics() {
        logger.info("=== Runtime Diagnostics ===");
        
        // JVM Information
        logger.info("Java Version: {}", System.getProperty("java.version"));
        logger.info("Java Vendor: {}", System.getProperty("java.vendor"));
        logger.info("OS: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
        
        // Memory Information
        Runtime runtime = Runtime.getRuntime();
        logger.info("Max Memory: {} MB", runtime.maxMemory() / 1024 / 1024);
        logger.info("Total Memory: {} MB", runtime.totalMemory() / 1024 / 1024);
        logger.info("Free Memory: {} MB", runtime.freeMemory() / 1024 / 1024);
        
        // Thread Information
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        while (rootGroup.getParent() != null) {
            rootGroup = rootGroup.getParent();
        }
        logger.info("Active Threads: {}", rootGroup.activeCount());
        
        // ClassLoader Information
        logger.info("ClassLoader: {}", this.getClass().getClassLoader().getClass().getName());
        
        // Performance Test
        runPerformanceTest();
    }
    
    private void runPerformanceTest() {
        logger.info("=== Performance Test ===");
        
        int messageCount = 10000;
        long startTime = System.nanoTime();
        
        for (int i = 0; i < messageCount; i++) {
            logger.debug("Performance test message {} at {}", i, System.nanoTime());
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000.0;
        double messagesPerSecond = messageCount / duration;
        
        logger.info("Messages: {}, Duration: {:.3f}s, Rate: {:.0f} msg/s", 
                   messageCount, duration, messagesPerSecond);
    }
}
```

This comprehensive usage guide covers all aspects of using the log4j2-log4Rich bridge. The layered architecture ensures that all log4j2 features work seamlessly while benefiting from log4Rich's performance optimizations.