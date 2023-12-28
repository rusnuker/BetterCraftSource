// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

import org.apache.logging.log4j.LogManager;

public final class Log4J2LoggerFactory extends InternalLoggerFactory
{
    public static final InternalLoggerFactory INSTANCE;
    
    @Deprecated
    public Log4J2LoggerFactory() {
    }
    
    public InternalLogger newInstance(final String name) {
        return new Log4J2Logger(LogManager.getLogger(name));
    }
    
    static {
        INSTANCE = new Log4J2LoggerFactory();
    }
}
