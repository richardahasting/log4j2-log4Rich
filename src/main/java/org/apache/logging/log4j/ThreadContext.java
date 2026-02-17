package org.apache.logging.log4j;

import com.log4rich.log4j2.bridge.ContextBridge;
import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;
import java.util.Map;
import java.util.List;

/**
 * log4j2 ThreadContext implementation that delegates to ContextBridge.
 * Provides both MDC (Mapped Diagnostic Context) and NDC (Nested Diagnostic Context) functionality.
 */
public class ThreadContext {
    
    // Prevent instantiation - all methods are static
    private ThreadContext() {}
    
    // ========== MDC (Mapped Diagnostic Context) Methods ==========
    
    /**
     * Puts a key-value pair into the current thread's context map.
     */
    public static void put(String key, String value) {
        ContextBridge.INSTANCE.put(key, value);
    }
    
    /**
     * Gets a value from the current thread's context map.
     */
    public static String get(String key) {
        return ContextBridge.INSTANCE.get(key);
    }
    
    /**
     * Removes a key from the current thread's context map.
     */
    public static void remove(String key) {
        ContextBridge.INSTANCE.remove(key);
    }

    /**
     * Clears the current thread's context map.
     */
    public static void clearMap() {
        ContextBridge.clearContext();
    }
    
    /**
     * Clears all context data (both map and stack).
     */
    public static void clearAll() {
        ContextBridge.clearAll();
    }
    
    /**
     * Checks if the context map contains the specified key.
     */
    public static boolean containsKey(String key) {
        return ContextBridge.INSTANCE.containsKey(key);
    }
    
    /**
     * Checks if the context map is empty.
     */
    public static boolean isEmpty() {
        return ContextBridge.INSTANCE.isEmpty() && ContextBridge.getDepth() == 0;
    }
    
    /**
     * Gets a copy of the current thread's context map.
     */
    public static Map<String, String> getContext() {
        return ContextBridge.getContext();
    }
    
    /**
     * Gets an immutable copy of the current thread's context map.
     */
    public static Map<String, String> getImmutableContext() {
        return ContextBridge.getImmutableContext();
    }
    
    // ========== NDC (Nested Diagnostic Context) Methods ==========
    
    /**
     * Pushes a value onto the current thread's diagnostic context stack.
     */
    public static void push(String message) {
        ContextBridge.push(message);
    }
    
    /**
     * Pops the top value from the current thread's diagnostic context stack.
     */
    public static String pop() {
        return ContextBridge.pop();
    }
    
    /**
     * Peeks at the top value of the current thread's diagnostic context stack.
     */
    public static String peek() {
        return ContextBridge.peek();
    }
    
    /**
     * Gets the depth of the current thread's diagnostic context stack.
     */
    public static int getDepth() {
        return ContextBridge.getDepth();
    }
    
    /**
     * Clears the current thread's diagnostic context stack.
     */
    public static void clearStack() {
        ContextBridge.clearStack();
    }
    
    /**
     * Gets an immutable copy of the current thread's diagnostic context stack.
     */
    public static List<String> getImmutableStack() {
        return ContextBridge.getImmutableStack();
    }
    
    /**
     * Clones the current thread's diagnostic context stack.
     */
    public static List<String> cloneStack() {
        return ContextBridge.getImmutableStack();
    }
    
    /**
     * Sets the current thread's diagnostic context stack.
     */
    public static void setStack(java.util.Collection<String> stack) {
        ContextBridge.clearStack();
        if (stack != null) {
            for (String item : stack) {
                ContextBridge.push(item);
            }
        }
    }
    
    /**
     * Trims the context stack to the specified size.
     */
    public static void trimToSize(int maxDepth) {
        if (maxDepth < 0) {
            return;
        }
        
        while (ContextBridge.getDepth() > maxDepth) {
            ContextBridge.pop();
        }
    }
    
    // ========== SPI Access ==========

    /**
     * Gets the ThreadContextMap instance.
     *
     * @return the ThreadContextMap implementation
     */
    public static ThreadContextMap getThreadContextMap() {
        return ContextBridge.INSTANCE;
    }

    /**
     * Gets a read-only view of the thread context map.
     *
     * @return the ReadOnlyThreadContextMap, or null
     */
    public static ReadOnlyThreadContextMap getThreadContextMap2() {
        // ContextBridge doesn't implement ReadOnlyThreadContextMap separately;
        // return null as per log4j2 convention when not available
        return null;
    }

    // ========== Convenience Methods ==========
    
    /**
     * Puts multiple key-value pairs into the context map.
     */
    public static void putAll(Map<String, String> context) {
        if (context != null) {
            for (Map.Entry<String, String> entry : context.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * Removes multiple keys from the context map.
     */
    public static void removeAll(Iterable<String> keys) {
        if (keys != null) {
            for (String key : keys) {
                remove(key);
            }
        }
    }
}