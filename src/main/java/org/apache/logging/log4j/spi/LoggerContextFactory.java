package org.apache.logging.log4j.spi;

import java.net.URI;

/**
 * LoggerContextFactory interface for log4j2 SPI compatibility.
 *
 * <p>This interface is used by {@link org.apache.logging.log4j.LogManager} to
 * create and manage {@link LoggerContext} instances. Implementations of this
 * interface are responsible for creating LoggerContext objects that provide
 * logger instances to applications.</p>
 *
 * <p>Spring Boot and other frameworks use this interface to integrate with
 * log4j2, so it's essential for compatibility.</p>
 *
 * @since 1.0.0
 */
public interface LoggerContextFactory {

    /**
     * Creates a LoggerContext.
     *
     * @param fqcn the fully qualified class name of the caller
     * @param loader the class loader to use (may be null)
     * @param externalContext an external context object (may be null)
     * @param currentContext if true, returns the current context; if false,
     *                       returns the context appropriate for the caller
     * @return the LoggerContext
     */
    LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext);

    /**
     * Creates a LoggerContext with a specific configuration location.
     *
     * @param fqcn the fully qualified class name of the caller
     * @param loader the class loader to use (may be null)
     * @param externalContext an external context object (may be null)
     * @param currentContext if true, returns the current context; if false,
     *                       returns the context appropriate for the caller
     * @param configLocation the location of the configuration (may be null)
     * @param name the context name (may be null)
     * @return the LoggerContext
     */
    LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext,
                            boolean currentContext, URI configLocation, String name);

    /**
     * Checks if a LoggerContext is installed.
     *
     * @param fqcn the fully qualified class name of the caller
     * @param loader the class loader (may be null)
     * @param currentContext if true, checks for the current context
     * @return true if a context is installed, false otherwise
     */
    default boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return false;
    }

    /**
     * Removes a LoggerContext.
     *
     * @param context the context to remove
     */
    void removeContext(LoggerContext context);

    /**
     * Determines if this factory depends on the caller's classloader.
     *
     * @return true if classloader dependent, false otherwise
     */
    default boolean isClassLoaderDependent() {
        return true;
    }
}