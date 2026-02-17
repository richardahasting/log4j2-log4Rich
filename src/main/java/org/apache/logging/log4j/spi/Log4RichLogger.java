package org.apache.logging.log4j.spi;

import com.log4rich.log4j2.bridge.LoggingEngine;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.*;

import java.util.function.Supplier;

/**
 * Concrete Logger implementation that delegates to log4Rich.
 * This is the main logger class returned by LogManager.
 */
public class Log4RichLogger implements Logger, ExtendedLogger {

    private final com.log4rich.core.Logger log4RichLogger;
    private final String name;
    private final MessageFactory messageFactory;

    public Log4RichLogger(String name) {
        this.name = name;
        this.log4RichLogger = com.log4rich.Log4Rich.getLogger(name);
        this.messageFactory = DefaultMessageFactory.INSTANCE;
        this.log4RichLogger.setContextProvider(com.log4rich.log4j2.bridge.ThreadContextProvider.INSTANCE);
    }

    public Log4RichLogger(String name, MessageFactory messageFactory) {
        this.name = name;
        this.log4RichLogger = com.log4rich.Log4Rich.getLogger(name);
        this.messageFactory = messageFactory != null ? messageFactory : DefaultMessageFactory.INSTANCE;
        this.log4RichLogger.setContextProvider(com.log4rich.log4j2.bridge.ThreadContextProvider.INSTANCE);
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
        return com.log4rich.log4j2.bridge.LevelTranslator.fromLog4Rich(log4RichLogger.getLevel());
    }

    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message, throwable);
    }

    // Level checking
    @Override
    public boolean isTraceEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.TRACE);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.TRACE, marker);
    }

    @Override
    public boolean isDebugEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.DEBUG);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.DEBUG, marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.INFO);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.INFO, marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.WARN);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.WARN, marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.ERROR);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.ERROR, marker);
    }

    @Override
    public boolean isFatalEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.FATAL);
    }

    @Override
    public boolean isFatalEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.FATAL, marker);
    }

    @Override
    public boolean isEnabled(Level level) {
        return LoggingEngine.isEnabled(log4RichLogger, level);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable throwable) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable throwable) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable throwable) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }

    // TRACE
    @Override
    public void trace(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.TRACE, message);
    }

    @Override
    public void trace(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.TRACE, message, param);
    }

    @Override
    public void trace(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.TRACE, message, param1, param2);
    }

    @Override
    public void trace(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null, params);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.TRACE, message, throwable);
    }

    @Override
    public void trace(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null);
    }

    @Override
    public void trace(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, param);
    }

    @Override
    public void trace(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, param1, param2);
    }

    @Override
    public void trace(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, params);
    }

    @Override
    public void trace(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, throwable);
    }

    @Override
    public void trace(Message message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null);
    }

    @Override
    public void trace(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, throwable);
    }

    @Override
    public void trace(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null);
    }

    @Override
    public void trace(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, throwable);
    }

    @Override
    public void trace(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, supplier, null);
    }

    @Override
    public void trace(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, supplier, throwable);
    }

    @Override
    public void trace(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, supplier, null);
    }

    @Override
    public void trace(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, supplier, throwable);
    }

    // DEBUG
    @Override
    public void debug(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.DEBUG, message);
    }

    @Override
    public void debug(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.DEBUG, message, param);
    }

    @Override
    public void debug(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.DEBUG, message, param1, param2);
    }

    @Override
    public void debug(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, null, params);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.DEBUG, message, throwable);
    }

    @Override
    public void debug(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null);
    }

    @Override
    public void debug(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null, param);
    }

    @Override
    public void debug(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null, param1, param2);
    }

    @Override
    public void debug(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null, params);
    }

    @Override
    public void debug(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, throwable);
    }

    @Override
    public void debug(Message message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, null);
    }

    @Override
    public void debug(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, throwable);
    }

    @Override
    public void debug(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null);
    }

    @Override
    public void debug(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, throwable);
    }

    @Override
    public void debug(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, supplier, null);
    }

    @Override
    public void debug(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, supplier, throwable);
    }

    @Override
    public void debug(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, supplier, null);
    }

    @Override
    public void debug(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, supplier, throwable);
    }

    // INFO
    @Override
    public void info(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.INFO, message);
    }

    @Override
    public void info(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.INFO, message, param);
    }

    @Override
    public void info(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.INFO, message, param1, param2);
    }

    @Override
    public void info(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, null, params);
    }

    @Override
    public void info(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.INFO, message, throwable);
    }

    @Override
    public void info(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null);
    }

    @Override
    public void info(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null, param);
    }

    @Override
    public void info(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null, param1, param2);
    }

    @Override
    public void info(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null, params);
    }

    @Override
    public void info(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, throwable);
    }

    @Override
    public void info(Message message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, null);
    }

    @Override
    public void info(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, throwable);
    }

    @Override
    public void info(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null);
    }

    @Override
    public void info(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, throwable);
    }

    @Override
    public void info(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, supplier, null);
    }

    @Override
    public void info(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, supplier, throwable);
    }

    @Override
    public void info(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, supplier, null);
    }

    @Override
    public void info(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, supplier, throwable);
    }

    // WARN
    @Override
    public void warn(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.WARN, message);
    }

    @Override
    public void warn(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.WARN, message, param);
    }

    @Override
    public void warn(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.WARN, message, param1, param2);
    }

    @Override
    public void warn(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, null, params);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.WARN, message, throwable);
    }

    @Override
    public void warn(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null);
    }

    @Override
    public void warn(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null, param);
    }

    @Override
    public void warn(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null, param1, param2);
    }

    @Override
    public void warn(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null, params);
    }

    @Override
    public void warn(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, throwable);
    }

    @Override
    public void warn(Message message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, null);
    }

    @Override
    public void warn(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, throwable);
    }

    @Override
    public void warn(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null);
    }

    @Override
    public void warn(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, throwable);
    }

    @Override
    public void warn(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, supplier, null);
    }

    @Override
    public void warn(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, supplier, throwable);
    }

    @Override
    public void warn(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, supplier, null);
    }

    @Override
    public void warn(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, supplier, throwable);
    }

    // ERROR
    @Override
    public void error(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.ERROR, message);
    }

    @Override
    public void error(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.ERROR, message, param);
    }

    @Override
    public void error(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.ERROR, message, param1, param2);
    }

    @Override
    public void error(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, null, params);
    }

    @Override
    public void error(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.ERROR, message, throwable);
    }

    @Override
    public void error(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null);
    }

    @Override
    public void error(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null, param);
    }

    @Override
    public void error(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null, param1, param2);
    }

    @Override
    public void error(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null, params);
    }

    @Override
    public void error(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, throwable);
    }

    @Override
    public void error(Message message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, null);
    }

    @Override
    public void error(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, throwable);
    }

    @Override
    public void error(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null);
    }

    @Override
    public void error(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, throwable);
    }

    @Override
    public void error(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, supplier, null);
    }

    @Override
    public void error(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, supplier, throwable);
    }

    @Override
    public void error(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, supplier, null);
    }

    @Override
    public void error(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, supplier, throwable);
    }

    // FATAL
    @Override
    public void fatal(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.FATAL, message);
    }

    @Override
    public void fatal(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.FATAL, message, param);
    }

    @Override
    public void fatal(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.FATAL, message, param1, param2);
    }

    @Override
    public void fatal(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, null, params);
    }

    @Override
    public void fatal(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.FATAL, message, throwable);
    }

    @Override
    public void fatal(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null);
    }

    @Override
    public void fatal(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null, param);
    }

    @Override
    public void fatal(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null, param1, param2);
    }

    @Override
    public void fatal(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null, params);
    }

    @Override
    public void fatal(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, throwable);
    }

    @Override
    public void fatal(Message message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, null);
    }

    @Override
    public void fatal(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, throwable);
    }

    @Override
    public void fatal(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null);
    }

    @Override
    public void fatal(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, throwable);
    }

    @Override
    public void fatal(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, supplier, null);
    }

    @Override
    public void fatal(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, supplier, throwable);
    }

    @Override
    public void fatal(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, supplier, null);
    }

    @Override
    public void fatal(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, supplier, throwable);
    }

    // Generic level
    @Override
    public void log(Level level, String message) {
        LoggingEngine.logSimple(log4RichLogger, level, message);
    }

    @Override
    public void log(Level level, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, level, null, message, null, params);
    }

    @Override
    public void log(Level level, String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, level, message, throwable);
    }

    @Override
    public void log(Level level, Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null);
    }

    @Override
    public void log(Level level, Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null, params);
    }

    @Override
    public void log(Level level, Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message, throwable);
    }

    @Override
    public void log(Level level, Message message) {
        LoggingEngine.log(log4RichLogger, level, null, message, null);
    }

    @Override
    public void log(Level level, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, null, message, throwable);
    }

    @Override
    public void log(Level level, Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null);
    }

    @Override
    public void log(Level level, Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message, throwable);
    }

    @Override
    public void log(Level level, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, level, null, supplier, null);
    }

    @Override
    public void log(Level level, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, null, supplier, throwable);
    }

    @Override
    public void log(Level level, Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, level, marker, supplier, null);
    }

    @Override
    public void log(Level level, Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, supplier, throwable);
    }

    // CharSequence/Object overloads for TRACE
    @Override
    public void trace(CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, Level.TRACE, message != null ? message.toString() : null);
    }

    @Override
    public void trace(CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.TRACE, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void trace(Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void trace(Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void trace(Object message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null);
    }

    @Override
    public void trace(Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, throwable);
    }

    @Override
    public void trace(Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null);
    }

    @Override
    public void trace(Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, throwable);
    }

    // CharSequence/Object overloads for DEBUG
    @Override
    public void debug(CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, Level.DEBUG, message != null ? message.toString() : null);
    }

    @Override
    public void debug(CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.DEBUG, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void debug(Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void debug(Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void debug(Object message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, null);
    }

    @Override
    public void debug(Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, throwable);
    }

    @Override
    public void debug(Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null);
    }

    @Override
    public void debug(Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, throwable);
    }

    // CharSequence/Object overloads for INFO
    @Override
    public void info(CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, Level.INFO, message != null ? message.toString() : null);
    }

    @Override
    public void info(CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.INFO, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void info(Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void info(Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void info(Object message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, null);
    }

    @Override
    public void info(Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, throwable);
    }

    @Override
    public void info(Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null);
    }

    @Override
    public void info(Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, throwable);
    }

    // CharSequence/Object overloads for WARN
    @Override
    public void warn(CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, Level.WARN, message != null ? message.toString() : null);
    }

    @Override
    public void warn(CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.WARN, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void warn(Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void warn(Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void warn(Object message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, null);
    }

    @Override
    public void warn(Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, throwable);
    }

    @Override
    public void warn(Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null);
    }

    @Override
    public void warn(Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, throwable);
    }

    // CharSequence/Object overloads for ERROR
    @Override
    public void error(CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, Level.ERROR, message != null ? message.toString() : null);
    }

    @Override
    public void error(CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.ERROR, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void error(Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void error(Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void error(Object message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, null);
    }

    @Override
    public void error(Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, throwable);
    }

    @Override
    public void error(Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null);
    }

    @Override
    public void error(Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, throwable);
    }

    // CharSequence/Object overloads for FATAL
    @Override
    public void fatal(CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, Level.FATAL, message != null ? message.toString() : null);
    }

    @Override
    public void fatal(CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.FATAL, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void fatal(Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void fatal(Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void fatal(Object message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, null);
    }

    @Override
    public void fatal(Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, throwable);
    }

    @Override
    public void fatal(Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null);
    }

    @Override
    public void fatal(Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, throwable);
    }

    // Generic level CharSequence/Object overloads
    @Override
    public void log(Level level, CharSequence message) {
        LoggingEngine.logSimple(log4RichLogger, level, message != null ? message.toString() : null);
    }

    @Override
    public void log(Level level, CharSequence message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, level, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void log(Level level, Marker marker, CharSequence message) {
        LoggingEngine.log(log4RichLogger, level, marker, message != null ? message.toString() : null, null);
    }

    @Override
    public void log(Level level, Marker marker, CharSequence message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message != null ? message.toString() : null, throwable);
    }

    @Override
    public void log(Level level, Object message) {
        LoggingEngine.log(log4RichLogger, level, null, message, null);
    }

    @Override
    public void log(Level level, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, null, message, throwable);
    }

    @Override
    public void log(Level level, Marker marker, Object message) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null);
    }

    @Override
    public void log(Level level, Marker marker, Object message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message, throwable);
    }

    // ========== logIfEnabled implementations ==========

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, params), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9), null);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable throwable) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message), throwable);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, message, throwable);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message.toString()), throwable);
        }
    }

    @Override
    public void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable throwable) {
        if (isEnabled(level, marker)) {
            logMessage(fqcn, level, marker, messageFactory.newMessage(message), throwable);
        }
    }
}
