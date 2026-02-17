package org.apache.logging.log4j.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

/**
 * Utility for locating classes in the call stack.
 * Used by LogManager and AbstractLoggerAdapter to determine calling class.
 *
 * <p>This implementation uses Java 9+ StackWalker for efficient stack walking.
 * All method signatures match the real log4j-api to ensure compatibility with
 * libraries like commons-logging 1.3.x, Spring Boot, and SLF4J bindings.</p>
 *
 * @since 1.0.0
 */
public final class StackLocatorUtil {

    private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    private StackLocatorUtil() {
    }

    /**
     * Gets the calling class at the specified depth.
     *
     * @param depth The number of stack frames to skip.
     * @return The class at the specified depth, or null if not found.
     */
    public static Class<?> getCallerClass(int depth) {
        return WALKER.walk(frames -> frames
                .skip(depth + 1)
                .findFirst()
                .map(StackWalker.StackFrame::getDeclaringClass)
                .orElse(null));
    }

    /**
     * Gets the calling class, skipping this utility and common framework classes.
     *
     * @param fqcn Fully qualified class name of the calling logger class to skip.
     * @return The caller's class.
     */
    public static Class<?> getCallerClass(String fqcn) {
        return WALKER.walk(frames -> frames
                .filter(f -> !f.getClassName().equals(fqcn))
                .filter(f -> !f.getClassName().startsWith("org.apache.logging.log4j"))
                .filter(f -> !f.getClassName().startsWith("org.slf4j"))
                .filter(f -> !f.getClassName().startsWith("com.log4rich"))
                .findFirst()
                .map(StackWalker.StackFrame::getDeclaringClass)
                .orElse(null));
    }

    /**
     * Gets the calling class, skipping the FQCN class and any classes in the specified package.
     *
     * @param fqcn Fully qualified class name to skip.
     * @param pkg Package name to skip.
     * @return The caller's class.
     */
    public static Class<?> getCallerClass(String fqcn, String pkg) {
        return WALKER.walk(frames -> frames
                .filter(f -> !f.getClassName().equals(fqcn))
                .filter(f -> pkg == null || !f.getClassName().startsWith(pkg))
                .filter(f -> !f.getClassName().startsWith("org.apache.logging.log4j"))
                .filter(f -> !f.getClassName().startsWith("org.slf4j"))
                .filter(f -> !f.getClassName().startsWith("com.log4rich"))
                .findFirst()
                .map(StackWalker.StackFrame::getDeclaringClass)
                .orElse(null));
    }

    /**
     * Gets the first caller class after the sentinel class in the stack.
     * Walks the stack, drops frames until it finds the sentinel class,
     * then drops all frames belonging to the sentinel class, and returns
     * the first frame after that.
     *
     * <p>This is the critical overload used by commons-logging 1.3.x's
     * {@code Log4jApiLogFactory} to resolve the caller's LoggerContext.</p>
     *
     * @param sentinelClass The class to use as a sentinel/anchor point.
     * @return The first caller class after the sentinel, or null if not found.
     */
    public static Class<?> getCallerClass(Class<?> sentinelClass) {
        if (sentinelClass == null) {
            return null;
        }
        return WALKER.walk(frames -> frames
                .dropWhile(f -> !f.getDeclaringClass().equals(sentinelClass))
                .dropWhile(f -> f.getDeclaringClass().equals(sentinelClass))
                .findFirst()
                .map(StackWalker.StackFrame::getDeclaringClass)
                .orElse(null));
    }

    /**
     * Gets the first caller class after the sentinel class that matches the predicate.
     *
     * @param sentinelClass The class to use as a sentinel/anchor point.
     * @param callerPredicate Predicate to filter candidate caller classes.
     * @return The first matching caller class after the sentinel, or null if not found.
     */
    public static Class<?> getCallerClass(Class<?> sentinelClass, Predicate<Class<?>> callerPredicate) {
        if (sentinelClass == null) {
            return null;
        }
        return WALKER.walk(frames -> frames
                .dropWhile(f -> !f.getDeclaringClass().equals(sentinelClass))
                .dropWhile(f -> f.getDeclaringClass().equals(sentinelClass))
                .map(StackWalker.StackFrame::getDeclaringClass)
                .filter(callerPredicate != null ? callerPredicate : c -> true)
                .findFirst()
                .orElse(null));
    }

    /**
     * Gets the caller class, skipping past the anchor class and then the specified number
     * of additional frames.
     *
     * @param anchor The anchor class to find in the stack.
     * @param skip Number of additional frames to skip after finding the anchor.
     * @return The caller class, or null if not found.
     */
    public static Class<?> getCallerClass(Class<?> anchor, int skip) {
        String anchorName = anchor.getName();
        return WALKER.walk(frames -> frames
                .dropWhile(f -> !f.getClassName().equals(anchorName))
                .skip(skip + 1)
                .findFirst()
                .map(StackWalker.StackFrame::getDeclaringClass)
                .orElse(null));
    }

    /**
     * Gets the ClassLoader for the caller at the specified stack depth.
     *
     * @param depth The depth in the call stack.
     * @return The ClassLoader, or null if not found.
     */
    public static ClassLoader getCallerClassLoader(int depth) {
        Class<?> callerClass = getCallerClass(depth + 1);
        return callerClass != null ? callerClass.getClassLoader() : null;
    }

    /**
     * Gets the StackTraceElement at the specified depth.
     *
     * @param depth The depth in the call stack.
     * @return The StackTraceElement, or null if not found.
     */
    public static StackTraceElement getStackTraceElement(int depth) {
        return WALKER.walk(frames -> frames
                .skip(depth + 1)
                .findFirst()
                .map(StackWalker.StackFrame::toStackTraceElement)
                .orElse(null));
    }

    /**
     * Returns the current stack trace as a Deque of classes.
     *
     * @return A Deque of classes in the current call stack.
     */
    public static Deque<Class<?>> getCurrentStackTrace() {
        return WALKER.walk(frames -> {
            Deque<Class<?>> deque = new ArrayDeque<>();
            frames.forEach(f -> deque.addLast(f.getDeclaringClass()));
            return deque;
        });
    }

    /**
     * Calculates the location (StackTraceElement) of the caller of the logger.
     * Walks past the specified logger FQCN to find the actual application caller.
     *
     * @param fqcnOfLogger The fully qualified class name of the logger implementation.
     * @return The StackTraceElement of the caller, or null if not found.
     */
    public static StackTraceElement calcLocation(String fqcnOfLogger) {
        if (fqcnOfLogger == null) {
            return null;
        }
        return WALKER.walk(frames -> frames
                .dropWhile(f -> !f.getClassName().equals(fqcnOfLogger))
                .dropWhile(f -> f.getClassName().equals(fqcnOfLogger))
                .findFirst()
                .map(StackWalker.StackFrame::toStackTraceElement)
                .orElse(null));
    }
}
