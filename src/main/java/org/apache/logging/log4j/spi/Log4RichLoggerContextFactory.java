package org.apache.logging.log4j.spi;

import java.net.URI;

/**
 * log4Rich implementation of the log4j2 LoggerContextFactory.
 *
 * <p>This factory creates and manages {@link Log4RichLoggerContext} instances
 * for the log4j2-log4Rich bridge. It provides a singleton context that is shared
 * across all callers, as log4Rich uses a global logging configuration.</p>
 *
 * <p>This implementation is required for Spring Boot and other frameworks that
 * use the log4j2 SPI to discover and integrate with logging implementations.</p>
 *
 * @since 1.0.0
 */
public class Log4RichLoggerContextFactory implements LoggerContextFactory {

    /** Singleton instance */
    public static final Log4RichLoggerContextFactory INSTANCE = new Log4RichLoggerContextFactory();

    /** The shared logger context */
    private final Log4RichLoggerContext context = Log4RichLoggerContext.INSTANCE;

    /**
     * Private constructor for singleton pattern.
     */
    private Log4RichLoggerContextFactory() {
    }

    /**
     * Creates or retrieves the LoggerContext.
     *
     * <p>This implementation always returns the same singleton context, as log4Rich
     * uses a global configuration. The classloader and external context parameters
     * are stored in the context for potential future use, but do not affect which
     * context is returned.</p>
     *
     * @param fqcn the fully qualified class name of the caller (ignored)
     * @param loader the class loader to use (ignored, log4Rich uses global config)
     * @param externalContext an external context object (stored in the context)
     * @param currentContext if true, returns the current context (always the same)
     * @return the singleton LoggerContext
     */
    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
        // Store external context if provided
        if (externalContext != null) {
            context.setExternalContext(externalContext);
        }
        return context;
    }

    /**
     * Creates or retrieves the LoggerContext with a specific configuration location.
     *
     * <p>This implementation ignores the configLocation and name parameters, as
     * log4Rich configuration is managed through its own configuration system.
     * The method always returns the same singleton context.</p>
     *
     * @param fqcn the fully qualified class name of the caller (ignored)
     * @param loader the class loader to use (ignored)
     * @param externalContext an external context object (stored in the context)
     * @param currentContext if true, returns the current context (always the same)
     * @param configLocation the location of the configuration (ignored)
     * @param name the context name (ignored)
     * @return the singleton LoggerContext
     */
    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext,
                                    boolean currentContext, URI configLocation, String name) {
        // Delegate to the simpler method
        return getContext(fqcn, loader, externalContext, currentContext);
    }

    /**
     * Checks if a LoggerContext is installed.
     *
     * <p>This implementation always returns true, as the context is always available.</p>
     *
     * @param fqcn the fully qualified class name of the caller (ignored)
     * @param loader the class loader (ignored)
     * @param currentContext if true, checks for the current context (ignored)
     * @return always returns true
     */
    @Override
    public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return true;
    }

    /**
     * Removes a LoggerContext.
     *
     * <p>This implementation is a no-op, as log4Rich uses a singleton context
     * that cannot be removed. The context remains available for the lifetime
     * of the application.</p>
     *
     * @param context the context to remove (ignored)
     */
    @Override
    public void removeContext(LoggerContext context) {
        // No-op: log4Rich uses a singleton context that cannot be removed
        // The context remains for the lifetime of the application
    }

    /**
     * Determines if this factory depends on the caller's classloader.
     *
     * <p>This implementation returns false, as log4Rich uses a global
     * configuration that is independent of classloaders.</p>
     *
     * @return always returns false
     */
    @Override
    public boolean isClassLoaderDependent() {
        return false;
    }
}