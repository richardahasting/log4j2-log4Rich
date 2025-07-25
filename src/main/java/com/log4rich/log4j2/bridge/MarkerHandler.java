package com.log4rich.log4j2.bridge;

import org.apache.logging.log4j.Marker;

/**
 * Handles log4j2 Marker extraction and formatting.
 * Provides consistent marker processing across all logging methods.
 */
public class MarkerHandler {
    
    /**
     * Extracts marker information for inclusion in log messages.
     * Handles marker hierarchy and parent relationships.
     */
    public static String extractMarkerInfo(Marker marker) {
        if (marker == null) {
            return null;
        }
        
        StringBuilder markerInfo = new StringBuilder(marker.getName());
        
        // Include parent markers for full context
        if (marker.hasParents()) {
            Marker[] parents = marker.getParents();
            if (parents.length > 0) {
                markerInfo.append(" [");
                for (int i = 0; i < parents.length; i++) {
                    if (i > 0) markerInfo.append(", ");
                    markerInfo.append(parents[i].getName());
                }
                markerInfo.append("]");
            }
        }
        
        return markerInfo.toString();
    }
    
    /**
     * Determines if logging should proceed based on marker filtering.
     * For now, always returns true - can be enhanced for marker-based filtering.
     */
    public static boolean shouldLog(Marker marker, org.apache.logging.log4j.Level level) {
        // Future enhancement: implement marker-based filtering
        // For now, all markers are allowed
        return true;
    }
    
    /**
     * Formats marker information for inclusion in log messages.
     * Returns formatted string or null if no marker.
     */
    public static String formatMarker(Marker marker) {
        String markerInfo = extractMarkerInfo(marker);
        if (markerInfo == null) {
            return null;
        }
        return "[" + markerInfo + "] ";
    }
    
    /**
     * Checks if a marker matches a specific name (including parent hierarchy).
     */
    public static boolean isInstanceOf(Marker marker, String name) {
        if (marker == null || name == null) {
            return false;
        }
        
        // Check marker itself
        if (name.equals(marker.getName())) {
            return true;
        }
        
        // Check parent markers recursively
        if (marker.hasParents()) {
            for (Marker parent : marker.getParents()) {
                if (isInstanceOf(parent, name)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if a marker matches another marker (including parent hierarchy).
     */
    public static boolean isInstanceOf(Marker marker, Marker other) {
        if (marker == null || other == null) {
            return false;
        }
        
        return isInstanceOf(marker, other.getName());
    }
}