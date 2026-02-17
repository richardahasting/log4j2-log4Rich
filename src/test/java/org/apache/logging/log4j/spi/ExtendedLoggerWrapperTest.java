package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.DefaultMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ExtendedLoggerWrapper} and {@link AbstractLogger}.
 */
class ExtendedLoggerWrapperTest {

    @Test
    void wrapperDelegatesToInnerLogger() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper", null);

        assertEquals("test.wrapper", wrapper.getName());
        assertNotNull(wrapper.getLevel());
    }

    @Test
    void wrapperPreservesMessageFactory() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner2");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper2",
            DefaultMessageFactory.INSTANCE);

        assertSame(DefaultMessageFactory.INSTANCE, wrapper.getMessageFactory());
    }

    @Test
    void wrapperDelegatesIsEnabled() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner3");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper3", null);

        // Should delegate to inner logger
        boolean enabled = wrapper.isEnabled(Level.INFO, null, (Message) null, null);
        assertEquals(inner.isEnabled(Level.INFO), enabled);
    }

    @Test
    void wrapperLogging() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner4");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper4", null);

        // All logging methods should work without throwing
        assertDoesNotThrow(() -> wrapper.info("info from wrapper"));
        assertDoesNotThrow(() -> wrapper.debug("debug {} {}", "a", "b"));
        assertDoesNotThrow(() -> wrapper.error("error", new RuntimeException("test")));
    }

    @Test
    void wrapperLevelChecks() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner5");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper5", null);

        assertDoesNotThrow(() -> wrapper.isTraceEnabled());
        assertDoesNotThrow(() -> wrapper.isDebugEnabled());
        assertDoesNotThrow(() -> wrapper.isInfoEnabled());
        assertDoesNotThrow(() -> wrapper.isWarnEnabled());
        assertDoesNotThrow(() -> wrapper.isErrorEnabled());
        assertDoesNotThrow(() -> wrapper.isFatalEnabled());
    }

    @Test
    void wrapperLogIfEnabled() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner6");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper6", null);

        assertDoesNotThrow(() ->
            wrapper.logIfEnabled("test.FQCN", Level.INFO, null, "logIfEnabled test"));
        assertDoesNotThrow(() ->
            wrapper.logIfEnabled("test.FQCN", Level.WARN, null, "msg {}", "param"));
    }

    @Test
    void wrapperLogMessage() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner7");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper7", null);

        assertDoesNotThrow(() ->
            wrapper.logMessage("test.FQCN", Level.ERROR, null,
                DefaultMessageFactory.INSTANCE.newMessage("direct logMessage"), null));
    }

    @Test
    void wrapperCharSequenceOverloads() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner8");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper8", null);

        CharSequence cs = new StringBuilder("charseq");
        assertDoesNotThrow(() -> wrapper.info(cs));
        assertDoesNotThrow(() -> wrapper.warn(cs, new RuntimeException()));
    }

    @Test
    void wrapperObjectOverloads() {
        ExtendedLogger inner = (ExtendedLogger) LogManager.getLogger("test.inner9");
        ExtendedLoggerWrapper wrapper = new ExtendedLoggerWrapper(inner, "test.wrapper9", null);

        assertDoesNotThrow(() -> wrapper.debug((Object) "object msg"));
        assertDoesNotThrow(() -> wrapper.error((Object) "error obj", new RuntimeException()));
    }
}
