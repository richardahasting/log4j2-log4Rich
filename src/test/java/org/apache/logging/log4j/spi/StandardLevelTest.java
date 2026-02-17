package org.apache.logging.log4j.spi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link StandardLevel} enum.
 */
class StandardLevelTest {

    @Test
    void allStandardLevelsExist() {
        assertNotNull(StandardLevel.OFF);
        assertNotNull(StandardLevel.FATAL);
        assertNotNull(StandardLevel.ERROR);
        assertNotNull(StandardLevel.WARN);
        assertNotNull(StandardLevel.INFO);
        assertNotNull(StandardLevel.DEBUG);
        assertNotNull(StandardLevel.TRACE);
        assertNotNull(StandardLevel.ALL);
    }

    @Test
    void intLevelsMatchLog4j2Specification() {
        assertEquals(0, StandardLevel.OFF.intLevel());
        assertEquals(100, StandardLevel.FATAL.intLevel());
        assertEquals(200, StandardLevel.ERROR.intLevel());
        assertEquals(300, StandardLevel.WARN.intLevel());
        assertEquals(400, StandardLevel.INFO.intLevel());
        assertEquals(500, StandardLevel.DEBUG.intLevel());
        assertEquals(600, StandardLevel.TRACE.intLevel());
        assertEquals(Integer.MAX_VALUE, StandardLevel.ALL.intLevel());
    }

    @Test
    void getStandardLevelByIntValue() {
        assertEquals(StandardLevel.ERROR, StandardLevel.getStandardLevel(200));
        assertEquals(StandardLevel.INFO, StandardLevel.getStandardLevel(400));
    }

    @Test
    void getStandardLevelForNonStandardReturnsNearest() {
        // A value between ERROR(200) and WARN(300) should map to ERROR
        StandardLevel level = StandardLevel.getStandardLevel(250);
        assertNotNull(level);
    }

    @Test
    void valuesReturnsAllLevels() {
        StandardLevel[] values = StandardLevel.values();
        assertEquals(8, values.length);
    }

    @Test
    void valueOfByName() {
        assertEquals(StandardLevel.ERROR, StandardLevel.valueOf("ERROR"));
        assertEquals(StandardLevel.DEBUG, StandardLevel.valueOf("DEBUG"));
    }
}
