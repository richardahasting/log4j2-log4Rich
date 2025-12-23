package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * Extended Logger interface required by log4j-slf4j2-impl.
 * Extends Logger to maintain type compatibility.
 * Adds the logMessage method for location-aware logging.
 */
public interface ExtendedLogger extends org.apache.logging.log4j.Logger {

    /**
     * Logs a message at the given level with the specified marker.
     *
     * @param fqcn The fully qualified class name of the logger wrapper.
     * @param level The logging level.
     * @param marker The marker to use.
     * @param message The message to log.
     * @param throwable The throwable to log.
     */
    void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable throwable);

    /**
     * Returns true if the given level is enabled.
     *
     * @param level The level to check.
     * @param marker The marker to check.
     * @param message The message to check.
     * @param throwable The throwable to check.
     * @return true if enabled.
     */
    boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable);

    /**
     * Returns true if the given level is enabled.
     *
     * @param level The level to check.
     * @param marker The marker to check.
     * @param message The message to check.
     * @return true if enabled.
     */
    boolean isEnabled(Level level, Marker marker, String message);

    /**
     * Returns true if the given level is enabled.
     *
     * @param level The level to check.
     * @param marker The marker to check.
     * @param message The message to check.
     * @param params The message parameters.
     * @return true if enabled.
     */
    boolean isEnabled(Level level, Marker marker, String message, Object... params);

    /**
     * Returns true if the given level is enabled.
     *
     * @param level The level to check.
     * @param marker The marker to check.
     * @param message The message to check.
     * @param throwable The throwable to check.
     * @return true if enabled.
     */
    boolean isEnabled(Level level, Marker marker, String message, Throwable throwable);

    /**
     * Returns true if the given level is enabled.
     *
     * @param level The level to check.
     * @param marker The marker to check.
     * @param message The message object.
     * @return true if enabled.
     */
    boolean isEnabled(Level level, Marker marker, Object message, Throwable throwable);

    /**
     * Returns true if the given level is enabled.
     *
     * @param level The level to check.
     * @param marker The marker to check.
     * @param message The character sequence.
     * @param throwable The throwable to check.
     * @return true if enabled.
     */
    boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable throwable);

    /**
     * Gets the logger name.
     *
     * @return The logger name.
     */
    String getName();

    /**
     * Gets the message factory.
     *
     * @return The message factory.
     */
    MessageFactory getMessageFactory();

    /**
     * Gets the logging level.
     *
     * @return The logging level.
     */
    Level getLevel();

    // ========== logIfEnabled methods ==========

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable throwable);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, Message message, Throwable throwable);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable);

    /**
     * Logs a message if the specified level is enabled.
     */
    void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable throwable);
}
