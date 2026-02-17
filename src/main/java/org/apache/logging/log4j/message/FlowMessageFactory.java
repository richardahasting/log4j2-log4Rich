package org.apache.logging.log4j.message;

/**
 * Factory for creating flow tracing messages (entry/exit).
 *
 * @since 1.0.5
 */
public interface FlowMessageFactory {

    /**
     * Creates an entry message.
     *
     * @param message the entry message text, or null
     * @param params parameters for the message
     * @return a new EntryMessage
     */
    EntryMessage newEntryMessage(String message, Object... params);

    /**
     * Creates an exit message.
     *
     * @param result the return value, or null
     * @param entryMessage the corresponding entry message, or null
     * @return a new Message representing the exit
     */
    Message newExitMessage(Object result, EntryMessage entryMessage);
}
