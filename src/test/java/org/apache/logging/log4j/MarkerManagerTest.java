package org.apache.logging.log4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link MarkerManager} and {@link Marker}.
 */
class MarkerManagerTest {

    @Test
    void getMarkerByName() {
        Marker marker = MarkerManager.getMarker("TEST");
        assertNotNull(marker);
        assertEquals("TEST", marker.getName());
    }

    @Test
    void sameNameReturnsSameMarker() {
        Marker m1 = MarkerManager.getMarker("SAME");
        Marker m2 = MarkerManager.getMarker("SAME");
        assertSame(m1, m2);
    }

    @Test
    void markerWithParent() {
        Marker parent = MarkerManager.getMarker("PARENT");
        Marker child = MarkerManager.getMarker("CHILD").addParents(parent);
        assertTrue(child.isInstanceOf(parent));
        assertTrue(child.isInstanceOf("PARENT"));
    }

    @Test
    void markerIsNotInstanceOfUnrelated() {
        Marker m1 = MarkerManager.getMarker("M1");
        Marker m2 = MarkerManager.getMarker("M2");
        assertFalse(m1.isInstanceOf(m2));
    }

    @Test
    void markerIsInstanceOfSelf() {
        Marker marker = MarkerManager.getMarker("SELF_TEST");
        assertTrue(marker.isInstanceOf(marker));
        assertTrue(marker.isInstanceOf("SELF_TEST"));
    }

    @Test
    void hasParents() {
        Marker parent = MarkerManager.getMarker("HAS_PARENT");
        Marker child = MarkerManager.getMarker("HAS_CHILD").addParents(parent);
        assertTrue(child.hasParents());
    }
}
