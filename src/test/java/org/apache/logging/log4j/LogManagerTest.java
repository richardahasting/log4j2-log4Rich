package org.apache.logging.log4j;

import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link LogManager} - the main entry point for obtaining loggers.
 */
class LogManagerTest {

    @Test
    void getLoggerByName() {
        Logger logger = LogManager.getLogger("test.LogManagerTest");
        assertNotNull(logger);
        assertEquals("test.LogManagerTest", logger.getName());
    }

    @Test
    void getLoggerByClass() {
        Logger logger = LogManager.getLogger(LogManagerTest.class);
        assertNotNull(logger);
        assertEquals(LogManagerTest.class.getName(), logger.getName());
    }

    @Test
    void getLoggerReturnsSameInstance() {
        Logger logger1 = LogManager.getLogger("test.Same");
        Logger logger2 = LogManager.getLogger("test.Same");
        assertSame(logger1, logger2);
    }

    @Test
    void getLoggerReturnsExtendedLogger() {
        Logger logger = LogManager.getLogger("test.Extended");
        assertInstanceOf(ExtendedLogger.class, logger);
    }

    @Test
    void getContextReturnsNonNull() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        assertNotNull(ctx);
    }

    @Test
    void getRootLoggerName() {
        Logger root = LogManager.getRootLogger();
        assertNotNull(root);
    }
}
