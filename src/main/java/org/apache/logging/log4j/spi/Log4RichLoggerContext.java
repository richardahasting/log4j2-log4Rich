package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * log4Rich implementation of the log4j2 LoggerContext.
 *
 * <p>This class provides the bridge between log4j2's SPI layer and log4Rich's
 * logging implementation. It manages logger instances and context objects.</p>
 *
 * @since 1.0.0
 */
public class Log4RichLoggerContext implements LoggerContext {

    /** Singleton instance */
    public static final Log4RichLoggerContext INSTANCE = new Log4RichLoggerContext();

    /** Logger cache - stores Log4RichLogger instances which implement ExtendedLogger */
    private final ConcurrentMap<String, ExtendedLogger> loggers = new ConcurrentHashMap<>();

    /** Context objects storage */
    private final ConcurrentMap<String, Object> contextObjects = new ConcurrentHashMap<>();

    /** External context (optional) */
    private Object externalContext;

    /**
     * Private constructor for singleton pattern.
     */
    private Log4RichLoggerContext() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedLogger getLogger(String name) {
        return loggers.computeIfAbsent(name, Log4RichLogger::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
        String key = name + (messageFactory != null ? ":" + messageFactory.getClass().getName() : "");
        return loggers.computeIfAbsent(key, k -> new Log4RichLogger(name, messageFactory));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(String name) {
        return loggers.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(String name, MessageFactory messageFactory) {
        if (name == null) {
            return false;
        }
        String cacheKey = messageFactory != null
            ? name + ":" + messageFactory.getClass().getName()
            : name;
        return loggers.containsKey(cacheKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
        if (name == null || messageFactoryClass == null) {
            return false;
        }
        String cacheKey = name + ":" + messageFactoryClass.getName();
        return loggers.containsKey(cacheKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getExternalContext() {
        return externalContext;
    }

    /**
     * Sets the external context object.
     *
     * @param externalContext the external context
     */
    public void setExternalContext(Object externalContext) {
        this.externalContext = externalContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(String key) {
        return contextObjects.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object putObject(String key, Object value) {
        if (key == null) {
            return null;
        }
        if (value == null) {
            return contextObjects.remove(key);
        }
        return contextObjects.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object putObjectIfAbsent(String key, Object value) {
        if (key == null || value == null) {
            return null;
        }
        return contextObjects.putIfAbsent(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object removeObject(String key) {
        if (key == null) {
            return null;
        }
        return contextObjects.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeObject(String key, Object value) {
        if (key == null || value == null) {
            return false;
        }
        return contextObjects.remove(key, value);
    }
}
