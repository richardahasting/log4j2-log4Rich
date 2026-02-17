package org.apache.logging.log4j.message;

/**
 * Default message factory implementation.
 * Creates appropriate message types based on input.
 * Implements {@link MessageFactory2} for optimized single-parameter overloads.
 */
public class DefaultMessageFactory implements MessageFactory2 {

    public static final MessageFactory INSTANCE = new DefaultMessageFactory();

    @Override
    public Message newMessage(Object message) {
        if (message == null) {
            return new SimpleMessage("null");
        }

        if (message instanceof String) {
            return new SimpleMessage((String) message);
        }

        if (message instanceof Message) {
            return (Message) message;
        }

        return new ObjectMessage(message);
    }

    @Override
    public Message newMessage(String message, Object... params) {
        if (params == null || params.length == 0) {
            return new SimpleMessage(message);
        }

        return new ParameterizedMessage(message, params);
    }

    @Override
    public Message newMessage(CharSequence charSequence) {
        return new SimpleMessage(charSequence != null ? charSequence.toString() : null);
    }

    @Override
    public Message newMessage(String message, Object p0) {
        return new ParameterizedMessage(message, p0);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1) {
        return new ParameterizedMessage(message, p0, p1);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3, p4});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3, p4, p5});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3, p4, p5, p6});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3, p4, p5, p6, p7});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3, p4, p5, p6, p7, p8});
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return new ParameterizedMessage(message, new Object[]{p0, p1, p2, p3, p4, p5, p6, p7, p8, p9});
    }
}
