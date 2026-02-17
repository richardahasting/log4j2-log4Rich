package org.apache.logging.log4j.spi;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * SPI interface for the NDC (Nested Diagnostic Context) stack implementation.
 *
 * @since 1.0.5
 */
public interface ThreadContextStack extends Collection<String> {

    /**
     * Pops the top element from the stack.
     *
     * @return the top element, or empty string if empty
     */
    String pop();

    /**
     * Peeks at the top element without removing it.
     *
     * @return the top element, or null if empty
     */
    String peek();

    /**
     * Pushes an element onto the stack.
     */
    void push(String message);

    /**
     * Returns the depth of the stack.
     */
    int getDepth();

    /**
     * Returns an immutable copy as a List.
     */
    List<String> asList();

    /**
     * Trims the stack to the given depth.
     */
    void trim(int depth);

    /**
     * Returns a copy of this stack.
     */
    ThreadContextStack copy();
}
