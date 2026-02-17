package org.apache.logging.log4j.status;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.DefaultMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;

import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * StatusLogger is used for logging messages from within the logging framework itself.
 * This implements Logger to maintain type compatibility with log4j-slf4j2-impl.
 * It logs to stderr rather than through log4Rich to avoid circular dependencies.
 */
public class StatusLogger implements Logger {

    private static final StatusLogger INSTANCE = new StatusLogger();
    private static final int MAX_STATUS_ENTRIES = 200;

    private final Queue<StatusData> messages = new ConcurrentLinkedQueue<>();
    private volatile Level statusLevel = Level.ERROR;
    private final PrintStream stream = System.err;
    private final String name = "StatusLogger";
    private final MessageFactory messageFactory = DefaultMessageFactory.INSTANCE;

    private StatusLogger() {}

    /**
     * Returns the singleton StatusLogger instance.
     */
    public static StatusLogger getLogger() {
        return INSTANCE;
    }

    /**
     * Sets the logging level for StatusLogger.
     */
    public void setLevel(Level level) {
        this.statusLevel = level != null ? level : Level.ERROR;
    }

    /**
     * Gets the status logging level.
     */
    public Level getStatusLevel() {
        return statusLevel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    @Override
    public Level getLevel() {
        return statusLevel;
    }

    // Level checking - based on status level
    @Override
    public boolean isTraceEnabled() {
        return statusLevel.intLevel() >= Level.TRACE.intLevel();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return statusLevel.intLevel() >= Level.DEBUG.intLevel();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return statusLevel.intLevel() >= Level.INFO.intLevel();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return statusLevel.intLevel() >= Level.WARN.intLevel();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return statusLevel.intLevel() >= Level.ERROR.intLevel();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return statusLevel.intLevel() >= Level.FATAL.intLevel();
    }

    @Override
    public boolean isFatalEnabled(Marker marker) {
        return isFatalEnabled();
    }

    @Override
    public boolean isEnabled(Level level) {
        return level.intLevel() <= statusLevel.intLevel();
    }

    @Override
    public boolean isEnabled(Level level, Marker marker) {
        return isEnabled(level);
    }

    // TRACE
    @Override
    public void trace(String message) {
        logStatus(Level.TRACE, message, null);
    }

    @Override
    public void trace(String message, Object param) {
        logStatus(Level.TRACE, formatMessage(message, param), null);
    }

    @Override
    public void trace(String message, Object param1, Object param2) {
        logStatus(Level.TRACE, formatMessage(message, param1, param2), null);
    }

    @Override
    public void trace(String message, Object... params) {
        logStatus(Level.TRACE, formatMessage(message, params), null);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        logStatus(Level.TRACE, message, throwable);
    }

    @Override
    public void trace(Marker marker, String message) {
        trace(message);
    }

    @Override
    public void trace(Marker marker, String message, Object param) {
        trace(message, param);
    }

    @Override
    public void trace(Marker marker, String message, Object param1, Object param2) {
        trace(message, param1, param2);
    }

    @Override
    public void trace(Marker marker, String message, Object... params) {
        trace(message, params);
    }

    @Override
    public void trace(Marker marker, String message, Throwable throwable) {
        trace(message, throwable);
    }

    @Override
    public void trace(Message message) {
        logStatus(Level.TRACE, message.getFormattedMessage(), null);
    }

    @Override
    public void trace(Message message, Throwable throwable) {
        logStatus(Level.TRACE, message.getFormattedMessage(), throwable);
    }

    @Override
    public void trace(Marker marker, Message message) {
        trace(message);
    }

    @Override
    public void trace(Marker marker, Message message, Throwable throwable) {
        trace(message, throwable);
    }

    @Override
    public void trace(Supplier<?> supplier) {
        if (isTraceEnabled()) {
            logStatus(Level.TRACE, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void trace(Supplier<?> supplier, Throwable throwable) {
        if (isTraceEnabled()) {
            logStatus(Level.TRACE, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void trace(Marker marker, Supplier<?> supplier) {
        trace(supplier);
    }

    @Override
    public void trace(Marker marker, Supplier<?> supplier, Throwable throwable) {
        trace(supplier, throwable);
    }

    // DEBUG
    @Override
    public void debug(String message) {
        logStatus(Level.DEBUG, message, null);
    }

    @Override
    public void debug(String message, Object param) {
        logStatus(Level.DEBUG, formatMessage(message, param), null);
    }

    @Override
    public void debug(String message, Object param1, Object param2) {
        logStatus(Level.DEBUG, formatMessage(message, param1, param2), null);
    }

    @Override
    public void debug(String message, Object... params) {
        logStatus(Level.DEBUG, formatMessage(message, params), null);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logStatus(Level.DEBUG, message, throwable);
    }

    @Override
    public void debug(Marker marker, String message) {
        debug(message);
    }

    @Override
    public void debug(Marker marker, String message, Object param) {
        debug(message, param);
    }

    @Override
    public void debug(Marker marker, String message, Object param1, Object param2) {
        debug(message, param1, param2);
    }

    @Override
    public void debug(Marker marker, String message, Object... params) {
        debug(message, params);
    }

    @Override
    public void debug(Marker marker, String message, Throwable throwable) {
        debug(message, throwable);
    }

    @Override
    public void debug(Message message) {
        logStatus(Level.DEBUG, message.getFormattedMessage(), null);
    }

    @Override
    public void debug(Message message, Throwable throwable) {
        logStatus(Level.DEBUG, message.getFormattedMessage(), throwable);
    }

    @Override
    public void debug(Marker marker, Message message) {
        debug(message);
    }

    @Override
    public void debug(Marker marker, Message message, Throwable throwable) {
        debug(message, throwable);
    }

    @Override
    public void debug(Supplier<?> supplier) {
        if (isDebugEnabled()) {
            logStatus(Level.DEBUG, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void debug(Supplier<?> supplier, Throwable throwable) {
        if (isDebugEnabled()) {
            logStatus(Level.DEBUG, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void debug(Marker marker, Supplier<?> supplier) {
        debug(supplier);
    }

    @Override
    public void debug(Marker marker, Supplier<?> supplier, Throwable throwable) {
        debug(supplier, throwable);
    }

    // INFO
    @Override
    public void info(String message) {
        logStatus(Level.INFO, message, null);
    }

    @Override
    public void info(String message, Object param) {
        logStatus(Level.INFO, formatMessage(message, param), null);
    }

    @Override
    public void info(String message, Object param1, Object param2) {
        logStatus(Level.INFO, formatMessage(message, param1, param2), null);
    }

    @Override
    public void info(String message, Object... params) {
        logStatus(Level.INFO, formatMessage(message, params), null);
    }

    @Override
    public void info(String message, Throwable throwable) {
        logStatus(Level.INFO, message, throwable);
    }

    @Override
    public void info(Marker marker, String message) {
        info(message);
    }

    @Override
    public void info(Marker marker, String message, Object param) {
        info(message, param);
    }

    @Override
    public void info(Marker marker, String message, Object param1, Object param2) {
        info(message, param1, param2);
    }

    @Override
    public void info(Marker marker, String message, Object... params) {
        info(message, params);
    }

    @Override
    public void info(Marker marker, String message, Throwable throwable) {
        info(message, throwable);
    }

    @Override
    public void info(Message message) {
        logStatus(Level.INFO, message.getFormattedMessage(), null);
    }

    @Override
    public void info(Message message, Throwable throwable) {
        logStatus(Level.INFO, message.getFormattedMessage(), throwable);
    }

    @Override
    public void info(Marker marker, Message message) {
        info(message);
    }

    @Override
    public void info(Marker marker, Message message, Throwable throwable) {
        info(message, throwable);
    }

    @Override
    public void info(Supplier<?> supplier) {
        if (isInfoEnabled()) {
            logStatus(Level.INFO, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void info(Supplier<?> supplier, Throwable throwable) {
        if (isInfoEnabled()) {
            logStatus(Level.INFO, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void info(Marker marker, Supplier<?> supplier) {
        info(supplier);
    }

    @Override
    public void info(Marker marker, Supplier<?> supplier, Throwable throwable) {
        info(supplier, throwable);
    }

    // WARN
    @Override
    public void warn(String message) {
        logStatus(Level.WARN, message, null);
    }

    @Override
    public void warn(String message, Object param) {
        logStatus(Level.WARN, formatMessage(message, param), null);
    }

    @Override
    public void warn(String message, Object param1, Object param2) {
        logStatus(Level.WARN, formatMessage(message, param1, param2), null);
    }

    @Override
    public void warn(String message, Object... params) {
        logStatus(Level.WARN, formatMessage(message, params), null);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        logStatus(Level.WARN, message, throwable);
    }

    @Override
    public void warn(Marker marker, String message) {
        warn(message);
    }

    @Override
    public void warn(Marker marker, String message, Object param) {
        warn(message, param);
    }

    @Override
    public void warn(Marker marker, String message, Object param1, Object param2) {
        warn(message, param1, param2);
    }

    @Override
    public void warn(Marker marker, String message, Object... params) {
        warn(message, params);
    }

    @Override
    public void warn(Marker marker, String message, Throwable throwable) {
        warn(message, throwable);
    }

    @Override
    public void warn(Message message) {
        logStatus(Level.WARN, message.getFormattedMessage(), null);
    }

    @Override
    public void warn(Message message, Throwable throwable) {
        logStatus(Level.WARN, message.getFormattedMessage(), throwable);
    }

    @Override
    public void warn(Marker marker, Message message) {
        warn(message);
    }

    @Override
    public void warn(Marker marker, Message message, Throwable throwable) {
        warn(message, throwable);
    }

    @Override
    public void warn(Supplier<?> supplier) {
        if (isWarnEnabled()) {
            logStatus(Level.WARN, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void warn(Supplier<?> supplier, Throwable throwable) {
        if (isWarnEnabled()) {
            logStatus(Level.WARN, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void warn(Marker marker, Supplier<?> supplier) {
        warn(supplier);
    }

    @Override
    public void warn(Marker marker, Supplier<?> supplier, Throwable throwable) {
        warn(supplier, throwable);
    }

    // ERROR
    @Override
    public void error(String message) {
        logStatus(Level.ERROR, message, null);
    }

    @Override
    public void error(String message, Object param) {
        logStatus(Level.ERROR, formatMessage(message, param), null);
    }

    @Override
    public void error(String message, Object param1, Object param2) {
        logStatus(Level.ERROR, formatMessage(message, param1, param2), null);
    }

    @Override
    public void error(String message, Object... params) {
        logStatus(Level.ERROR, formatMessage(message, params), null);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logStatus(Level.ERROR, message, throwable);
    }

    @Override
    public void error(Marker marker, String message) {
        error(message);
    }

    @Override
    public void error(Marker marker, String message, Object param) {
        error(message, param);
    }

    @Override
    public void error(Marker marker, String message, Object param1, Object param2) {
        error(message, param1, param2);
    }

    @Override
    public void error(Marker marker, String message, Object... params) {
        error(message, params);
    }

    @Override
    public void error(Marker marker, String message, Throwable throwable) {
        error(message, throwable);
    }

    @Override
    public void error(Message message) {
        logStatus(Level.ERROR, message.getFormattedMessage(), null);
    }

    @Override
    public void error(Message message, Throwable throwable) {
        logStatus(Level.ERROR, message.getFormattedMessage(), throwable);
    }

    @Override
    public void error(Marker marker, Message message) {
        error(message);
    }

    @Override
    public void error(Marker marker, Message message, Throwable throwable) {
        error(message, throwable);
    }

    @Override
    public void error(Supplier<?> supplier) {
        if (isErrorEnabled()) {
            logStatus(Level.ERROR, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void error(Supplier<?> supplier, Throwable throwable) {
        if (isErrorEnabled()) {
            logStatus(Level.ERROR, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void error(Marker marker, Supplier<?> supplier) {
        error(supplier);
    }

    @Override
    public void error(Marker marker, Supplier<?> supplier, Throwable throwable) {
        error(supplier, throwable);
    }

    // FATAL
    @Override
    public void fatal(String message) {
        logStatus(Level.FATAL, message, null);
    }

    @Override
    public void fatal(String message, Object param) {
        logStatus(Level.FATAL, formatMessage(message, param), null);
    }

    @Override
    public void fatal(String message, Object param1, Object param2) {
        logStatus(Level.FATAL, formatMessage(message, param1, param2), null);
    }

    @Override
    public void fatal(String message, Object... params) {
        logStatus(Level.FATAL, formatMessage(message, params), null);
    }

    @Override
    public void fatal(String message, Throwable throwable) {
        logStatus(Level.FATAL, message, throwable);
    }

    @Override
    public void fatal(Marker marker, String message) {
        fatal(message);
    }

    @Override
    public void fatal(Marker marker, String message, Object param) {
        fatal(message, param);
    }

    @Override
    public void fatal(Marker marker, String message, Object param1, Object param2) {
        fatal(message, param1, param2);
    }

    @Override
    public void fatal(Marker marker, String message, Object... params) {
        fatal(message, params);
    }

    @Override
    public void fatal(Marker marker, String message, Throwable throwable) {
        fatal(message, throwable);
    }

    @Override
    public void fatal(Message message) {
        logStatus(Level.FATAL, message.getFormattedMessage(), null);
    }

    @Override
    public void fatal(Message message, Throwable throwable) {
        logStatus(Level.FATAL, message.getFormattedMessage(), throwable);
    }

    @Override
    public void fatal(Marker marker, Message message) {
        fatal(message);
    }

    @Override
    public void fatal(Marker marker, Message message, Throwable throwable) {
        fatal(message, throwable);
    }

    @Override
    public void fatal(Supplier<?> supplier) {
        if (isFatalEnabled()) {
            logStatus(Level.FATAL, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void fatal(Supplier<?> supplier, Throwable throwable) {
        if (isFatalEnabled()) {
            logStatus(Level.FATAL, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void fatal(Marker marker, Supplier<?> supplier) {
        fatal(supplier);
    }

    @Override
    public void fatal(Marker marker, Supplier<?> supplier, Throwable throwable) {
        fatal(supplier, throwable);
    }

    // Generic log methods
    @Override
    public void log(Level level, String message) {
        logStatus(level, message, null);
    }

    @Override
    public void log(Level level, String message, Object... params) {
        logStatus(level, formatMessage(message, params), null);
    }

    @Override
    public void log(Level level, String message, Throwable throwable) {
        logStatus(level, message, throwable);
    }

    @Override
    public void log(Level level, Marker marker, String message) {
        log(level, message);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object... params) {
        log(level, message, params);
    }

    @Override
    public void log(Level level, Marker marker, String message, Throwable throwable) {
        log(level, message, throwable);
    }

    @Override
    public void log(Level level, Message message) {
        logStatus(level, message.getFormattedMessage(), null);
    }

    @Override
    public void log(Level level, Message message, Throwable throwable) {
        logStatus(level, message.getFormattedMessage(), throwable);
    }

    @Override
    public void log(Level level, Marker marker, Message message) {
        log(level, message);
    }

    @Override
    public void log(Level level, Marker marker, Message message, Throwable throwable) {
        log(level, message, throwable);
    }

    @Override
    public void log(Level level, Supplier<?> supplier) {
        if (isEnabled(level)) {
            logStatus(level, String.valueOf(supplier.get()), null);
        }
    }

    @Override
    public void log(Level level, Supplier<?> supplier, Throwable throwable) {
        if (isEnabled(level)) {
            logStatus(level, String.valueOf(supplier.get()), throwable);
        }
    }

    @Override
    public void log(Level level, Marker marker, Supplier<?> supplier) {
        log(level, supplier);
    }

    @Override
    public void log(Level level, Marker marker, Supplier<?> supplier, Throwable throwable) {
        log(level, supplier, throwable);
    }

    /**
     * Internal method to log status messages to stderr.
     */
    private void logStatus(Level level, String message, Throwable throwable) {
        if (level.intLevel() <= statusLevel.intLevel()) {
            StatusData data = new StatusData(System.currentTimeMillis(), level,
                    new SimpleMessage(message), throwable);

            // Keep only recent messages
            while (messages.size() >= MAX_STATUS_ENTRIES) {
                messages.poll();
            }
            messages.offer(data);

            // Print to stderr
            stream.println("StatusLogger " + level.name() + ": " + message);
            if (throwable != null) {
                throwable.printStackTrace(stream);
            }
        }
    }

    /**
     * Format message with parameters (simple {} replacement).
     */
    private String formatMessage(String message, Object... params) {
        if (params == null || params.length == 0) {
            return message;
        }
        StringBuilder sb = new StringBuilder();
        int paramIndex = 0;
        int i = 0;
        while (i < message.length()) {
            if (i < message.length() - 1 && message.charAt(i) == '{' && message.charAt(i + 1) == '}') {
                if (paramIndex < params.length) {
                    sb.append(params[paramIndex++]);
                } else {
                    sb.append("{}");
                }
                i += 2;
            } else {
                sb.append(message.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * Clear all status messages.
     */
    public void clear() {
        messages.clear();
    }

    /**
     * Simple data holder for status messages.
     */
    public static class StatusData {
        private final long timestamp;
        private final Level level;
        private final Message message;
        private final Throwable throwable;

        public StatusData(long timestamp, Level level, Message message, Throwable throwable) {
            this.timestamp = timestamp;
            this.level = level;
            this.message = message;
            this.throwable = throwable;
        }

        public long getTimestamp() { return timestamp; }
        public Level getLevel() { return level; }
        public Message getMessage() { return message; }
        public Throwable getThrowable() { return throwable; }
    }
}
