package org.apache.logging.log4j;

import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.FlowMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.function.Supplier;

/**
 * log4j2 Logger interface - required for SLF4J binding compatibility.
 * ExtendedLogger and other SPI classes must be assignable to this interface.
 */
public interface Logger {

    String getName();

    MessageFactory getMessageFactory();

    /**
     * Gets the current logging level for this logger.
     *
     * @return the current Level.
     */
    Level getLevel();

    // Level checking
    boolean isTraceEnabled();
    boolean isTraceEnabled(Marker marker);
    boolean isDebugEnabled();
    boolean isDebugEnabled(Marker marker);
    boolean isInfoEnabled();
    boolean isInfoEnabled(Marker marker);
    boolean isWarnEnabled();
    boolean isWarnEnabled(Marker marker);
    boolean isErrorEnabled();
    boolean isErrorEnabled(Marker marker);
    boolean isFatalEnabled();
    boolean isFatalEnabled(Marker marker);
    boolean isEnabled(Level level);
    boolean isEnabled(Level level, Marker marker);

    // ========== TRACE ==========
    void trace(String message);
    void trace(String message, Object param);
    void trace(String message, Object param1, Object param2);
    void trace(String message, Object... params);
    void trace(String message, Throwable throwable);
    void trace(Marker marker, String message);
    void trace(Marker marker, String message, Object param);
    void trace(Marker marker, String message, Object param1, Object param2);
    void trace(Marker marker, String message, Object... params);
    void trace(Marker marker, String message, Throwable throwable);
    void trace(Message message);
    void trace(Message message, Throwable throwable);
    void trace(Marker marker, Message message);
    void trace(Marker marker, Message message, Throwable throwable);
    void trace(Supplier<?> supplier);
    void trace(Supplier<?> supplier, Throwable throwable);
    void trace(Marker marker, Supplier<?> supplier);
    void trace(Marker marker, Supplier<?> supplier, Throwable throwable);
    void trace(CharSequence message);
    void trace(CharSequence message, Throwable throwable);
    void trace(Marker marker, CharSequence message);
    void trace(Marker marker, CharSequence message, Throwable throwable);
    void trace(Object message);
    void trace(Object message, Throwable throwable);
    void trace(Marker marker, Object message);
    void trace(Marker marker, Object message, Throwable throwable);

    // ========== DEBUG ==========
    void debug(String message);
    void debug(String message, Object param);
    void debug(String message, Object param1, Object param2);
    void debug(String message, Object... params);
    void debug(String message, Throwable throwable);
    void debug(Marker marker, String message);
    void debug(Marker marker, String message, Object param);
    void debug(Marker marker, String message, Object param1, Object param2);
    void debug(Marker marker, String message, Object... params);
    void debug(Marker marker, String message, Throwable throwable);
    void debug(Message message);
    void debug(Message message, Throwable throwable);
    void debug(Marker marker, Message message);
    void debug(Marker marker, Message message, Throwable throwable);
    void debug(Supplier<?> supplier);
    void debug(Supplier<?> supplier, Throwable throwable);
    void debug(Marker marker, Supplier<?> supplier);
    void debug(Marker marker, Supplier<?> supplier, Throwable throwable);
    void debug(CharSequence message);
    void debug(CharSequence message, Throwable throwable);
    void debug(Marker marker, CharSequence message);
    void debug(Marker marker, CharSequence message, Throwable throwable);
    void debug(Object message);
    void debug(Object message, Throwable throwable);
    void debug(Marker marker, Object message);
    void debug(Marker marker, Object message, Throwable throwable);

    // ========== INFO ==========
    void info(String message);
    void info(String message, Object param);
    void info(String message, Object param1, Object param2);
    void info(String message, Object... params);
    void info(String message, Throwable throwable);
    void info(Marker marker, String message);
    void info(Marker marker, String message, Object param);
    void info(Marker marker, String message, Object param1, Object param2);
    void info(Marker marker, String message, Object... params);
    void info(Marker marker, String message, Throwable throwable);
    void info(Message message);
    void info(Message message, Throwable throwable);
    void info(Marker marker, Message message);
    void info(Marker marker, Message message, Throwable throwable);
    void info(Supplier<?> supplier);
    void info(Supplier<?> supplier, Throwable throwable);
    void info(Marker marker, Supplier<?> supplier);
    void info(Marker marker, Supplier<?> supplier, Throwable throwable);
    void info(CharSequence message);
    void info(CharSequence message, Throwable throwable);
    void info(Marker marker, CharSequence message);
    void info(Marker marker, CharSequence message, Throwable throwable);
    void info(Object message);
    void info(Object message, Throwable throwable);
    void info(Marker marker, Object message);
    void info(Marker marker, Object message, Throwable throwable);

    // ========== WARN ==========
    void warn(String message);
    void warn(String message, Object param);
    void warn(String message, Object param1, Object param2);
    void warn(String message, Object... params);
    void warn(String message, Throwable throwable);
    void warn(Marker marker, String message);
    void warn(Marker marker, String message, Object param);
    void warn(Marker marker, String message, Object param1, Object param2);
    void warn(Marker marker, String message, Object... params);
    void warn(Marker marker, String message, Throwable throwable);
    void warn(Message message);
    void warn(Message message, Throwable throwable);
    void warn(Marker marker, Message message);
    void warn(Marker marker, Message message, Throwable throwable);
    void warn(Supplier<?> supplier);
    void warn(Supplier<?> supplier, Throwable throwable);
    void warn(Marker marker, Supplier<?> supplier);
    void warn(Marker marker, Supplier<?> supplier, Throwable throwable);
    void warn(CharSequence message);
    void warn(CharSequence message, Throwable throwable);
    void warn(Marker marker, CharSequence message);
    void warn(Marker marker, CharSequence message, Throwable throwable);
    void warn(Object message);
    void warn(Object message, Throwable throwable);
    void warn(Marker marker, Object message);
    void warn(Marker marker, Object message, Throwable throwable);

    // ========== ERROR ==========
    void error(String message);
    void error(String message, Object param);
    void error(String message, Object param1, Object param2);
    void error(String message, Object... params);
    void error(String message, Throwable throwable);
    void error(Marker marker, String message);
    void error(Marker marker, String message, Object param);
    void error(Marker marker, String message, Object param1, Object param2);
    void error(Marker marker, String message, Object... params);
    void error(Marker marker, String message, Throwable throwable);
    void error(Message message);
    void error(Message message, Throwable throwable);
    void error(Marker marker, Message message);
    void error(Marker marker, Message message, Throwable throwable);
    void error(Supplier<?> supplier);
    void error(Supplier<?> supplier, Throwable throwable);
    void error(Marker marker, Supplier<?> supplier);
    void error(Marker marker, Supplier<?> supplier, Throwable throwable);
    void error(CharSequence message);
    void error(CharSequence message, Throwable throwable);
    void error(Marker marker, CharSequence message);
    void error(Marker marker, CharSequence message, Throwable throwable);
    void error(Object message);
    void error(Object message, Throwable throwable);
    void error(Marker marker, Object message);
    void error(Marker marker, Object message, Throwable throwable);

    // ========== FATAL ==========
    void fatal(String message);
    void fatal(String message, Object param);
    void fatal(String message, Object param1, Object param2);
    void fatal(String message, Object... params);
    void fatal(String message, Throwable throwable);
    void fatal(Marker marker, String message);
    void fatal(Marker marker, String message, Object param);
    void fatal(Marker marker, String message, Object param1, Object param2);
    void fatal(Marker marker, String message, Object... params);
    void fatal(Marker marker, String message, Throwable throwable);
    void fatal(Message message);
    void fatal(Message message, Throwable throwable);
    void fatal(Marker marker, Message message);
    void fatal(Marker marker, Message message, Throwable throwable);
    void fatal(Supplier<?> supplier);
    void fatal(Supplier<?> supplier, Throwable throwable);
    void fatal(Marker marker, Supplier<?> supplier);
    void fatal(Marker marker, Supplier<?> supplier, Throwable throwable);
    void fatal(CharSequence message);
    void fatal(CharSequence message, Throwable throwable);
    void fatal(Marker marker, CharSequence message);
    void fatal(Marker marker, CharSequence message, Throwable throwable);
    void fatal(Object message);
    void fatal(Object message, Throwable throwable);
    void fatal(Marker marker, Object message);
    void fatal(Marker marker, Object message, Throwable throwable);

    // ========== Generic level ==========
    void log(Level level, String message);
    void log(Level level, String message, Object... params);
    void log(Level level, String message, Throwable throwable);
    void log(Level level, Marker marker, String message);
    void log(Level level, Marker marker, String message, Object... params);
    void log(Level level, Marker marker, String message, Throwable throwable);
    void log(Level level, Message message);
    void log(Level level, Message message, Throwable throwable);
    void log(Level level, Marker marker, Message message);
    void log(Level level, Marker marker, Message message, Throwable throwable);
    void log(Level level, Supplier<?> supplier);
    void log(Level level, Supplier<?> supplier, Throwable throwable);
    void log(Level level, Marker marker, Supplier<?> supplier);
    void log(Level level, Marker marker, Supplier<?> supplier, Throwable throwable);
    void log(Level level, CharSequence message);
    void log(Level level, CharSequence message, Throwable throwable);
    void log(Level level, Marker marker, CharSequence message);
    void log(Level level, Marker marker, CharSequence message, Throwable throwable);
    void log(Level level, Object message);
    void log(Level level, Object message, Throwable throwable);
    void log(Level level, Marker marker, Object message);
    void log(Level level, Marker marker, Object message, Throwable throwable);

    // ========== Flow Tracing ==========

    /**
     * Logs a Throwable that has been caught at ERROR level.
     */
    default void catching(Throwable throwable) {
        catching(Level.ERROR, throwable);
    }

    /**
     * Logs a Throwable that has been caught at the specified level.
     */
    default void catching(Level level, Throwable throwable) {
        if (isEnabled(level)) {
            log(level, "Catching", throwable);
        }
    }

    /**
     * Logs a Throwable to be thrown at ERROR level.
     */
    default <T extends Throwable> T throwing(T throwable) {
        return throwing(Level.ERROR, throwable);
    }

    /**
     * Logs a Throwable to be thrown at the specified level.
     */
    default <T extends Throwable> T throwing(Level level, T throwable) {
        if (isEnabled(level)) {
            log(level, "Throwing", throwable);
        }
        return throwable;
    }

    /**
     * Logs method entry at TRACE level.
     */
    default void entry() {
        if (isTraceEnabled()) {
            trace("Enter");
        }
    }

    /**
     * Logs method entry with parameters at TRACE level.
     */
    default void entry(Object... params) {
        if (isTraceEnabled()) {
            if (params == null || params.length == 0) {
                trace("Enter");
            } else {
                StringBuilder sb = new StringBuilder("Enter(");
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(params[i]);
                }
                sb.append(")");
                trace(sb.toString());
            }
        }
    }

    /**
     * Logs method exit at TRACE level.
     */
    default void exit() {
        if (isTraceEnabled()) {
            trace("Exit");
        }
    }

    /**
     * Logs method exit with result at TRACE level.
     */
    default <R> R exit(R result) {
        if (isTraceEnabled()) {
            trace("Exit with result: " + result);
        }
        return result;
    }

    /**
     * Logs entry to a method using a format string at TRACE level.
     */
    default EntryMessage traceEntry(String format, Object... params) {
        EntryMessage entryMessage = getFlowMessageFactory().newEntryMessage(format, params);
        if (isTraceEnabled()) {
            trace(entryMessage.getFormattedMessage());
        }
        return entryMessage;
    }

    /**
     * Logs exit from a method at TRACE level.
     */
    default void traceExit() {
        if (isTraceEnabled()) {
            trace("Exit");
        }
    }

    /**
     * Logs exit from a method with result at TRACE level.
     */
    default <R> R traceExit(R result) {
        if (isTraceEnabled()) {
            Message exitMsg = getFlowMessageFactory().newExitMessage(result, null);
            trace(exitMsg.getFormattedMessage());
        }
        return result;
    }

    /**
     * Logs exit from a method, referencing the entry message, at TRACE level.
     */
    default <R> R traceExit(EntryMessage entryMessage, R result) {
        if (isTraceEnabled()) {
            Message exitMsg = getFlowMessageFactory().newExitMessage(result, entryMessage);
            trace(exitMsg.getFormattedMessage());
        }
        return result;
    }

    /**
     * Logs a message using printf-style formatting.
     */
    default void printf(Level level, String format, Object... args) {
        if (isEnabled(level)) {
            log(level, String.format(format, args));
        }
    }

    /**
     * Logs a message using printf-style formatting with a marker.
     */
    default void printf(Level level, Marker marker, String format, Object... args) {
        if (isEnabled(level, marker)) {
            log(level, marker, String.format(format, args));
        }
    }

    /**
     * Gets the FlowMessageFactory.
     */
    default FlowMessageFactory getFlowMessageFactory() {
        return org.apache.logging.log4j.message.DefaultFlowMessageFactory.INSTANCE;
    }
}
