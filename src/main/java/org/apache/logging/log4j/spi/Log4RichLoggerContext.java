package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
    public Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger(String name, MessageFactory messageFactory) {
        return LogManager.getLogger(name, messageFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(String name) {
        return LogManager.exists(name);
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
            ? name + "#" + messageFactory.getClass().getName()
            : name;
        return LogManager.exists(cacheKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
        if (name == null || messageFactoryClass == null) {
            return false;
        }
        String cacheKey = name + "#" + messageFactoryClass.getName();
        return LogManager.exists(cacheKey);
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