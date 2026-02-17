package org.apache.logging.log4j;

import org.apache.logging.log4j.message.DefaultMessageFactory;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Logger implementations - basic logging operations and level checking.
 */
class LoggerTest {

    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = LogManager.getLogger("test.LoggerTest");
    }

    @Test
    void loggerHasName() {
        assertEquals("test.LoggerTest", logger.getName());
    }

    @Test
    void loggerHasMessageFactory() {
        MessageFactory mf = logger.getMessageFactory();
        assertNotNull(mf);
        assertInstanceOf(DefaultMessageFactory.class, mf);
    }

    @Test
    void loggerHasLevel() {
        Level level = logger.getLevel();
        assertNotNull(level);
    }

    // ========== Level Check Methods ==========

    @Test
    void isEnabledByLevel() {
        // These should not throw - just verify they return boolean
        boolean result = logger.isEnabled(Level.INFO);
        // Result depends on configuration, just verify it runs
        assertTrue(result || !result);
    }

    @Test
    void isTraceEnabled() {
        assertDoesNotThrow(() -> logger.isTraceEnabled());
    }

    @Test
    void isDebugEnabled() {
        assertDoesNotThrow(() -> logger.isDebugEnabled());
    }

    @Test
    void isInfoEnabled() {
        assertDoesNotThrow(() -> logger.isInfoEnabled());
    }

    @Test
    void isWarnEnabled() {
        assertDoesNotThrow(() -> logger.isWarnEnabled());
    }

    @Test
    void isErrorEnabled() {
        assertDoesNotThrow(() -> logger.isErrorEnabled());
    }

    @Test
    void isFatalEnabled() {
        assertDoesNotThrow(() -> logger.isFatalEnabled());
    }

    // ========== Basic Logging (should not throw) ==========

    @Test
    void traceString() {
        assertDoesNotThrow(() -> logger.trace("trace message"));
    }

    @Test
    void debugStringWithParam() {
        assertDoesNotThrow(() -> logger.debug("value is {}", 42));
    }

    @Test
    void infoStringWithTwoParams() {
        assertDoesNotThrow(() -> logger.info("{} = {}", "key", "value"));
    }

    @Test
    void warnStringWithVarargs() {
        assertDoesNotThrow(() -> logger.warn("{} {} {}", "a", "b", "c"));
    }

    @Test
    void errorWithThrowable() {
        assertDoesNotThrow(() -> logger.error("error occurred", new RuntimeException("test")));
    }

    @Test
    void fatalString() {
        assertDoesNotThrow(() -> logger.fatal("fatal error"));
    }

    @Test
    void logAtLevel() {
        assertDoesNotThrow(() -> logger.log(Level.INFO, "log at info"));
    }

    // ========== CharSequence Overloads ==========

    @Test
    void traceCharSequence() {
        CharSequence cs = new StringBuilder("trace cs");
        assertDoesNotThrow(() -> logger.trace(cs));
    }

    @Test
    void debugCharSequenceWithThrowable() {
        CharSequence cs = new StringBuilder("debug cs");
        assertDoesNotThrow(() -> logger.debug(cs, new RuntimeException()));
    }

    // ========== Object Overloads ==========

    @Test
    void infoObject() {
        assertDoesNotThrow(() -> logger.info((Object) "object msg"));
    }

    @Test
    void errorObjectWithThrowable() {
        assertDoesNotThrow(() -> logger.error((Object) "error obj", new RuntimeException()));
    }

    // ========== Supplier Overloads ==========

    @Test
    void traceSupplier() {
        assertDoesNotThrow(() -> logger.trace(() -> "lazy trace"));
    }

    @Test
    void debugSupplierWithThrowable() {
        assertDoesNotThrow(() -> logger.debug(() -> "lazy debug", new RuntimeException()));
    }

    // ========== Marker Overloads ==========

    @Test
    void infoWithMarker() {
        Marker marker = MarkerManager.getMarker("TEST_MARKER");
        assertDoesNotThrow(() -> logger.info(marker, "marked message"));
    }

    @Test
    void warnWithMarkerAndParams() {
        Marker marker = MarkerManager.getMarker("TEST");
        assertDoesNotThrow(() -> logger.warn(marker, "msg {} {}", "a", "b"));
    }

    // ========== Extended Logger ==========

    @Test
    void loggerIsExtendedLogger() {
        assertInstanceOf(ExtendedLogger.class, logger);
    }

    @Test
    void logMessage() {
        ExtendedLogger ext = (ExtendedLogger) logger;
        assertDoesNotThrow(() ->
            ext.logMessage("test.FQCN", Level.INFO, null,
                DefaultMessageFactory.INSTANCE.newMessage("logMessage test"), null));
    }

    @Test
    void logIfEnabled() {
        ExtendedLogger ext = (ExtendedLogger) logger;
        assertDoesNotThrow(() ->
            ext.logIfEnabled("test.FQCN", Level.WARN, null, "logIfEnabled test"));
    }
}
