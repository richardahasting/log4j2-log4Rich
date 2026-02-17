package com.log4rich.log4j2.bridge;

import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ContextBridge} - the ThreadContextMap2 SPI implementation.
 */
class ContextBridgeTest {

    @AfterEach
    void cleanup() {
        ContextBridge.clearAll();
    }

    @Test
    void singletonExists() {
        assertNotNull(ContextBridge.INSTANCE);
    }

    @Test
    void implementsThreadContextMap() {
        assertInstanceOf(ThreadContextMap.class, ContextBridge.INSTANCE);
    }

    @Test
    void implementsThreadContextMap2() {
        assertInstanceOf(ThreadContextMap2.class, ContextBridge.INSTANCE);
    }

    // ========== Instance MDC operations ==========

    @Test
    void putAndGet() {
        ContextBridge.INSTANCE.put("key", "value");
        assertEquals("value", ContextBridge.INSTANCE.get("key"));
    }

    @Test
    void putNullValueRemoves() {
        ContextBridge.INSTANCE.put("key", "value");
        ContextBridge.INSTANCE.put("key", null);
        assertNull(ContextBridge.INSTANCE.get("key"));
    }

    @Test
    void putNullKeyIgnored() {
        assertDoesNotThrow(() -> ContextBridge.INSTANCE.put(null, "value"));
    }

    @Test
    void getNullKeyReturnsNull() {
        assertNull(ContextBridge.INSTANCE.get(null));
    }

    @Test
    void remove() {
        ContextBridge.INSTANCE.put("key", "value");
        ContextBridge.INSTANCE.remove("key");
        assertNull(ContextBridge.INSTANCE.get("key"));
    }

    @Test
    void containsKey() {
        ContextBridge.INSTANCE.put("key", "value");
        assertTrue(ContextBridge.INSTANCE.containsKey("key"));
        assertFalse(ContextBridge.INSTANCE.containsKey("other"));
    }

    @Test
    void isEmpty() {
        assertTrue(ContextBridge.INSTANCE.isEmpty());
        ContextBridge.INSTANCE.put("key", "value");
        assertFalse(ContextBridge.INSTANCE.isEmpty());
    }

    @Test
    void clear() {
        ContextBridge.INSTANCE.put("a", "1");
        ContextBridge.INSTANCE.put("b", "2");
        ContextBridge.INSTANCE.clear();
        assertTrue(ContextBridge.INSTANCE.isEmpty());
    }

    @Test
    void getCopy() {
        ContextBridge.INSTANCE.put("key", "value");
        Map<String, String> copy = ContextBridge.INSTANCE.getCopy();
        assertEquals("value", copy.get("key"));
        // Modifying copy shouldn't affect original
        copy.put("new", "val");
        assertNull(ContextBridge.INSTANCE.get("new"));
    }

    @Test
    void getImmutableMapOrNullWhenEmpty() {
        assertNull(ContextBridge.INSTANCE.getImmutableMapOrNull());
    }

    @Test
    void getImmutableMapOrNullWhenNotEmpty() {
        ContextBridge.INSTANCE.put("key", "value");
        Map<String, String> map = ContextBridge.INSTANCE.getImmutableMapOrNull();
        assertNotNull(map);
        assertThrows(UnsupportedOperationException.class, () -> map.put("new", "val"));
    }

    // ========== ThreadContextMap2 bulk operations ==========

    @Test
    void putAll() {
        Map<String, String> values = new HashMap<>();
        values.put("a", "1");
        values.put("b", "2");
        ContextBridge.INSTANCE.putAll(values);
        assertEquals("1", ContextBridge.INSTANCE.get("a"));
        assertEquals("2", ContextBridge.INSTANCE.get("b"));
    }

    @Test
    void removeAll() {
        ContextBridge.INSTANCE.put("a", "1");
        ContextBridge.INSTANCE.put("b", "2");
        ContextBridge.INSTANCE.put("c", "3");
        java.util.List<String> keys = java.util.Arrays.asList("a", "c");
        ContextBridge.INSTANCE.removeAll(keys);
        assertNull(ContextBridge.INSTANCE.get("a"));
        assertEquals("2", ContextBridge.INSTANCE.get("b"));
        assertNull(ContextBridge.INSTANCE.get("c"));
    }

    // ========== Static NDC operations ==========

    @Test
    void pushAndPop() {
        ContextBridge.push("msg");
        assertEquals("msg", ContextBridge.pop());
    }

    @Test
    void peek() {
        ContextBridge.push("msg");
        assertEquals("msg", ContextBridge.peek());
        assertEquals("msg", ContextBridge.peek()); // doesn't consume
    }

    @Test
    void depthTracking() {
        assertEquals(0, ContextBridge.getDepth());
        ContextBridge.push("a");
        assertEquals(1, ContextBridge.getDepth());
        ContextBridge.push("b");
        assertEquals(2, ContextBridge.getDepth());
        ContextBridge.pop();
        assertEquals(1, ContextBridge.getDepth());
    }

    @Test
    void clearStack() {
        ContextBridge.push("a");
        ContextBridge.push("b");
        ContextBridge.clearStack();
        assertEquals(0, ContextBridge.getDepth());
    }

    @Test
    void getImmutableStack() {
        ContextBridge.push("a");
        ContextBridge.push("b");
        java.util.List<String> stack = ContextBridge.getImmutableStack();
        assertEquals(2, stack.size());
        assertThrows(UnsupportedOperationException.class, () -> stack.add("c"));
    }

    @Test
    void popOnEmptyReturnsNull() {
        assertNull(ContextBridge.pop());
    }

    @Test
    void peekOnEmptyReturnsNull() {
        assertNull(ContextBridge.peek());
    }

    @Test
    void clearAllClearsBoth() {
        ContextBridge.INSTANCE.put("key", "value");
        ContextBridge.push("msg");
        ContextBridge.clearAll();
        assertTrue(ContextBridge.INSTANCE.isEmpty());
        assertEquals(0, ContextBridge.getDepth());
    }
}
