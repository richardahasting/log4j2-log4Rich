package org.apache.logging.log4j.spi;

import java.net.URL;

/**
 * Provider class for log4j2 SPI.
 * Used by ServiceLoader to discover the logging implementation.
 */
public class Provider {

    public static final String CURRENT_VERSION = "2.20.0";
    public static final int PRIORITY = 15; // Higher than default

    private final int priority;
    private final String version;
    private final Class<? extends LoggerContextFactory> loggerContextFactoryClass;
    private final String threadContextMap;
    private final URL url;

    /**
     * Default constructor for ServiceLoader.
     */
    public Provider() {
        this.priority = PRIORITY;
        this.version = CURRENT_VERSION;
        this.loggerContextFactoryClass = Log4RichLoggerContextFactory.class;
        this.threadContextMap = null;
        this.url = null;
    }

    public Provider(int priority, String version,
                   Class<? extends LoggerContextFactory> loggerContextFactoryClass) {
        this.priority = priority;
        this.version = version;
        this.loggerContextFactoryClass = loggerContextFactoryClass;
        this.threadContextMap = null;
        this.url = null;
    }

    public int getPriority() {
        return priority;
    }

    public String getVersion() {
        return version;
    }

    public Class<? extends LoggerContextFactory> loadLoggerContextFactory() {
        return loggerContextFactoryClass;
    }

    public String getThreadContextMap() {
        return threadContextMap;
    }

    public URL getUrl() {
        return url;
    }

    public String getClassName() {
        return loggerContextFactoryClass != null ? loggerContextFactoryClass.getName() : null;
    }
}
