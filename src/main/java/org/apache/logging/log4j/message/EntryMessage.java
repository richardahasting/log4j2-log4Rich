package org.apache.logging.log4j.message;

/**
 * Message used for flow tracing entry points.
 * Wraps an inner message with "Enter" semantics.
 *
 * @since 1.0.5
 */
public class EntryMessage implements Message {

    private final Message message;

    public EntryMessage(Message message) {
        this.message = message;
    }

    /**
     * Gets the underlying message.
     *
     * @return the wrapped message
     */
    public Message getMessage() {
        return message;
    }

    @Override
    public String getFormattedMessage() {
        return message != null ? "Enter " + message.getFormattedMessage() : "Enter";
    }

    @Override
    public String getFormat() {
        return message != null ? message.getFormat() : "";
    }

    @Override
    public Object[] getParameters() {
        return message != null ? message.getParameters() : new Object[0];
    }

    @Override
    public Throwable getThrowable() {
        return message != null ? message.getThrowable() : null;
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }
}
