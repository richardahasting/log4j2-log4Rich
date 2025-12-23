package org.apache.logging.log4j.util;

/**
 * Utility class for classloader operations.
 */
public final class LoaderUtil {

    private LoaderUtil() {
    }

    /**
     * Gets the thread context classloader if available, otherwise the classloader
     * that loaded this class.
     *
     * @return The appropriate classloader.
     */
    public static ClassLoader getThreadContextClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = LoaderUtil.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Gets the classloader for a given class.
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = clazz.getClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        return cl;
    }

    /**
     * Loads a class by name.
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        return getThreadContextClassLoader().loadClass(className);
    }

    /**
     * Creates a new instance of a class.
     */
    public static <T> T newInstanceOf(Class<T> clazz) throws ReflectiveOperationException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * Creates a new instance of a class by name.
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstanceOf(String className) throws ReflectiveOperationException {
        return (T) loadClass(className).getDeclaredConstructor().newInstance();
    }
}
