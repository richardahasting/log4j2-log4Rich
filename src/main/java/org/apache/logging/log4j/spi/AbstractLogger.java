package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.*;

import java.util.function.Supplier;

/**
 * Abstract base class for Logger implementations.
 * Routes all logging methods through {@link #logMessage(String, Level, Marker, Message, Throwable)}.
 *
 * <p>In the real log4j2, this is a ~2000-line class. This implementation provides the
 * essential routing that SLF4J binding and other wrappers depend on.</p>
 *
 * @since 1.0.5
 */
public abstract class AbstractLogger implements ExtendedLogger {

    private static final String FQCN = AbstractLogger.class.getName();

    protected final String name;
    protected final MessageFactory messageFactory;

    protected AbstractLogger(String name) {
        this.name = name;
        this.messageFactory = DefaultMessageFactory.INSTANCE;
    }

    protected AbstractLogger(String name, MessageFactory messageFactory) {
        this.name = name;
        this.messageFactory = messageFactory != null ? messageFactory : DefaultMessageFactory.INSTANCE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    // ========== Abstract methods subclasses must implement ==========

    @Override
    public abstract Level getLevel();

    @Override
    public abstract void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable throwable);

    @Override
    public abstract boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable);

    // ========== isEnabled convenience delegates ==========

    @Override
    public boolean isTraceEnabled() { return isEnabled(Level.TRACE, null, (Message) null, null); }
    @Override
    public boolean isTraceEnabled(Marker marker) { return isEnabled(Level.TRACE, marker, (Message) null, null); }
    @Override
    public boolean isDebugEnabled() { return isEnabled(Level.DEBUG, null, (Message) null, null); }
    @Override
    public boolean isDebugEnabled(Marker marker) { return isEnabled(Level.DEBUG, marker, (Message) null, null); }
    @Override
    public boolean isInfoEnabled() { return isEnabled(Level.INFO, null, (Message) null, null); }
    @Override
    public boolean isInfoEnabled(Marker marker) { return isEnabled(Level.INFO, marker, (Message) null, null); }
    @Override
    public boolean isWarnEnabled() { return isEnabled(Level.WARN, null, (Message) null, null); }
    @Override
    public boolean isWarnEnabled(Marker marker) { return isEnabled(Level.WARN, marker, (Message) null, null); }
    @Override
    public boolean isErrorEnabled() { return isEnabled(Level.ERROR, null, (Message) null, null); }
    @Override
    public boolean isErrorEnabled(Marker marker) { return isEnabled(Level.ERROR, marker, (Message) null, null); }
    @Override
    public boolean isFatalEnabled() { return isEnabled(Level.FATAL, null, (Message) null, null); }
    @Override
    public boolean isFatalEnabled(Marker marker) { return isEnabled(Level.FATAL, marker, (Message) null, null); }
    @Override
    public boolean isEnabled(Level level) { return isEnabled(level, null, (Message) null, null); }
    @Override
    public boolean isEnabled(Level level, Marker marker) { return isEnabled(level, marker, (Message) null, null); }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return isEnabled(level, marker, (Message) null, null);
    }
    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return isEnabled(level, marker, (Message) null, null);
    }
    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable throwable) {
        return isEnabled(level, marker, (Message) null, null);
    }
    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable throwable) {
        return isEnabled(level, marker, (Message) null, null);
    }
    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable throwable) {
        return isEnabled(level, marker, (Message) null, null);
    }

    // ========== Logging methods - delegate through logMessage ==========

    private void logIfEnabled(Level level, Marker marker, String message, Object... params) {
        if (isEnabled(level, marker)) {
            logMessage(FQCN, level, marker, messageFactory.newMessage(message, params), null);
        }
    }

    // --- TRACE ---
    @Override public void trace(String msg) { logIfEnabled(Level.TRACE, null, msg); }
    @Override public void trace(String msg, Object p) { logIfEnabled(Level.TRACE, null, msg, p); }
    @Override public void trace(String msg, Object p1, Object p2) { logIfEnabled(Level.TRACE, null, msg, p1, p2); }
    @Override public void trace(String msg, Object... ps) { logIfEnabled(Level.TRACE, null, msg, ps); }
    @Override public void trace(String msg, Throwable t) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, messageFactory.newMessage(msg), t); }
    @Override public void trace(Marker m, String msg) { logIfEnabled(Level.TRACE, m, msg); }
    @Override public void trace(Marker m, String msg, Object p) { logIfEnabled(Level.TRACE, m, msg, p); }
    @Override public void trace(Marker m, String msg, Object p1, Object p2) { logIfEnabled(Level.TRACE, m, msg, p1, p2); }
    @Override public void trace(Marker m, String msg, Object... ps) { logIfEnabled(Level.TRACE, m, msg, ps); }
    @Override public void trace(Marker m, String msg, Throwable t) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, messageFactory.newMessage(msg), t); }
    @Override public void trace(Message msg) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, msg, null); }
    @Override public void trace(Message msg, Throwable t) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, msg, t); }
    @Override public void trace(Marker m, Message msg) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, msg, null); }
    @Override public void trace(Marker m, Message msg, Throwable t) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, msg, t); }
    @Override public void trace(Supplier<?> s) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, messageFactory.newMessage(s.get()), null); }
    @Override public void trace(Supplier<?> s, Throwable t) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, messageFactory.newMessage(s.get()), t); }
    @Override public void trace(Marker m, Supplier<?> s) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, messageFactory.newMessage(s.get()), null); }
    @Override public void trace(Marker m, Supplier<?> s, Throwable t) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, messageFactory.newMessage(s.get()), t); }
    @Override public void trace(CharSequence msg) { trace(msg != null ? msg.toString() : null); }
    @Override public void trace(CharSequence msg, Throwable t) { trace(msg != null ? msg.toString() : null, t); }
    @Override public void trace(Marker m, CharSequence msg) { trace(m, msg != null ? msg.toString() : null); }
    @Override public void trace(Marker m, CharSequence msg, Throwable t) { trace(m, msg != null ? msg.toString() : null, t); }
    @Override public void trace(Object msg) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, messageFactory.newMessage(msg), null); }
    @Override public void trace(Object msg, Throwable t) { if (isTraceEnabled()) logMessage(FQCN, Level.TRACE, null, messageFactory.newMessage(msg), t); }
    @Override public void trace(Marker m, Object msg) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, messageFactory.newMessage(msg), null); }
    @Override public void trace(Marker m, Object msg, Throwable t) { if (isEnabled(Level.TRACE, m)) logMessage(FQCN, Level.TRACE, m, messageFactory.newMessage(msg), t); }

    // --- DEBUG ---
    @Override public void debug(String msg) { logIfEnabled(Level.DEBUG, null, msg); }
    @Override public void debug(String msg, Object p) { logIfEnabled(Level.DEBUG, null, msg, p); }
    @Override public void debug(String msg, Object p1, Object p2) { logIfEnabled(Level.DEBUG, null, msg, p1, p2); }
    @Override public void debug(String msg, Object... ps) { logIfEnabled(Level.DEBUG, null, msg, ps); }
    @Override public void debug(String msg, Throwable t) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, messageFactory.newMessage(msg), t); }
    @Override public void debug(Marker m, String msg) { logIfEnabled(Level.DEBUG, m, msg); }
    @Override public void debug(Marker m, String msg, Object p) { logIfEnabled(Level.DEBUG, m, msg, p); }
    @Override public void debug(Marker m, String msg, Object p1, Object p2) { logIfEnabled(Level.DEBUG, m, msg, p1, p2); }
    @Override public void debug(Marker m, String msg, Object... ps) { logIfEnabled(Level.DEBUG, m, msg, ps); }
    @Override public void debug(Marker m, String msg, Throwable t) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, messageFactory.newMessage(msg), t); }
    @Override public void debug(Message msg) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, msg, null); }
    @Override public void debug(Message msg, Throwable t) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, msg, t); }
    @Override public void debug(Marker m, Message msg) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, msg, null); }
    @Override public void debug(Marker m, Message msg, Throwable t) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, msg, t); }
    @Override public void debug(Supplier<?> s) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, messageFactory.newMessage(s.get()), null); }
    @Override public void debug(Supplier<?> s, Throwable t) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, messageFactory.newMessage(s.get()), t); }
    @Override public void debug(Marker m, Supplier<?> s) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, messageFactory.newMessage(s.get()), null); }
    @Override public void debug(Marker m, Supplier<?> s, Throwable t) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, messageFactory.newMessage(s.get()), t); }
    @Override public void debug(CharSequence msg) { debug(msg != null ? msg.toString() : null); }
    @Override public void debug(CharSequence msg, Throwable t) { debug(msg != null ? msg.toString() : null, t); }
    @Override public void debug(Marker m, CharSequence msg) { debug(m, msg != null ? msg.toString() : null); }
    @Override public void debug(Marker m, CharSequence msg, Throwable t) { debug(m, msg != null ? msg.toString() : null, t); }
    @Override public void debug(Object msg) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, messageFactory.newMessage(msg), null); }
    @Override public void debug(Object msg, Throwable t) { if (isDebugEnabled()) logMessage(FQCN, Level.DEBUG, null, messageFactory.newMessage(msg), t); }
    @Override public void debug(Marker m, Object msg) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, messageFactory.newMessage(msg), null); }
    @Override public void debug(Marker m, Object msg, Throwable t) { if (isEnabled(Level.DEBUG, m)) logMessage(FQCN, Level.DEBUG, m, messageFactory.newMessage(msg), t); }

    // --- INFO ---
    @Override public void info(String msg) { logIfEnabled(Level.INFO, null, msg); }
    @Override public void info(String msg, Object p) { logIfEnabled(Level.INFO, null, msg, p); }
    @Override public void info(String msg, Object p1, Object p2) { logIfEnabled(Level.INFO, null, msg, p1, p2); }
    @Override public void info(String msg, Object... ps) { logIfEnabled(Level.INFO, null, msg, ps); }
    @Override public void info(String msg, Throwable t) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, messageFactory.newMessage(msg), t); }
    @Override public void info(Marker m, String msg) { logIfEnabled(Level.INFO, m, msg); }
    @Override public void info(Marker m, String msg, Object p) { logIfEnabled(Level.INFO, m, msg, p); }
    @Override public void info(Marker m, String msg, Object p1, Object p2) { logIfEnabled(Level.INFO, m, msg, p1, p2); }
    @Override public void info(Marker m, String msg, Object... ps) { logIfEnabled(Level.INFO, m, msg, ps); }
    @Override public void info(Marker m, String msg, Throwable t) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, messageFactory.newMessage(msg), t); }
    @Override public void info(Message msg) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, msg, null); }
    @Override public void info(Message msg, Throwable t) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, msg, t); }
    @Override public void info(Marker m, Message msg) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, msg, null); }
    @Override public void info(Marker m, Message msg, Throwable t) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, msg, t); }
    @Override public void info(Supplier<?> s) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, messageFactory.newMessage(s.get()), null); }
    @Override public void info(Supplier<?> s, Throwable t) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, messageFactory.newMessage(s.get()), t); }
    @Override public void info(Marker m, Supplier<?> s) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, messageFactory.newMessage(s.get()), null); }
    @Override public void info(Marker m, Supplier<?> s, Throwable t) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, messageFactory.newMessage(s.get()), t); }
    @Override public void info(CharSequence msg) { info(msg != null ? msg.toString() : null); }
    @Override public void info(CharSequence msg, Throwable t) { info(msg != null ? msg.toString() : null, t); }
    @Override public void info(Marker m, CharSequence msg) { info(m, msg != null ? msg.toString() : null); }
    @Override public void info(Marker m, CharSequence msg, Throwable t) { info(m, msg != null ? msg.toString() : null, t); }
    @Override public void info(Object msg) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, messageFactory.newMessage(msg), null); }
    @Override public void info(Object msg, Throwable t) { if (isInfoEnabled()) logMessage(FQCN, Level.INFO, null, messageFactory.newMessage(msg), t); }
    @Override public void info(Marker m, Object msg) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, messageFactory.newMessage(msg), null); }
    @Override public void info(Marker m, Object msg, Throwable t) { if (isEnabled(Level.INFO, m)) logMessage(FQCN, Level.INFO, m, messageFactory.newMessage(msg), t); }

    // --- WARN ---
    @Override public void warn(String msg) { logIfEnabled(Level.WARN, null, msg); }
    @Override public void warn(String msg, Object p) { logIfEnabled(Level.WARN, null, msg, p); }
    @Override public void warn(String msg, Object p1, Object p2) { logIfEnabled(Level.WARN, null, msg, p1, p2); }
    @Override public void warn(String msg, Object... ps) { logIfEnabled(Level.WARN, null, msg, ps); }
    @Override public void warn(String msg, Throwable t) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, messageFactory.newMessage(msg), t); }
    @Override public void warn(Marker m, String msg) { logIfEnabled(Level.WARN, m, msg); }
    @Override public void warn(Marker m, String msg, Object p) { logIfEnabled(Level.WARN, m, msg, p); }
    @Override public void warn(Marker m, String msg, Object p1, Object p2) { logIfEnabled(Level.WARN, m, msg, p1, p2); }
    @Override public void warn(Marker m, String msg, Object... ps) { logIfEnabled(Level.WARN, m, msg, ps); }
    @Override public void warn(Marker m, String msg, Throwable t) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, messageFactory.newMessage(msg), t); }
    @Override public void warn(Message msg) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, msg, null); }
    @Override public void warn(Message msg, Throwable t) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, msg, t); }
    @Override public void warn(Marker m, Message msg) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, msg, null); }
    @Override public void warn(Marker m, Message msg, Throwable t) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, msg, t); }
    @Override public void warn(Supplier<?> s) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, messageFactory.newMessage(s.get()), null); }
    @Override public void warn(Supplier<?> s, Throwable t) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, messageFactory.newMessage(s.get()), t); }
    @Override public void warn(Marker m, Supplier<?> s) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, messageFactory.newMessage(s.get()), null); }
    @Override public void warn(Marker m, Supplier<?> s, Throwable t) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, messageFactory.newMessage(s.get()), t); }
    @Override public void warn(CharSequence msg) { warn(msg != null ? msg.toString() : null); }
    @Override public void warn(CharSequence msg, Throwable t) { warn(msg != null ? msg.toString() : null, t); }
    @Override public void warn(Marker m, CharSequence msg) { warn(m, msg != null ? msg.toString() : null); }
    @Override public void warn(Marker m, CharSequence msg, Throwable t) { warn(m, msg != null ? msg.toString() : null, t); }
    @Override public void warn(Object msg) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, messageFactory.newMessage(msg), null); }
    @Override public void warn(Object msg, Throwable t) { if (isWarnEnabled()) logMessage(FQCN, Level.WARN, null, messageFactory.newMessage(msg), t); }
    @Override public void warn(Marker m, Object msg) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, messageFactory.newMessage(msg), null); }
    @Override public void warn(Marker m, Object msg, Throwable t) { if (isEnabled(Level.WARN, m)) logMessage(FQCN, Level.WARN, m, messageFactory.newMessage(msg), t); }

    // --- ERROR ---
    @Override public void error(String msg) { logIfEnabled(Level.ERROR, null, msg); }
    @Override public void error(String msg, Object p) { logIfEnabled(Level.ERROR, null, msg, p); }
    @Override public void error(String msg, Object p1, Object p2) { logIfEnabled(Level.ERROR, null, msg, p1, p2); }
    @Override public void error(String msg, Object... ps) { logIfEnabled(Level.ERROR, null, msg, ps); }
    @Override public void error(String msg, Throwable t) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, messageFactory.newMessage(msg), t); }
    @Override public void error(Marker m, String msg) { logIfEnabled(Level.ERROR, m, msg); }
    @Override public void error(Marker m, String msg, Object p) { logIfEnabled(Level.ERROR, m, msg, p); }
    @Override public void error(Marker m, String msg, Object p1, Object p2) { logIfEnabled(Level.ERROR, m, msg, p1, p2); }
    @Override public void error(Marker m, String msg, Object... ps) { logIfEnabled(Level.ERROR, m, msg, ps); }
    @Override public void error(Marker m, String msg, Throwable t) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, messageFactory.newMessage(msg), t); }
    @Override public void error(Message msg) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, msg, null); }
    @Override public void error(Message msg, Throwable t) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, msg, t); }
    @Override public void error(Marker m, Message msg) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, msg, null); }
    @Override public void error(Marker m, Message msg, Throwable t) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, msg, t); }
    @Override public void error(Supplier<?> s) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, messageFactory.newMessage(s.get()), null); }
    @Override public void error(Supplier<?> s, Throwable t) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, messageFactory.newMessage(s.get()), t); }
    @Override public void error(Marker m, Supplier<?> s) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, messageFactory.newMessage(s.get()), null); }
    @Override public void error(Marker m, Supplier<?> s, Throwable t) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, messageFactory.newMessage(s.get()), t); }
    @Override public void error(CharSequence msg) { error(msg != null ? msg.toString() : null); }
    @Override public void error(CharSequence msg, Throwable t) { error(msg != null ? msg.toString() : null, t); }
    @Override public void error(Marker m, CharSequence msg) { error(m, msg != null ? msg.toString() : null); }
    @Override public void error(Marker m, CharSequence msg, Throwable t) { error(m, msg != null ? msg.toString() : null, t); }
    @Override public void error(Object msg) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, messageFactory.newMessage(msg), null); }
    @Override public void error(Object msg, Throwable t) { if (isErrorEnabled()) logMessage(FQCN, Level.ERROR, null, messageFactory.newMessage(msg), t); }
    @Override public void error(Marker m, Object msg) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, messageFactory.newMessage(msg), null); }
    @Override public void error(Marker m, Object msg, Throwable t) { if (isEnabled(Level.ERROR, m)) logMessage(FQCN, Level.ERROR, m, messageFactory.newMessage(msg), t); }

    // --- FATAL ---
    @Override public void fatal(String msg) { logIfEnabled(Level.FATAL, null, msg); }
    @Override public void fatal(String msg, Object p) { logIfEnabled(Level.FATAL, null, msg, p); }
    @Override public void fatal(String msg, Object p1, Object p2) { logIfEnabled(Level.FATAL, null, msg, p1, p2); }
    @Override public void fatal(String msg, Object... ps) { logIfEnabled(Level.FATAL, null, msg, ps); }
    @Override public void fatal(String msg, Throwable t) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, messageFactory.newMessage(msg), t); }
    @Override public void fatal(Marker m, String msg) { logIfEnabled(Level.FATAL, m, msg); }
    @Override public void fatal(Marker m, String msg, Object p) { logIfEnabled(Level.FATAL, m, msg, p); }
    @Override public void fatal(Marker m, String msg, Object p1, Object p2) { logIfEnabled(Level.FATAL, m, msg, p1, p2); }
    @Override public void fatal(Marker m, String msg, Object... ps) { logIfEnabled(Level.FATAL, m, msg, ps); }
    @Override public void fatal(Marker m, String msg, Throwable t) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, messageFactory.newMessage(msg), t); }
    @Override public void fatal(Message msg) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, msg, null); }
    @Override public void fatal(Message msg, Throwable t) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, msg, t); }
    @Override public void fatal(Marker m, Message msg) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, msg, null); }
    @Override public void fatal(Marker m, Message msg, Throwable t) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, msg, t); }
    @Override public void fatal(Supplier<?> s) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, messageFactory.newMessage(s.get()), null); }
    @Override public void fatal(Supplier<?> s, Throwable t) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, messageFactory.newMessage(s.get()), t); }
    @Override public void fatal(Marker m, Supplier<?> s) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, messageFactory.newMessage(s.get()), null); }
    @Override public void fatal(Marker m, Supplier<?> s, Throwable t) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, messageFactory.newMessage(s.get()), t); }
    @Override public void fatal(CharSequence msg) { fatal(msg != null ? msg.toString() : null); }
    @Override public void fatal(CharSequence msg, Throwable t) { fatal(msg != null ? msg.toString() : null, t); }
    @Override public void fatal(Marker m, CharSequence msg) { fatal(m, msg != null ? msg.toString() : null); }
    @Override public void fatal(Marker m, CharSequence msg, Throwable t) { fatal(m, msg != null ? msg.toString() : null, t); }
    @Override public void fatal(Object msg) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, messageFactory.newMessage(msg), null); }
    @Override public void fatal(Object msg, Throwable t) { if (isFatalEnabled()) logMessage(FQCN, Level.FATAL, null, messageFactory.newMessage(msg), t); }
    @Override public void fatal(Marker m, Object msg) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, messageFactory.newMessage(msg), null); }
    @Override public void fatal(Marker m, Object msg, Throwable t) { if (isEnabled(Level.FATAL, m)) logMessage(FQCN, Level.FATAL, m, messageFactory.newMessage(msg), t); }

    // --- Generic log() ---
    @Override public void log(Level l, String msg) { logIfEnabled(l, null, msg); }
    @Override public void log(Level l, String msg, Object... ps) { logIfEnabled(l, null, msg, ps); }
    @Override public void log(Level l, String msg, Throwable t) { if (isEnabled(l)) logMessage(FQCN, l, null, messageFactory.newMessage(msg), t); }
    @Override public void log(Level l, Marker m, String msg) { logIfEnabled(l, m, msg); }
    @Override public void log(Level l, Marker m, String msg, Object... ps) { logIfEnabled(l, m, msg, ps); }
    @Override public void log(Level l, Marker m, String msg, Throwable t) { if (isEnabled(l, m)) logMessage(FQCN, l, m, messageFactory.newMessage(msg), t); }
    @Override public void log(Level l, Message msg) { if (isEnabled(l)) logMessage(FQCN, l, null, msg, null); }
    @Override public void log(Level l, Message msg, Throwable t) { if (isEnabled(l)) logMessage(FQCN, l, null, msg, t); }
    @Override public void log(Level l, Marker m, Message msg) { if (isEnabled(l, m)) logMessage(FQCN, l, m, msg, null); }
    @Override public void log(Level l, Marker m, Message msg, Throwable t) { if (isEnabled(l, m)) logMessage(FQCN, l, m, msg, t); }
    @Override public void log(Level l, Supplier<?> s) { if (isEnabled(l)) logMessage(FQCN, l, null, messageFactory.newMessage(s.get()), null); }
    @Override public void log(Level l, Supplier<?> s, Throwable t) { if (isEnabled(l)) logMessage(FQCN, l, null, messageFactory.newMessage(s.get()), t); }
    @Override public void log(Level l, Marker m, Supplier<?> s) { if (isEnabled(l, m)) logMessage(FQCN, l, m, messageFactory.newMessage(s.get()), null); }
    @Override public void log(Level l, Marker m, Supplier<?> s, Throwable t) { if (isEnabled(l, m)) logMessage(FQCN, l, m, messageFactory.newMessage(s.get()), t); }
    @Override public void log(Level l, CharSequence msg) { log(l, msg != null ? msg.toString() : null); }
    @Override public void log(Level l, CharSequence msg, Throwable t) { log(l, msg != null ? msg.toString() : null, t); }
    @Override public void log(Level l, Marker m, CharSequence msg) { log(l, m, msg != null ? msg.toString() : null); }
    @Override public void log(Level l, Marker m, CharSequence msg, Throwable t) { log(l, m, msg != null ? msg.toString() : null, t); }
    @Override public void log(Level l, Object msg) { if (isEnabled(l)) logMessage(FQCN, l, null, messageFactory.newMessage(msg), null); }
    @Override public void log(Level l, Object msg, Throwable t) { if (isEnabled(l)) logMessage(FQCN, l, null, messageFactory.newMessage(msg), t); }
    @Override public void log(Level l, Marker m, Object msg) { if (isEnabled(l, m)) logMessage(FQCN, l, m, messageFactory.newMessage(msg), null); }
    @Override public void log(Level l, Marker m, Object msg, Throwable t) { if (isEnabled(l, m)) logMessage(FQCN, l, m, messageFactory.newMessage(msg), t); }

    // ========== logIfEnabled (ExtendedLogger) ==========

    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, params), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9), null); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable throwable) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message), throwable); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, Message message, Throwable throwable) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, message, throwable); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message != null ? message.toString() : null), throwable); }
    @Override public void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable throwable) { if (isEnabled(level, marker)) logMessage(fqcn, level, marker, messageFactory.newMessage(message), throwable); }
}
