package org.apache.logging.log4j.spi;

import java.util.Map;

/**
 * Read-only view of the thread context map.
 *
 * @since 1.0.5
 */
public interface ReadOnlyThreadContextMap {

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
     * Gets a non-null copy of the map.
     */
    Map<String, String> getCopy();

    /**
     * Gets an immutable view or null.
     */
    Map<String, String> getImmutableMapOrNull();

    /**
     * Returns true if the map is empty.
     */
    boolean isEmpty();
}
