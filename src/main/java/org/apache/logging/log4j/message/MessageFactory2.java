package org.apache.logging.log4j.message;

/**
 * Extension of {@link MessageFactory} with optimized overloads that avoid
 * varargs array allocation. Newer log4j2 code checks for this interface
 * to use the more efficient single-parameter methods.
 *
 * @since 1.0.5
 */
public interface MessageFactory2 extends MessageFactory {

    /**
     * Creates a message from a CharSequence.
     */
    Message newMessage(CharSequence charSequence);

    /**
     * Creates a message with 1 parameter (avoids varargs allocation).
     */
    Message newMessage(String message, Object p0);

    /**
     * Creates a message with 2 parameters.
     */
    Message newMessage(String message, Object p0, Object p1);

    /**
     * Creates a message with 3 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2);

    /**
     * Creates a message with 4 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3);

    /**
     * Creates a message with 5 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4);

    /**
     * Creates a message with 6 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);

    /**
     * Creates a message with 7 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);

    /**
     * Creates a message with 8 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);

    /**
     * Creates a message with 9 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);

    /**
     * Creates a message with 10 parameters.
     */
    Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);
}
