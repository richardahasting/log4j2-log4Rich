package org.apache.logging.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Marker factory and manager.
 */
public class MarkerManager {
    
    private static final ConcurrentMap<String, Marker> MARKERS = new ConcurrentHashMap<>();
    
    /**
     * Gets or creates a marker with the specified name.
     */
    public static Marker getMarker(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Marker name cannot be null");
        }
        
        return MARKERS.computeIfAbsent(name, Marker::new);
    }
    
    /**
     * Checks if a marker exists.
     */
    public static boolean exists(String name) {
        return name != null && MARKERS.containsKey(name);
    }
    
    /**
     * Gets all existing markers.
     */
    public static Marker[] getMarkers() {
        return MARKERS.values().toArray(new Marker[0]);
    }
}