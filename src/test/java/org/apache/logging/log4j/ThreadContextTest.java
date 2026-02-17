package org.apache.logging.log4j;

import org.apache.logging.log4j.spi.ThreadContextMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ThreadContext} - MDC and NDC operations.
 */
class ThreadContextTest {

    @AfterEach
    void cleanup() {
        ThreadContext.clearAll();
    }

    // ========== MDC Tests ==========

    @Test
    void putAndGet() {
        ThreadContext.put("key1", "value1");
        assertEquals("value1", ThreadContext.get("key1"));
    }

    @Test
    void getReturnsNullForMissingKey() {
        assertNull(ThreadContext.get("nonexistent"));
    }

    @Test
    void remove() {
        ThreadContext.put("key1", "value1");
        ThreadContext.remove("key1");
        assertNull(ThreadContext.get("key1"));
    }

    @Test
    void containsKey() {
        ThreadContext.put("key1", "value1");
        assertTrue(ThreadContext.containsKey("key1"));
        assertFalse(ThreadContext.containsKey("key2"));
    }

    @Test
    void clearMap() {
        ThreadContext.put("key1", "value1");
        ThreadContext.put("key2", "value2");
        ThreadContext.clearMap();
        assertNull(ThreadContext.get("key1"));
        assertNull(ThreadContext.get("key2"));
    }

    @Test
    void getContext() {
        ThreadContext.put("key1", "value1");
        ThreadContext.put("key2", "value2");
        Map<String, String> ctx = ThreadContext.getContext();
        assertEquals(2, ctx.size());
        assertEquals("value1", ctx.get("key1"));
    }

    @Test
    void getImmutableContext() {
        ThreadContext.put("key1", "value1");
        Map<String, String> ctx = ThreadContext.getImmutableContext();
        assertThrows(UnsupportedOperationException.class, () -> ctx.put("key2", "value2"));
    }

    @Test
    void putAll() {
        Map<String, String> values = new HashMap<>();
        values.put("a", "1");
        values.put("b", "2");
        ThreadContext.putAll(values);
        assertEquals("1", ThreadContext.get("a"));
        assertEquals("2", ThreadContext.get("b"));
    }

    @Test
    void removeAll() {
        ThreadContext.put("a", "1");
        ThreadContext.put("b", "2");
        ThreadContext.put("c", "3");
        java.util.List<String> keys = java.util.Arrays.asList("a", "c");
        ThreadContext.removeAll(keys);
        assertNull(ThreadContext.get("a"));
        assertEquals("2", ThreadContext.get("b"));
        assertNull(ThreadContext.get("c"));
    }

    // ========== NDC Tests ==========

    @Test
    void pushAndPop() {
        ThreadContext.push("msg1");
        assertEquals("msg1", ThreadContext.pop());
    }

    @Test
    void pushAndPeek() {
        ThreadContext.push("msg1");
        assertEquals("msg1", ThreadContext.peek());
        // peek doesn't remove
        assertEquals("msg1", ThreadContext.peek());
    }

    @Test
    void stackIsLIFO() {
        ThreadContext.push("first");
        ThreadContext.push("second");
        ThreadContext.push("third");
        assertEquals("third", ThreadContext.pop());
        assertEquals("second", ThreadContext.pop());
        assertEquals("first", ThreadContext.pop());
    }

    @Test
    void getDepth() {
        assertEquals(0, ThreadContext.getDepth());
        ThreadContext.push("a");
        assertEquals(1, ThreadContext.getDepth());
        ThreadContext.push("b");
        assertEquals(2, ThreadContext.getDepth());
    }

    @Test
    void clearStack() {
        ThreadContext.push("a");
        ThreadContext.push("b");
        ThreadContext.clearStack();
        assertEquals(0, ThreadContext.getDepth());
    }

    @Test
    void getImmutableStack() {
        ThreadContext.push("a");
        ThreadContext.push("b");
        List<String> stack = ThreadContext.getImmutableStack();
        assertEquals(2, stack.size());
        assertThrows(UnsupportedOperationException.class, () -> stack.add("c"));
    }

    @Test
    void cloneStack() {
        ThreadContext.push("a");
        List<String> cloned = ThreadContext.cloneStack();
        assertNotNull(cloned);
        assertEquals(1, cloned.size());
    }

    @Test
    void trimToSize() {
        ThreadContext.push("a");
        ThreadContext.push("b");
        ThreadContext.push("c");
        ThreadContext.trimToSize(1);
        assertEquals(1, ThreadContext.getDepth());
    }

    // ========== Combined Tests ==========

    @Test
    void clearAllRemovesBothMapAndStack() {
        ThreadContext.put("key", "val");
        ThreadContext.push("msg");
        ThreadContext.clearAll();
        assertNull(ThreadContext.get("key"));
        assertEquals(0, ThreadContext.getDepth());
    }

    @Test
    void isEmptyWhenBothEmpty() {
        assertTrue(ThreadContext.isEmpty());
    }

    @Test
    void isEmptyFalseWithMDC() {
        ThreadContext.put("key", "val");
        assertFalse(ThreadContext.isEmpty());
    }

    @Test
    void isEmptyFalseWithNDC() {
        ThreadContext.push("msg");
        assertFalse(ThreadContext.isEmpty());
    }

    // ========== SPI Access ==========

    @Test
    void getThreadContextMapReturnsNonNull() {
        ThreadContextMap map = ThreadContext.getThreadContextMap();
        assertNotNull(map);
    }

    @Test
    void threadContextMapPutAndGet() {
        ThreadContextMap map = ThreadContext.getThreadContextMap();
        map.put("spi-key", "spi-value");
        assertEquals("spi-value", map.get("spi-key"));
    }
}
