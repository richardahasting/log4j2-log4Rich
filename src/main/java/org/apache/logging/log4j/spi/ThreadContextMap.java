package org.apache.logging.log4j.spi;

import java.util.Map;

/**
 * SPI interface for the MDC (Mapped Diagnostic Context) implementation.
 * The real log4j2 {@link org.apache.logging.log4j.ThreadContext} delegates
 * to an instance of this interface.
 *
 * @since 1.0.5
 */
public interface ThreadContextMap {

    /**
     * Clears the context map.
     */
    void clear();

    /**
     * Checks if the map contains the key.
     */
    boolean containsKey(String key);

    /**
     * Gets the value for the key.
     */
    String get(String key);

    /**
     * Gets a non-null copy of the context map.
     */
    Map<String, String> getCopy();

    /**
     * Gets an immutable view of the context map.
     */
    Map<String, String> getImmutableMapOrNull();

    /**
     * Returns true if the map is empty.
     */
    boolean isEmpty();

    /**
     * Puts a key-value pair into the map.
     */
    void put(String key, String value);

    /**
     * Removes the key from the map.
     */
    void remove(String key);
}
