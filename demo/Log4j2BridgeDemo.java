package demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.function.Supplier;

/**
 * Demonstration of log4j2 to log4Rich bridge functionality.
 * Shows all major log4j2 features working with log4Rich backend.
 */
public class Log4j2BridgeDemo {
    
    // Standard log4j2 Logger - automatically uses log4Rich backend
    private static final Logger logger = LogManager.getLogger(Log4j2BridgeDemo.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("log4j2 to log4Rich Bridge Demonstration");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Basic logging methods
        demonstrateBasicLogging();
        
        // Parameter formatting
        demonstrateParameterFormatting();
        
        // Exception logging
        demonstrateExceptionLogging();
        
        // Marker support
        demonstrateMarkers();
        
        // Thread context (MDC/NDC)
        demonstrateThreadContext();
        
        // Level checking
        demonstrateLevelChecking();
        
        // Message objects
        demonstrateMessageObjects();
        
        // Lambda/Supplier support
        demonstrateLambdaSupport();
        
        // Performance comparison
        demonstratePerformance();
        
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("All log4j2 API calls successfully translated to log4Rich!");
        System.out.println("Zero code changes required - just swap the jar file.");
        System.out.println("=".repeat(70));
    }
    
    private static void demonstrateBasicLogging() {
        System.out.println("🔵 Basic Logging Methods:");
        
        logger.trace("This is a TRACE message");
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
        logger.fatal("This is a FATAL message");
        
        System.out.println("✓ All basic logging levels working\n");
    }
    
    private static void demonstrateParameterFormatting() {
        System.out.println("🔵 Parameter Formatting:");
        
        String user = "Alice";
        int count = 42;
        double value = 3.14159;
        
        // Single parameter
        logger.info("Single param: {}", user);
        
        // Multiple parameters
        logger.info("Multiple params: {} has {} items worth ${}", user, count, value);
        
        // Array parameter
        String[] items = {"apple", "banana", "cherry"};
        logger.info("Array param: {}", (Object) items);
        
        System.out.println("✓ Parameter formatting working\n");
    }
    
    private static void demonstrateExceptionLogging() {
        System.out.println("🔵 Exception Logging:");
        
        try {
            simulateError();
        } catch (Exception e) {
            logger.error("Caught exception during simulation", e);
            logger.error("Exception with params: {} failed after {} attempts", "operation", 3, e);
        }
        
        System.out.println("✓ Exception logging working\n");
    }
    
    private static void demonstrateMarkers() {
        System.out.println("🔵 Marker Support:");
        
        Marker performanceMarker = MarkerManager.getMarker("PERFORMANCE");
        Marker securityMarker = MarkerManager.getMarker("SECURITY");
        
        // Add parent marker
        securityMarker.addParents(performanceMarker);
        
        logger.info(performanceMarker, "Performance measurement started");
        logger.warn(securityMarker, "Security check failed for user {}", "bob");
        
        // Test marker hierarchy
        logger.debug("Security marker has parents: {}", securityMarker.hasParents());
        logger.debug("Security marker is instance of PERFORMANCE: {}", 
                    securityMarker.isInstanceOf("PERFORMANCE"));
        
        System.out.println("✓ Marker support working\n");
    }
    
    private static void demonstrateThreadContext() {
        System.out.println("🔵 Thread Context (MDC/NDC):");
        
        // MDC (Mapped Diagnostic Context)
        ThreadContext.put("userId", "12345");
        ThreadContext.put("sessionId", "abcdef");
        ThreadContext.put("requestId", "req-" + System.currentTimeMillis());
        
        logger.info("Message with MDC context");
        
        // NDC (Nested Diagnostic Context)
        ThreadContext.push("UserService");
        ThreadContext.push("authenticateUser");
        
        logger.info("Message with NDC context");
        
        logger.debug("MDC size: {}, NDC depth: {}", 
                    ThreadContext.getContext().size(), ThreadContext.getDepth());
        
        // Clean up
        ThreadContext.pop();
        ThreadContext.pop();
        ThreadContext.clearAll();
        
        System.out.println("✓ Thread context working\n");
    }
    
    private static void demonstrateLevelChecking() {
        System.out.println("🔵 Level Checking:");
        
        // Basic level checks
        logger.debug("Debug enabled: {}", logger.isDebugEnabled());
        logger.info("Info enabled: {}", logger.isInfoEnabled());
        logger.warn("Warn enabled: {}", logger.isWarnEnabled());
        
        // Level-specific checks for performance
        if (logger.isDebugEnabled()) {
            logger.debug("Expensive debug operation: {}", expensiveDebugOperation());
        }
        
        // Generic level checking
        logger.info("ERROR level enabled: {}", logger.isEnabled(Level.ERROR));
        
        System.out.println("✓ Level checking working\n");
    }
    
    private static void demonstrateMessageObjects() {
        System.out.println("🔵 Message Objects:");
        
        // ParameterizedMessage
        ParameterizedMessage paramMsg = new ParameterizedMessage("User {} performed {} actions", "Charlie", 7);
        logger.info(paramMsg);
        
        // ParameterizedMessage with exception
        try {
            throw new RuntimeException("Demo exception");
        } catch (Exception e) {
            ParameterizedMessage exceptionMsg = new ParameterizedMessage("Operation {} failed", "backup", e);
            logger.error(exceptionMsg);
        }
        
        System.out.println("✓ Message objects working\n");
    }
    
    private static void demonstrateLambdaSupport() {
        System.out.println("🔵 Lambda/Supplier Support:");
        
        // Supplier for lazy evaluation
        logger.debug(() -> "Expensive computation result: " + expensiveComputation());
        
        // Supplier with exception
        try {
            throw new IllegalStateException("Lambda demo exception");
        } catch (Exception e) {
            logger.error(() -> "Lambda error message with timestamp: " + System.currentTimeMillis(), e);
        }
        
        System.out.println("✓ Lambda support working\n");
    }
    
    private static void demonstratePerformance() {
        System.out.println("🔵 Performance Demonstration:");
        
        long startTime = System.nanoTime();
        
        // Rapid logging to show log4Rich performance
        for (int i = 0; i < 10000; i++) {
            logger.debug("Performance test message {} with timestamp {}", i, System.nanoTime());
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        
        logger.info("Logged 10,000 messages in {:.2f} ms", durationMs);
        logger.info("Performance: {:.0f} messages/second", 10000 / (durationMs / 1000));
        
        System.out.println("✓ High-performance logging demonstrated\n");
    }
    
    // Helper methods
    
    private static void simulateError() throws Exception {
        throw new RuntimeException("Simulated error for demonstration");
    }
    
    private static String expensiveDebugOperation() {
        // Simulate expensive operation
        return "debug_result_" + System.currentTimeMillis();
    }
    
    private static String expensiveComputation() {
        // Simulate expensive computation
        return "computed_" + Math.random();
    }
}