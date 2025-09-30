package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * LoggerContext interface for log4j2 SPI compatibility.
 * This is the anchor point for logging implementations in log4j2.
 *
 * <p>This interface provides methods for retrieving loggers and managing
 * context-specific objects. It is implemented by log4Rich to provide
 * full log4j2 API compatibility.</p>
 *
 * @since 1.0.0
 */
public interface LoggerContext {

    /**
     * Gets a logger with the specified name.
     *
     * @param name the logger name
     * @return the logger instance
     */
    Logger getLogger(String name);

    /**
     * Gets a logger with the specified name and message factory.
     *
     * @param name the logger name
     * @param messageFactory the message factory to use
     * @return the logger instance
     */
    Logger getLogger(String name, MessageFactory messageFactory);

    /**
     * Gets a logger for the specified class.
     *
     * @param cls the class
     * @return the logger instance
     */
    default Logger getLogger(Class<?> cls) {
        return getLogger(cls != null ? cls.getName() : "ROOT");
    }

    /**
     * Gets a logger for the specified class with the specified message factory.
     *
     * @param cls the class
     * @param messageFactory the message factory to use
     * @return the logger instance
     */
    default Logger getLogger(Class<?> cls, MessageFactory messageFactory) {
        return getLogger(cls != null ? cls.getName() : "ROOT", messageFactory);
    }

    /**
     * Checks if a logger with the specified name exists.
     *
     * @param name the logger name
     * @return true if the logger exists, false otherwise
     */
    boolean hasLogger(String name);

    /**
     * Checks if a logger with the specified name and message factory exists.
     *
     * @param name the logger name
     * @param messageFactory the message factory
     * @return true if the logger exists, false otherwise
     */
    boolean hasLogger(String name, MessageFactory messageFactory);

    /**
     * Checks if a logger with the specified name and message factory class exists.
     *
     * @param name the logger name
     * @param messageFactoryClass the message factory class
     * @return true if the logger exists, false otherwise
     */
    boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass);

    /**
     * Gets the external context object.
     *
     * @return the external context, or null if none
     */
    Object getExternalContext();

    /**
     * Gets an object from the context.
     *
     * @param key the object key
     * @return the object, or null if not found
     */
    default Object getObject(String key) {
        return null;
    }

    /**
     * Puts an object into the context.
     *
     * @param key the object key
     * @param value the object value
     * @return the previous value, or null if none
     */
    default Object putObject(String key, Object value) {
        return null;
    }

    /**
     * Puts an object into the context if absent.
     *
     * @param key the object key
     * @param value the object value
     * @return the previous value, or null if none
     */
    default Object putObjectIfAbsent(String key, Object value) {
        return null;
    }

    /**
     * Removes an object from the context.
     *
     * @param key the object key
     * @return the removed object, or null if not found
     */
    default Object removeObject(String key) {
        return null;
    }

    /**
     * Removes an object from the context if it matches the specified value.
     *
     * @param key the object key
     * @param value the expected value
     * @return true if the object was removed, false otherwise
     */
    default boolean removeObject(String key, Object value) {
        return false;
    }
}