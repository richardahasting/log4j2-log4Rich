package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * Convenience class for logger wrappers that need to preserve caller location.
 * Extends {@link AbstractLogger} and wraps an existing {@link ExtendedLogger},
 * delegating all core operations to it.
 *
 * <p>This is the primary base class used by SLF4J's log4j2 binding
 * ({@code Log4jLogger}) and other logging facades that wrap log4j2 loggers.
 * The wrapper preserves the caller's FQCN so that location information
 * (file, class, method, line) reflects the actual call site rather than
 * the wrapper class.</p>
 *
 * @since 1.0.5
 */
public class ExtendedLoggerWrapper extends AbstractLogger {

    private static final long serialVersionUID = 1L;

    /** The wrapped logger instance. */
    protected final ExtendedLogger logger;

    /**
     * Creates a new wrapper around the given logger.
     *
     * @param logger the ExtendedLogger to wrap
     * @param name the logger name
     * @param messageFactory the message factory
     */
    public ExtendedLoggerWrapper(final ExtendedLogger logger, final String name,
                                  final MessageFactory messageFactory) {
        super(name, messageFactory);
        this.logger = logger;
    }

    @Override
    public Level getLevel() {
        return logger.getLevel();
    }

    /**
     * Delegates to the wrapped logger's logMessage.
     */
    @Override
    public void logMessage(final String fqcn, final Level level, final Marker marker,
                           final Message message, final Throwable throwable) {
        logger.logMessage(fqcn, level, marker, message, throwable);
    }

    /**
     * Delegates to the wrapped logger's isEnabled.
     */
    @Override
    public boolean isEnabled(final Level level, final Marker marker,
                             final Message message, final Throwable throwable) {
        return logger.isEnabled(level, marker, message, throwable);
    }
}
