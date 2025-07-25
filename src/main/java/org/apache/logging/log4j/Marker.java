package org.apache.logging.log4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * log4j2 Marker implementation.
 * Supports marker hierarchy and parent relationships.
 */
public class Marker {
    
    private final String name;
    private volatile Marker[] parents = new Marker[0];
    
    Marker(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Marker name cannot be null");
        }
        this.name = name;
    }
    
    /**
     * Gets the marker name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Adds parent markers to this marker.
     */
    public Marker addParents(Marker... markers) {
        if (markers == null || markers.length == 0) {
            return this;
        }
        
        synchronized (this) {
            // Create new array with additional space
            Marker[] newParents = new Marker[parents.length + markers.length];
            System.arraycopy(parents, 0, newParents, 0, parents.length);
            
            // Add new parents, avoiding duplicates
            int insertIndex = parents.length;
            for (Marker marker : markers) {
                if (marker != null && !containsParent(marker)) {
                    newParents[insertIndex++] = marker;
                }
            }
            
            // Trim array if we skipped duplicates
            if (insertIndex < newParents.length) {
                Marker[] trimmed = new Marker[insertIndex];
                System.arraycopy(newParents, 0, trimmed, 0, insertIndex);
                this.parents = trimmed;
            } else {
                this.parents = newParents;
            }
        }
        
        return this;
    }
    
    /**
     * Removes a parent marker.
     */
    public boolean removeParent(Marker marker) {
        if (marker == null || parents.length == 0) {
            return false;
        }
        
        synchronized (this) {
            int removeIndex = -1;
            for (int i = 0; i < parents.length; i++) {
                if (parents[i].equals(marker)) {
                    removeIndex = i;
                    break;
                }
            }
            
            if (removeIndex == -1) {
                return false;
            }
            
            // Create new array without the removed parent
            Marker[] newParents = new Marker[parents.length - 1];
            System.arraycopy(parents, 0, newParents, 0, removeIndex);
            System.arraycopy(parents, removeIndex + 1, newParents, removeIndex, 
                           parents.length - removeIndex - 1);
            
            this.parents = newParents;
            return true;
        }
    }
    
    /**
     * Gets parent markers.
     */
    public Marker[] getParents() {
        return parents.clone();
    }
    
    /**
     * Checks if this marker has parents.
     */
    public boolean hasParents() {
        return parents.length > 0;
    }
    
    /**
     * Checks if this marker is an instance of the specified marker.
     * Checks the marker hierarchy recursively.
     */
    public boolean isInstanceOf(Marker marker) {
        if (marker == null) {
            return false;
        }
        
        if (this.equals(marker)) {
            return true;
        }
        
        // Check parent markers recursively
        for (Marker parent : parents) {
            if (parent.isInstanceOf(marker)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if this marker is an instance of the specified marker name.
     */
    public boolean isInstanceOf(String markerName) {
        if (markerName == null) {
            return false;
        }
        
        if (this.name.equals(markerName)) {
            return true;
        }
        
        // Check parent markers recursively
        for (Marker parent : parents) {
            if (parent.isInstanceOf(markerName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Helper method to check if a marker is already a parent.
     */
    private boolean containsParent(Marker marker) {
        for (Marker parent : parents) {
            if (parent.equals(marker)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Marker marker = (Marker) obj;
        return name.equals(marker.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return name;
    }
}

