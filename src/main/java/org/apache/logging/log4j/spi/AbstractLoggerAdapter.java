package org.apache.logging.log4j.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.LoaderUtil;

/**
 * Abstract base class for logger adapters (like SLF4J adapter).
 * Provides caching of logger instances per LoggerContext.
 *
 * @param <L> The Logger type being adapted to.
 */
public abstract class AbstractLoggerAdapter<L> implements LoggerAdapter<L> {

    /**
     * Map of LoggerContext to a map of logger names to Loggers.
     */
    protected final ConcurrentMap<LoggerContext, ConcurrentMap<String, L>> registry =
            new ConcurrentHashMap<>();

    /**
     * Gets or creates a Logger for the given name in the current context.
     *
     * @param name The name of the Logger to get.
     * @return The Logger.
     */
    public L getLogger(String name) {
        LoggerContext context = getContext();
        ConcurrentMap<String, L> loggers = getLoggersInContext(context);
        L logger = loggers.get(name);
        if (logger != null) {
            return logger;
        }
        loggers.putIfAbsent(name, newLogger(name, context));
        return loggers.get(name);
    }

    /**
     * Gets the logger map for a given context, creating it if needed.
     */
    protected ConcurrentMap<String, L> getLoggersInContext(LoggerContext context) {
        ConcurrentMap<String, L> loggers = registry.get(context);
        if (loggers == null) {
            registry.putIfAbsent(context, new ConcurrentHashMap<String, L>());
            loggers = registry.get(context);
        }
        return loggers;
    }

    /**
     * Creates a new logger instance for the given name.
     *
     * @param name The name of the Logger.
     * @param context The LoggerContext.
     * @return A new Logger instance.
     */
    protected abstract L newLogger(String name, LoggerContext context);

    /**
     * Gets the current LoggerContext.
     *
     * @return The current LoggerContext.
     */
    protected abstract LoggerContext getContext();

    /**
     * Gets the LoggerContext for the given class's classloader.
     * Required by commons-logging 1.3.5+ which calls this from
     * Log4jApiLogFactory$LogAdapter.getContext().
     *
     * @param cls the class whose classloader determines the context, or null
     * @return the LoggerContext for the resolved classloader
     */
    protected LoggerContext getContext(Class<?> cls) {
        ClassLoader loader = null;
        if (cls != null) {
            loader = cls.getClassLoader();
        }
        if (loader == null) {
            loader = LoaderUtil.getThreadContextClassLoader();
        }
        return LogManager.getContext(loader, false);
    }

    /**
     * Closes the adapter and releases resources.
     */
    public void close() {
        registry.clear();
    }
}
