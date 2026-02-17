package org.apache.logging.log4j.spi;

/**
 * Standard logging levels as an enum.
 * Maps standard level names to their integer values, matching the constants in
 * {@link org.apache.logging.log4j.Level}.
 *
 * @since 1.0.5
 */
public enum StandardLevel {

    OFF(0),
    FATAL(100),
    ERROR(200),
    WARN(300),
    INFO(400),
    DEBUG(500),
    TRACE(600),
    ALL(Integer.MAX_VALUE);

    private final int intLevel;

    StandardLevel(int intLevel) {
        this.intLevel = intLevel;
    }

    /**
     * Returns the integer value of this level.
     *
     * @return the integer level value
     */
    public int intLevel() {
        return intLevel;
    }

    /**
     * Returns the StandardLevel closest to the given integer value.
     * If the value falls between two standard levels, the more specific
     * (lower intLevel) level is returned.
     *
     * @param intLevel the integer level value
     * @return the nearest StandardLevel
     */
    public static StandardLevel getStandardLevel(int intLevel) {
        StandardLevel[] levels = values();
        StandardLevel result = OFF;
        for (StandardLevel level : levels) {
            if (level.intLevel <= intLevel) {
                result = level;
            }
        }
        return result;
    }
}
