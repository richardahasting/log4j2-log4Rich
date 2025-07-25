package com.log4rich.log4j2.bridge;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bridges log4j2 ThreadContext to log4Rich context system.
 * Manages thread-local context data and NDC stack.
 */
public class ContextBridge {
    
    // Thread-local storage for context data (MDC equivalent)
    private static final ThreadLocal<Map<String, String>> CONTEXT_MAP = 
        ThreadLocal.withInitial(ConcurrentHashMap::new);
    
    // Thread-local storage for nested diagnostic context (NDC)
    private static final ThreadLocal<java.util.Deque<String>> CONTEXT_STACK = 
        ThreadLocal.withInitial(java.util.ArrayDeque::new);
    
    /**
     * Applies current thread context to log4Rich.
     * This would typically integrate with log4Rich's context system.
     */
    public static void applyThreadContext() {
        // For now, this is a placeholder
        // In a full implementation, this would:
        // 1. Get current thread context
        // 2. Apply it to log4Rich's context system
        // 3. Handle both MDC and NDC data
        
        Map<String, String> context = CONTEXT_MAP.get();
        if (!context.isEmpty()) {
            // Apply context to log4Rich
            // log4Rich.setContext(context);
        }
    }
    
    /**
     * Gets the current thread's context map.
     */
    public static Map<String, String> getContext() {
        return new ConcurrentHashMap<>(CONTEXT_MAP.get());
    }
    
    /**
     * Gets an immutable copy of the current context.
     */
    public static Map<String, String> getImmutableContext() {
        Map<String, String> context = CONTEXT_MAP.get();
        return context.isEmpty() ? 
            java.util.Collections.emptyMap() : 
            java.util.Collections.unmodifiableMap(new ConcurrentHashMap<>(context));
    }
    
    /**
     * Puts a key-value pair into the current thread's context.
     */
    public static void put(String key, String value) {
        if (key != null) {
            if (value == null) {
                CONTEXT_MAP.get().remove(key);
            } else {
                CONTEXT_MAP.get().put(key, value);
            }
        }
    }
    
    /**
     * Gets a value from the current thread's context.
     */
    public static String get(String key) {
        return key != null ? CONTEXT_MAP.get().get(key) : null;
    }
    
    /**
     * Removes a key from the current thread's context.
     */
    public static void remove(String key) {
        if (key != null) {
            CONTEXT_MAP.get().remove(key);
        }
    }
    
    /**
     * Clears the current thread's context.
     */
    public static void clearContext() {
        CONTEXT_MAP.get().clear();
    }
    
    /**
     * Checks if the context contains a specific key.
     */
    public static boolean containsKey(String key) {
        return key != null && CONTEXT_MAP.get().containsKey(key);
    }
    
    /**
     * Checks if the context is empty.
     */
    public static boolean isEmpty() {
        return CONTEXT_MAP.get().isEmpty();
    }
    
    // NDC (Nested Diagnostic Context) operations
    
    /**
     * Pushes a value onto the NDC stack.
     */
    public static void push(String value) {
        if (value != null) {
            CONTEXT_STACK.get().push(value);
        }
    }
    
    /**
     * Pops a value from the NDC stack.
     */
    public static String pop() {
        java.util.Deque<String> stack = CONTEXT_STACK.get();
        return stack.isEmpty() ? null : stack.pop();
    }
    
    /**
     * Peeks at the top of the NDC stack without removing it.
     */
    public static String peek() {
        java.util.Deque<String> stack = CONTEXT_STACK.get();
        return stack.isEmpty() ? null : stack.peek();
    }
    
    /**
     * Gets the depth of the NDC stack.
     */
    public static int getDepth() {
        return CONTEXT_STACK.get().size();
    }
    
    /**
     * Clears the NDC stack.
     */
    public static void clearStack() {
        CONTEXT_STACK.get().clear();
    }
    
    /**
     * Gets an immutable copy of the current NDC stack.
     */
    public static java.util.List<String> getImmutableStack() {
        java.util.Deque<String> stack = CONTEXT_STACK.get();
        return stack.isEmpty() ? 
            java.util.Collections.emptyList() : 
            java.util.Collections.unmodifiableList(new java.util.ArrayList<>(stack));
    }
    
    /**
     * Clears both context map and stack.
     */
    public static void clearAll() {
        clearContext();
        clearStack();
    }
}