package org.apache.logging.log4j.util;

/**
 * Utility for locating classes in the call stack.
 * Used by LogManager to determine calling class.
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
     * Gets the caller class, skipping the specified number of frames.
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
}
