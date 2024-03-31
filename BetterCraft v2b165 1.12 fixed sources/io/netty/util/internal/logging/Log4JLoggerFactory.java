// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

import org.apache.log4j.Logger;

public class Log4JLoggerFactory extends InternalLoggerFactory
{
    public static final InternalLoggerFactory INSTANCE;
    
    @Deprecated
    public Log4JLoggerFactory() {
    }
    
    public InternalLogger newInstance(final String name) {
        return new Log4JLogger(Logger.getLogger(name));
    }
    
    static {
        INSTANCE = new Log4JLoggerFactory();
    }
}
