package org.apache.logging.log4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link CloseableThreadContext} - auto-closeable MDC/NDC management.
 */
class CloseableThreadContextTest {

    @AfterEach
    void cleanup() {
        ThreadContext.clearAll();
    }

    @Test
    void putAddsToContextAndRemovesOnClose() {
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.put("requestId", "123")) {
            assertEquals("123", ThreadContext.get("requestId"));
        }
        assertNull(ThreadContext.get("requestId"));
    }

    @Test
    void pushAddsToStackAndPopsOnClose() {
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.push("operation1")) {
            assertEquals("operation1", ThreadContext.peek());
            assertEquals(1, ThreadContext.getDepth());
        }
        assertEquals(0, ThreadContext.getDepth());
    }

    @Test
    void pushWithFormatting() {
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.push("op-%s-%d", "test", 42)) {
            assertEquals("op-test-42", ThreadContext.peek());
        }
        assertEquals(0, ThreadContext.getDepth());
    }

    @Test
    void chainingPut() {
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.put("a", "1").put("b", "2")) {
            assertEquals("1", ThreadContext.get("a"));
            assertEquals("2", ThreadContext.get("b"));
        }
        assertNull(ThreadContext.get("a"));
        assertNull(ThreadContext.get("b"));
    }

    @Test
    void chainingPush() {
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.push("msg1").push("msg2")) {
            assertEquals(2, ThreadContext.getDepth());
        }
        assertEquals(0, ThreadContext.getDepth());
    }

    @Test
    void mixedPutAndPush() {
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.put("key", "val").push("msg")) {
            assertEquals("val", ThreadContext.get("key"));
            assertEquals(1, ThreadContext.getDepth());
        }
        assertNull(ThreadContext.get("key"));
        assertEquals(0, ThreadContext.getDepth());
    }

    @Test
    void nestedContexts() {
        try (CloseableThreadContext.Instance outer = CloseableThreadContext.put("outer", "1")) {
            assertEquals("1", ThreadContext.get("outer"));
            try (CloseableThreadContext.Instance inner = CloseableThreadContext.put("inner", "2")) {
                assertEquals("1", ThreadContext.get("outer"));
                assertEquals("2", ThreadContext.get("inner"));
            }
            // inner is cleaned up
            assertEquals("1", ThreadContext.get("outer"));
            assertNull(ThreadContext.get("inner"));
        }
        // outer is cleaned up
        assertNull(ThreadContext.get("outer"));
    }

    @Test
    void existingContextNotAffected() {
        ThreadContext.put("existing", "keep");
        try (CloseableThreadContext.Instance ctx = CloseableThreadContext.put("temp", "val")) {
            assertEquals("keep", ThreadContext.get("existing"));
        }
        // existing context preserved
        assertEquals("keep", ThreadContext.get("existing"));
    }
}
