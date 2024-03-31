// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;

public enum ThreadNameCachingStrategy
{
    CACHED {
        public String getThreadName() {
            String result = ThreadNameCachingStrategy.THREADLOCAL_NAME.get();
            if (result == null) {
                result = Thread.currentThread().getName();
                ThreadNameCachingStrategy.THREADLOCAL_NAME.set(result);
            }
            return result;
        }
    }, 
    UNCACHED {
        public String getThreadName() {
            return Thread.currentThread().getName();
        }
    };
    
    private static final StatusLogger LOGGER;
    private static final ThreadLocal<String> THREADLOCAL_NAME;
    
    abstract String getThreadName();
    
    public static ThreadNameCachingStrategy create() {
        final String name = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ThreadNameStrategy", ThreadNameCachingStrategy.CACHED.name());
        try {
            final ThreadNameCachingStrategy result = valueOf(name);
            ThreadNameCachingStrategy.LOGGER.debug("AsyncLogger.ThreadNameStrategy={}", result);
            return result;
        }
        catch (final Exception ex) {
            ThreadNameCachingStrategy.LOGGER.debug("Using AsyncLogger.ThreadNameStrategy.CACHED: '{}' not valid: {}", name, ex.toString());
            return ThreadNameCachingStrategy.CACHED;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        THREADLOCAL_NAME = new ThreadLocal<String>();
    }
}
