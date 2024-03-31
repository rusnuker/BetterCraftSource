// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

import java.util.logging.Logger;

public class JdkLoggerFactory extends InternalLoggerFactory
{
    public static final InternalLoggerFactory INSTANCE;
    
    @Deprecated
    public JdkLoggerFactory() {
    }
    
    public InternalLogger newInstance(final String name) {
        return new JdkLogger(Logger.getLogger(name));
    }
    
    static {
        INSTANCE = new JdkLoggerFactory();
    }
}
