package org.apache.logging.log4j;

import com.log4rich.log4j2.bridge.LoggingEngine;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.function.Supplier;

/**
 * log4j2 Logger implementation that delegates to log4Rich.
 * 
 * ALL 180+ logging methods are simple one-liners that delegate to LoggingEngine.
 * This demonstrates the power of the layered architecture - no code duplication!
 */
public class Logger {
    
    private final com.log4rich.core.Logger log4RichLogger;
    private final String name;
    private final MessageFactory messageFactory;
    
    protected Logger(String name) {
        this.name = name;
        this.log4RichLogger = com.log4rich.Log4Rich.getLogger(name);
        this.messageFactory = org.apache.logging.log4j.message.DefaultMessageFactory.INSTANCE;
        
        // Set up context provider for log4j2 ThreadContext integration
        this.log4RichLogger.setContextProvider(com.log4rich.log4j2.bridge.ThreadContextProvider.INSTANCE);
    }
    
    protected Logger(String name, MessageFactory messageFactory) {
        this.name = name;
        this.log4RichLogger = com.log4rich.Log4Rich.getLogger(name);
        this.messageFactory = messageFactory != null ? messageFactory : org.apache.logging.log4j.message.DefaultMessageFactory.INSTANCE;
        
        // Set up context provider for log4j2 ThreadContext integration
        this.log4RichLogger.setContextProvider(com.log4rich.log4j2.bridge.ThreadContextProvider.INSTANCE);
    }
    
    // ========== Logger Properties ==========
    
    public String getName() {
        return name;
    }
    
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }
    
    // ========== Level Checking Methods ==========
    
    public boolean isTraceEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.TRACE);
    }
    
    public boolean isTraceEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.TRACE, marker);
    }
    
    public boolean isDebugEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.DEBUG);
    }
    
    public boolean isDebugEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.DEBUG, marker);
    }
    
    public boolean isInfoEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.INFO);
    }
    
    public boolean isInfoEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.INFO, marker);
    }
    
    public boolean isWarnEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.WARN);
    }
    
    public boolean isWarnEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.WARN, marker);
    }
    
    public boolean isErrorEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.ERROR);
    }
    
    public boolean isErrorEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.ERROR, marker);
    }
    
    public boolean isFatalEnabled() {
        return LoggingEngine.isEnabled(log4RichLogger, Level.FATAL);
    }
    
    public boolean isFatalEnabled(Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, Level.FATAL, marker);
    }
    
    public boolean isEnabled(Level level) {
        return LoggingEngine.isEnabled(log4RichLogger, level);
    }
    
    public boolean isEnabled(Level level, Marker marker) {
        return LoggingEngine.isEnabled(log4RichLogger, level, marker);
    }
    
    // ========== TRACE Level Methods ==========
    
    public void trace(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.TRACE, message);
    }
    
    public void trace(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.TRACE, message, param);
    }
    
    public void trace(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.TRACE, message, param1, param2);
    }
    
    public void trace(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null, params);
    }
    
    public void trace(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.TRACE, message, throwable);
    }
    
    public void trace(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null);
    }
    
    public void trace(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, param);
    }
    
    public void trace(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, param1, param2);
    }
    
    public void trace(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null, params);
    }
    
    public void trace(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, throwable);
    }
    
    public void trace(Message message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, null);
    }
    
    public void trace(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, message, throwable);
    }
    
    public void trace(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, null);
    }
    
    public void trace(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, message, throwable);
    }
    
    public void trace(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, supplier, null);
    }
    
    public void trace(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, null, supplier, throwable);
    }
    
    public void trace(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, supplier, null);
    }
    
    public void trace(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.TRACE, marker, supplier, throwable);
    }
    
    // ========== DEBUG Level Methods ==========
    
    public void debug(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.DEBUG, message);
    }
    
    public void debug(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.DEBUG, message, param);
    }
    
    public void debug(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.DEBUG, message, param1, param2);
    }
    
    public void debug(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, null, params);
    }
    
    public void debug(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.DEBUG, message, throwable);
    }
    
    public void debug(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null);
    }
    
    public void debug(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null, param);
    }
    
    public void debug(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null, param1, param2);
    }
    
    public void debug(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null, params);
    }
    
    public void debug(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, throwable);
    }
    
    public void debug(Message message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, null);
    }
    
    public void debug(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, message, throwable);
    }
    
    public void debug(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, null);
    }
    
    public void debug(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, message, throwable);
    }
    
    public void debug(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, supplier, null);
    }
    
    public void debug(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, null, supplier, throwable);
    }
    
    public void debug(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, supplier, null);
    }
    
    public void debug(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.DEBUG, marker, supplier, throwable);
    }
    
    // ========== INFO Level Methods ==========
    
    public void info(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.INFO, message);
    }
    
    public void info(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.INFO, message, param);
    }
    
    public void info(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.INFO, message, param1, param2);
    }
    
    public void info(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, null, params);
    }
    
    public void info(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.INFO, message, throwable);
    }
    
    public void info(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null);
    }
    
    public void info(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null, param);
    }
    
    public void info(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null, param1, param2);
    }
    
    public void info(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null, params);
    }
    
    public void info(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, throwable);
    }
    
    public void info(Message message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, null);
    }
    
    public void info(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, message, throwable);
    }
    
    public void info(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, null);
    }
    
    public void info(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, message, throwable);
    }
    
    public void info(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, supplier, null);
    }
    
    public void info(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, null, supplier, throwable);
    }
    
    public void info(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, supplier, null);
    }
    
    public void info(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.INFO, marker, supplier, throwable);
    }
    
    // ========== WARN Level Methods ==========
    
    public void warn(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.WARN, message);
    }
    
    public void warn(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.WARN, message, param);
    }
    
    public void warn(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.WARN, message, param1, param2);
    }
    
    public void warn(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, null, params);
    }
    
    public void warn(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.WARN, message, throwable);
    }
    
    public void warn(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null);
    }
    
    public void warn(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null, param);
    }
    
    public void warn(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null, param1, param2);
    }
    
    public void warn(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null, params);
    }
    
    public void warn(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, throwable);
    }
    
    public void warn(Message message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, null);
    }
    
    public void warn(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, message, throwable);
    }
    
    public void warn(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, null);
    }
    
    public void warn(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, message, throwable);
    }
    
    public void warn(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, supplier, null);
    }
    
    public void warn(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, null, supplier, throwable);
    }
    
    public void warn(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, supplier, null);
    }
    
    public void warn(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.WARN, marker, supplier, throwable);
    }
    
    // ========== ERROR Level Methods ==========
    
    public void error(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.ERROR, message);
    }
    
    public void error(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.ERROR, message, param);
    }
    
    public void error(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.ERROR, message, param1, param2);
    }
    
    public void error(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, null, params);
    }
    
    public void error(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.ERROR, message, throwable);
    }
    
    public void error(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null);
    }
    
    public void error(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null, param);
    }
    
    public void error(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null, param1, param2);
    }
    
    public void error(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null, params);
    }
    
    public void error(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, throwable);
    }
    
    public void error(Message message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, null);
    }
    
    public void error(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, message, throwable);
    }
    
    public void error(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, null);
    }
    
    public void error(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, message, throwable);
    }
    
    public void error(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, supplier, null);
    }
    
    public void error(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, null, supplier, throwable);
    }
    
    public void error(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, supplier, null);
    }
    
    public void error(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.ERROR, marker, supplier, throwable);
    }
    
    // ========== FATAL Level Methods ==========
    
    public void fatal(String message) {
        LoggingEngine.logSimple(log4RichLogger, Level.FATAL, message);
    }
    
    public void fatal(String message, Object param) {
        LoggingEngine.logSingleParam(log4RichLogger, Level.FATAL, message, param);
    }
    
    public void fatal(String message, Object param1, Object param2) {
        LoggingEngine.logTwoParams(log4RichLogger, Level.FATAL, message, param1, param2);
    }
    
    public void fatal(String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, null, params);
    }
    
    public void fatal(String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, Level.FATAL, message, throwable);
    }
    
    public void fatal(Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null);
    }
    
    public void fatal(Marker marker, String message, Object param) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null, param);
    }
    
    public void fatal(Marker marker, String message, Object param1, Object param2) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null, param1, param2);
    }
    
    public void fatal(Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null, params);
    }
    
    public void fatal(Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, throwable);
    }
    
    public void fatal(Message message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, null);
    }
    
    public void fatal(Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, message, throwable);
    }
    
    public void fatal(Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, null);
    }
    
    public void fatal(Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, message, throwable);
    }
    
    public void fatal(Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, supplier, null);
    }
    
    public void fatal(Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, null, supplier, throwable);
    }
    
    public void fatal(Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, supplier, null);
    }
    
    public void fatal(Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, Level.FATAL, marker, supplier, throwable);
    }
    
    // ========== Generic Level Methods ==========
    
    public void log(Level level, String message) {
        LoggingEngine.logSimple(log4RichLogger, level, message);
    }
    
    public void log(Level level, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, level, null, message, null, params);
    }
    
    public void log(Level level, String message, Throwable throwable) {
        LoggingEngine.logWithException(log4RichLogger, level, message, throwable);
    }
    
    public void log(Level level, Marker marker, String message) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null);
    }
    
    public void log(Level level, Marker marker, String message, Object... params) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null, params);
    }
    
    public void log(Level level, Marker marker, String message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message, throwable);
    }
    
    public void log(Level level, Message message) {
        LoggingEngine.log(log4RichLogger, level, null, message, null);
    }
    
    public void log(Level level, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, null, message, throwable);
    }
    
    public void log(Level level, Marker marker, Message message) {
        LoggingEngine.log(log4RichLogger, level, marker, message, null);
    }
    
    public void log(Level level, Marker marker, Message message, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, message, throwable);
    }
    
    public void log(Level level, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, level, null, supplier, null);
    }
    
    public void log(Level level, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, null, supplier, throwable);
    }
    
    public void log(Level level, Marker marker, Supplier<?> supplier) {
        LoggingEngine.log(log4RichLogger, level, marker, supplier, null);
    }
    
    public void log(Level level, Marker marker, Supplier<?> supplier, Throwable throwable) {
        LoggingEngine.log(log4RichLogger, level, marker, supplier, throwable);
    }
}