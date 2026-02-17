package org.apache.logging.log4j.spi;

import java.util.Map;

/**
 * Extension of {@link ThreadContextMap} with bulk operations.
 *
 * @since 1.0.5
 */
public interface ThreadContextMap2 extends ThreadContextMap {

    /**
     * Puts all key-value pairs into the map.
     */
    void putAll(Map<String, String> map);

    /**
     * Removes all specified keys from the map.
     */
    void removeAll(Iterable<String> keys);
}
