package org.apache.logging.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * log4j2 LogManager implementation that delegates to log4Rich.
 * Manages logger instances and provides factory methods.
 */
public class LogManager {
    
    // Cache of logger instances
    private static final ConcurrentMap<String, Logger> LOGGERS = new ConcurrentHashMap<>();
    
    // Default message factory
    private static final MessageFactory DEFAULT_MESSAGE_FACTORY = org.apache.logging.log4j.message.DefaultMessageFactory.INSTANCE;
    
    // Prevent instantiation - all methods are static
    private LogManager() {}
    
    // ========== Basic Logger Factory Methods ==========
    
    /**
     * Gets a logger for the calling class.
     */
    public static Logger getLogger() {
        // Get the calling class name
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        // Index 2 because: 0=getStackTrace, 1=getLogger, 2=actual caller
        String className = stack.length > 2 ? stack[2].getClassName() : "ROOT";
        return getLogger(className);
    }
    
    /**
     * Gets a logger for the specified class.
     */
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz != null ? clazz.getName() : "ROOT");
    }
    
    /**
     * Gets a logger with the specified name.
     */
    public static Logger getLogger(String name) {
        if (name == null) {
            name = "ROOT";
        }
        
        return LOGGERS.computeIfAbsent(name, loggerName -> new Logger(loggerName));
    }
    
    /**
     * Gets the root logger.
     */
    public static Logger getRootLogger() {
        return getLogger("ROOT");
    }
    
    // ========== Logger Factory Methods with MessageFactory ==========
    
    /**
     * Gets a logger for the calling class with the specified message factory.
     */
    public static Logger getLogger(MessageFactory messageFactory) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String className = stack.length > 2 ? stack[2].getClassName() : "ROOT";
        return getLogger(className, messageFactory);
    }
    
    /**
     * Gets a logger for the specified class with the specified message factory.
     */
    public static Logger getLogger(Class<?> clazz, MessageFactory messageFactory) {
        return getLogger(clazz != null ? clazz.getName() : "ROOT", messageFactory);
    }
    
    /**
     * Gets a logger with the specified name and message factory.
     */
    public static Logger getLogger(String name, MessageFactory messageFactory) {
        if (name == null) {
            name = "ROOT";
        }
        
        final String loggerName = name;
        final MessageFactory factory = messageFactory != null ? messageFactory : DEFAULT_MESSAGE_FACTORY;
        
        // For loggers with custom message factories, we create a unique key
        String cacheKey = loggerName + "#" + factory.getClass().getName();
        
        return LOGGERS.computeIfAbsent(cacheKey, key -> new Logger(loggerName, factory));
    }
    
    // ========== Logger Management Methods ==========
    
    /**
     * Checks if a logger with the specified name exists.
     */
    public static boolean exists(String name) {
        if (name == null) {
            return false;
        }
        
        // Check both direct name and with default message factory suffix
        return LOGGERS.containsKey(name) || 
               LOGGERS.containsKey(name + "#" + DEFAULT_MESSAGE_FACTORY.getClass().getName());
    }
    
    /**
     * Gets all existing logger names.
     */
    public static String[] getLoggerNames() {
        return LOGGERS.keySet().stream()
            .map(key -> key.contains("#") ? key.substring(0, key.indexOf("#")) : key)
            .distinct()
            .toArray(String[]::new);
    }
    
    /**
     * Gets the number of existing loggers.
     */
    public static int getLoggerCount() {
        return LOGGERS.size();
    }
    
    // ========== Factory and Context Methods ==========
    
    /**
     * Gets the logger factory (returns this LogManager).
     */
    public static LoggerFactory getFactory() {
        return FACTORY_INSTANCE;
    }
    
    /**
     * Gets the logging context.
     * For now, returns a simple context wrapper.
     */
    public static LoggerContext getContext() {
        return LoggerContextImpl.INSTANCE;
    }
    
    /**
     * Gets the logging context with class loader consideration.
     */
    public static LoggerContext getContext(boolean currentContext) {
        return getContext();
    }
    
    /**
     * Gets the logging context for the specified class loader.
     */
    public static LoggerContext getContext(ClassLoader loader) {
        return getContext();
    }
    
    // ========== Shutdown Methods ==========
    
    /**
     * Shuts down the logging system.
     */
    public static void shutdown() {
        shutdown(true);
    }
    
    /**
     * Shuts down the logging system.
     */
    public static void shutdown(boolean currentContext) {
        // Delegate to log4Rich shutdown
        try {
            com.log4rich.Log4Rich.shutdown();
        } catch (Exception e) {
            // Ignore shutdown errors
        }
        
        // Clear our logger cache
        LOGGERS.clear();
    }
    
    /**
     * Shuts down the logging system with timeout.
     */
    public static void shutdown(long timeout, java.util.concurrent.TimeUnit timeUnit) {
        shutdown();
    }
    
    // ========== Inner Classes for Factory and Context ==========
    
    private static final LoggerFactory FACTORY_INSTANCE = new LoggerFactory() {
        @Override
        public Logger getLogger(String name) {
            return LogManager.getLogger(name);
        }
        
        @Override
        public Logger getLogger(String name, MessageFactory messageFactory) {
            return LogManager.getLogger(name, messageFactory);
        }
    };
}

/**
 * Simple logger factory interface.
 */
interface LoggerFactory {
    Logger getLogger(String name);
    Logger getLogger(String name, MessageFactory messageFactory);
}

/**
 * Simple logger context implementation.
 */
class LoggerContext {
    public String getName() {
        return "log4j2-log4Rich-Context";
    }
}

/**
 * Default logger context implementation.
 */
class LoggerContextImpl extends LoggerContext {
    static final LoggerContext INSTANCE = new LoggerContextImpl();
    
    private LoggerContextImpl() {}
}