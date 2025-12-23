package org.apache.logging.log4j;

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

    // TRACE
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

    // DEBUG
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

    // INFO
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

    // WARN
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

    // ERROR
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

    // FATAL
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

    // Generic level
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
}
