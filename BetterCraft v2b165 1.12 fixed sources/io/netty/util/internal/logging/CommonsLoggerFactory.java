// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

import org.apache.commons.logging.LogFactory;

@Deprecated
public class CommonsLoggerFactory extends InternalLoggerFactory
{
    public static final InternalLoggerFactory INSTANCE;
    
    @Deprecated
    public CommonsLoggerFactory() {
    }
    
    public InternalLogger newInstance(final String name) {
        return new CommonsLogger(LogFactory.getLog(name), name);
    }
    
    static {
        INSTANCE = new CommonsLoggerFactory();
    }
}
