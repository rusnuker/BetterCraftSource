// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

public class Slf4JLoggerFactory extends InternalLoggerFactory
{
    public static final InternalLoggerFactory INSTANCE;
    
    @Deprecated
    public Slf4JLoggerFactory() {
    }
    
    Slf4JLoggerFactory(final boolean failIfNOP) {
        assert failIfNOP;
        if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
            throw new NoClassDefFoundError("NOPLoggerFactory not supported");
        }
    }
    
    public InternalLogger newInstance(final String name) {
        return new Slf4JLogger(LoggerFactory.getLogger(name));
    }
    
    static {
        INSTANCE = new Slf4JLoggerFactory();
    }
}
