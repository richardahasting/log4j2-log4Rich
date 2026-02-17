package org.apache.logging.log4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Level} - logging level representation.
 */
class LevelTest {

    @Test
    void standardLevelsExist() {
        assertNotNull(Level.OFF);
        assertNotNull(Level.FATAL);
        assertNotNull(Level.ERROR);
        assertNotNull(Level.WARN);
        assertNotNull(Level.INFO);
        assertNotNull(Level.DEBUG);
        assertNotNull(Level.TRACE);
        assertNotNull(Level.ALL);
    }

    @Test
    void levelOrdering() {
        assertTrue(Level.FATAL.intLevel() < Level.ERROR.intLevel());
        assertTrue(Level.ERROR.intLevel() < Level.WARN.intLevel());
        assertTrue(Level.WARN.intLevel() < Level.INFO.intLevel());
        assertTrue(Level.INFO.intLevel() < Level.DEBUG.intLevel());
        assertTrue(Level.DEBUG.intLevel() < Level.TRACE.intLevel());
    }

    @Test
    void offIsLeastPermissive() {
        assertTrue(Level.OFF.intLevel() < Level.FATAL.intLevel());
    }

    @Test
    void allIsMostPermissive() {
        assertTrue(Level.ALL.intLevel() > Level.TRACE.intLevel());
    }

    @Test
    void isMoreSpecificThan() {
        assertTrue(Level.ERROR.isMoreSpecificThan(Level.WARN));
        assertTrue(Level.FATAL.isMoreSpecificThan(Level.ERROR));
        assertFalse(Level.DEBUG.isMoreSpecificThan(Level.INFO));
    }

    @Test
    void isLessSpecificThan() {
        assertTrue(Level.WARN.isLessSpecificThan(Level.ERROR));
        assertTrue(Level.DEBUG.isLessSpecificThan(Level.INFO));
        assertFalse(Level.ERROR.isLessSpecificThan(Level.WARN));
    }

    @Test
    void isInRange() {
        assertTrue(Level.WARN.isInRange(Level.ERROR, Level.DEBUG));
        assertTrue(Level.ERROR.isInRange(Level.ERROR, Level.DEBUG));
        assertTrue(Level.DEBUG.isInRange(Level.ERROR, Level.DEBUG));
        assertFalse(Level.TRACE.isInRange(Level.ERROR, Level.DEBUG));
        assertFalse(Level.FATAL.isInRange(Level.ERROR, Level.DEBUG));
    }

    @Test
    void nameReturnsCorrectValue() {
        assertEquals("ERROR", Level.ERROR.name());
        assertEquals("WARN", Level.WARN.name());
        assertEquals("INFO", Level.INFO.name());
        assertEquals("DEBUG", Level.DEBUG.name());
        assertEquals("TRACE", Level.TRACE.name());
    }

    @Test
    void toStringReturnsName() {
        assertEquals("ERROR", Level.ERROR.toString());
    }

    @Test
    void toLevelByName() {
        assertEquals(Level.ERROR, Level.toLevel("ERROR"));
        assertEquals(Level.WARN, Level.toLevel("WARN"));
        assertEquals(Level.INFO, Level.toLevel("INFO"));
    }

    @Test
    void toLevelCaseInsensitive() {
        assertEquals(Level.ERROR, Level.toLevel("error"));
        assertEquals(Level.DEBUG, Level.toLevel("Debug"));
    }

    @Test
    void toLevelUnknownCreatesCustomLevel() {
        // Our implementation creates custom levels for unknown names
        Level unknown = Level.toLevel("UNKNOWN");
        assertNotNull(unknown);
        assertEquals("UNKNOWN", unknown.name());
    }

    @Test
    void toLevelWithDefaultForNull() {
        // Default is used when name is null
        assertEquals(Level.WARN, Level.toLevel(null, Level.WARN));
    }

    @Test
    void getLevel() {
        assertSame(Level.ERROR, Level.getLevel("ERROR"));
        // Our getLevel creates custom levels for unknown names
        Level custom = Level.getLevel("CUSTOM_LEVEL");
        assertNotNull(custom);
        assertEquals("CUSTOM_LEVEL", custom.name());
    }

    @Test
    void getLevelReturnsNullForNull() {
        assertNull(Level.getLevel(null));
    }

    @Test
    void toLevelByStandardName() {
        assertEquals(Level.FATAL, Level.toLevel("FATAL"));
        assertEquals(Level.TRACE, Level.toLevel("TRACE"));
    }
}
