package org.apache.logging.log4j.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultMessageFactory} and {@link MessageFactory2} interface.
 */
class MessageFactoryTest {

    private final DefaultMessageFactory factory = new DefaultMessageFactory();

    @Test
    void newMessageString() {
        Message msg = factory.newMessage("hello");
        assertNotNull(msg);
        assertEquals("hello", msg.getFormattedMessage());
    }

    @Test
    void newMessageStringWithOneParam() {
        Message msg = factory.newMessage("value is {}", 42);
        assertNotNull(msg);
        String formatted = msg.getFormattedMessage();
        assertTrue(formatted.contains("42"), "Should contain parameter: " + formatted);
    }

    @Test
    void newMessageObject() {
        Message msg = factory.newMessage((Object) "object message");
        assertNotNull(msg);
        assertEquals("object message", msg.getFormattedMessage());
    }

    @Test
    void newMessageNullSafe() {
        Message msg = factory.newMessage((String) null);
        assertNotNull(msg);
    }

    // ========== MessageFactory2 optimized overloads ==========

    @Test
    void instanceImplementsMessageFactory2() {
        assertInstanceOf(MessageFactory2.class, factory);
    }

    @Test
    void newMessageWithOneParam() {
        MessageFactory2 mf2 = factory;
        Message msg = mf2.newMessage("key={}", "value");
        assertNotNull(msg);
    }

    @Test
    void newMessageWithTwoParams() {
        MessageFactory2 mf2 = factory;
        Message msg = mf2.newMessage("{} + {}", 1, 2);
        assertNotNull(msg);
    }

    @Test
    void newMessageWithThreeParams() {
        MessageFactory2 mf2 = factory;
        Message msg = mf2.newMessage("{} {} {}", "a", "b", "c");
        assertNotNull(msg);
    }

    // ========== Message interface ==========

    @Test
    void messageGetFormat() {
        Message msg = factory.newMessage("format {} here");
        assertNotNull(msg.getFormat());
    }

    @Test
    void messageGetParameters() {
        Message msg = factory.newMessage("test {} {}", "a", "b");
        Object[] params = msg.getParameters();
        assertNotNull(params);
    }

    @Test
    void messageGetThrowable() {
        // SimpleMessage typically returns null for throwable
        Message msg = factory.newMessage("test");
        // Just verify it doesn't throw
        msg.getThrowable();
    }
}
