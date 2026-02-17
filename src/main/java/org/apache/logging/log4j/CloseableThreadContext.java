package org.apache.logging.log4j;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides try-with-resources support for {@link ThreadContext} operations.
 * MDC/NDC values added through this class are automatically removed when
 * the {@link Instance} is closed.
 *
 * <p>Usage:</p>
 * <pre>{@code
 * try (CloseableThreadContext.Instance ctx = CloseableThreadContext.put("requestId", id)) {
 *     // MDC value is set
 *     processRequest();
 * }
 * // MDC value automatically removed
 * }</pre>
 *
 * @since 1.0.5
 */
public class CloseableThreadContext {

    private CloseableThreadContext() {
    }

    /**
     * Puts a key-value pair into the ThreadContext map.
     *
     * @param key the key
     * @param value the value
     * @return an Instance that will remove the key when closed
     */
    public static Instance put(String key, String value) {
        return new Instance().put(key, value);
    }

    /**
     * Pushes a message onto the ThreadContext stack.
     *
     * @param message the message
     * @return an Instance that will pop the value when closed
     */
    public static Instance push(String message) {
        return new Instance().push(message);
    }

    /**
     * Pushes a formatted message onto the ThreadContext stack.
     *
     * @param message the message format
     * @param args the message arguments
     * @return an Instance that will pop the value when closed
     */
    public static Instance push(String message, Object... args) {
        if (args != null && args.length > 0) {
            return new Instance().push(String.format(message, args));
        }
        return new Instance().push(message);
    }

    /**
     * An auto-closeable instance that tracks MDC keys and NDC pushes
     * for cleanup on close.
     */
    public static class Instance implements Closeable {

        private final List<String> keys = new ArrayList<>();
        private int pushCount = 0;

        private Instance() {
        }

        /**
         * Puts a key-value pair into the ThreadContext map.
         * The key will be removed when this Instance is closed.
         *
         * @param key the key
         * @param value the value
         * @return this instance for chaining
         */
        public Instance put(String key, String value) {
            ThreadContext.put(key, value);
            keys.add(key);
            return this;
        }

        /**
         * Pushes a message onto the ThreadContext stack.
         * The message will be popped when this Instance is closed.
         *
         * @param message the message
         * @return this instance for chaining
         */
        public Instance push(String message) {
            ThreadContext.push(message);
            pushCount++;
            return this;
        }

        /**
         * Removes all keys and pops all stack entries that were added
         * through this instance.
         */
        @Override
        public void close() {
            for (String key : keys) {
                ThreadContext.remove(key);
            }
            for (int i = 0; i < pushCount; i++) {
                ThreadContext.pop();
            }
        }
    }
}
