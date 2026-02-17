package com.log4rich.log4j2.bridge;

import org.apache.logging.log4j.spi.ThreadContextMap2;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bridges log4j2 ThreadContext to log4Rich context system.
 * Manages thread-local context data and NDC stack.
 * Implements {@link ThreadContextMap2} for SPI compatibility.
 *
 * <p>Instance methods satisfy the ThreadContextMap/ThreadContextMap2 interfaces.
 * Use the {@link #INSTANCE} singleton for SPI access. Static helper methods
 * are provided for NDC operations and bulk context access that don't overlap
 * with the interface.</p>
 */
public class ContextBridge implements ThreadContextMap2 {

    /** Singleton instance for SPI access. */
    public static final ContextBridge INSTANCE = new ContextBridge();

    // Thread-local storage for context data (MDC equivalent)
    private static final ThreadLocal<Map<String, String>> CONTEXT_MAP =
        ThreadLocal.withInitial(ConcurrentHashMap::new);

    // Thread-local storage for nested diagnostic context (NDC)
    private static final ThreadLocal<Deque<String>> CONTEXT_STACK =
        ThreadLocal.withInitial(ArrayDeque::new);

    /**
     * Applies current thread context to log4Rich.
     */
    public static void applyThreadContext() {
        Map<String, String> context = CONTEXT_MAP.get();
        if (!context.isEmpty()) {
            // Apply context to log4Rich
        }
    }

    // ========== Static context access (no interface conflict) ==========

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
        return context.isEmpty()
            ? Collections.emptyMap()
            : Collections.unmodifiableMap(new ConcurrentHashMap<>(context));
    }

    /**
     * Clears the current thread's context map.
     */
    public static void clearContext() {
        CONTEXT_MAP.get().clear();
    }

    /**
     * Clears both context map and stack.
     */
    public static void clearAll() {
        clearContext();
        clearStack();
    }

    // ========== Static NDC operations ==========

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
        Deque<String> stack = CONTEXT_STACK.get();
        return stack.isEmpty() ? null : stack.pop();
    }

    /**
     * Peeks at the top of the NDC stack without removing it.
     */
    public static String peek() {
        Deque<String> stack = CONTEXT_STACK.get();
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
    public static List<String> getImmutableStack() {
        Deque<String> stack = CONTEXT_STACK.get();
        return stack.isEmpty()
            ? Collections.emptyList()
            : Collections.unmodifiableList(new ArrayList<>(stack));
    }

    // ========== ThreadContextMap interface (instance methods) ==========

    @Override
    public void put(final String key, final String value) {
        if (key != null) {
            if (value == null) {
                CONTEXT_MAP.get().remove(key);
            } else {
                CONTEXT_MAP.get().put(key, value);
            }
        }
    }

    @Override
    public String get(final String key) {
        return key != null ? CONTEXT_MAP.get().get(key) : null;
    }

    @Override
    public void remove(final String key) {
        if (key != null) {
            CONTEXT_MAP.get().remove(key);
        }
    }

    @Override
    public boolean containsKey(final String key) {
        return key != null && CONTEXT_MAP.get().containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return CONTEXT_MAP.get().isEmpty();
    }

    @Override
    public void clear() {
        clearContext();
    }

    @Override
    public Map<String, String> getCopy() {
        return getContext();
    }

    @Override
    public Map<String, String> getImmutableMapOrNull() {
        Map<String, String> context = CONTEXT_MAP.get();
        return context.isEmpty() ? null : Collections.unmodifiableMap(new ConcurrentHashMap<>(context));
    }

    // ========== ThreadContextMap2 interface ==========

    @Override
    public void putAll(Map<String, String> map) {
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void removeAll(Iterable<String> keys) {
        if (keys != null) {
            for (String key : keys) {
                remove(key);
            }
        }
    }
}
