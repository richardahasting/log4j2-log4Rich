package com.log4rich.log4j2.bridge;

import com.log4rich.core.ContextProvider;
import java.util.List;
import java.util.Map;

/**
 * ContextProvider implementation that bridges log4j2 ThreadContext to log4Rich.
 * This allows log4Rich to access MDC and NDC data from log4j2's ThreadContext.
 */
public class ThreadContextProvider implements ContextProvider {
    
    public static final ContextProvider INSTANCE = new ThreadContextProvider();
    
    private ThreadContextProvider() {}
    
    @Override
    public Map<String, String> getMDC() {
        return ContextBridge.getContext();
    }
    
    @Override
    public List<String> getNDC() {
        return ContextBridge.getImmutableStack();
    }
    
    @Override
    public boolean hasContext() {
        return !ContextBridge.INSTANCE.isEmpty();
    }
}