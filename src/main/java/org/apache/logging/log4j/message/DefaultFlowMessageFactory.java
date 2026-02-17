package org.apache.logging.log4j.message;

/**
 * Default implementation of {@link FlowMessageFactory}.
 *
 * @since 1.0.5
 */
public class DefaultFlowMessageFactory implements FlowMessageFactory {

    public static final FlowMessageFactory INSTANCE = new DefaultFlowMessageFactory();

    @Override
    public EntryMessage newEntryMessage(String message, Object... params) {
        if (message == null && (params == null || params.length == 0)) {
            return new EntryMessage(null);
        }
        Message inner;
        if (params != null && params.length > 0) {
            inner = new ParameterizedMessage(message, params);
        } else {
            inner = new SimpleMessage(message);
        }
        return new EntryMessage(inner);
    }

    @Override
    public Message newExitMessage(Object result, EntryMessage entryMessage) {
        if (result == null && entryMessage == null) {
            return new SimpleMessage("Exit");
        }
        if (result == null) {
            return new SimpleMessage("Exit " + entryMessage.getFormattedMessage());
        }
        String msg = entryMessage != null
                ? "Exit " + entryMessage.getFormattedMessage() + " with result: " + result
                : "Exit with result: " + result;
        return new SimpleMessage(msg);
    }
}
