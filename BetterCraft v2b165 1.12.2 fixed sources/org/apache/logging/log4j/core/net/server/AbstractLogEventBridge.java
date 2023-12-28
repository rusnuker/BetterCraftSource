// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import java.io.InputStream;

public abstract class AbstractLogEventBridge<T extends InputStream> implements LogEventBridge<T>
{
    protected static final int END = -1;
    protected static final Logger logger;
    
    @Override
    public T wrapStream(final InputStream inputStream) throws IOException {
        return (T)inputStream;
    }
    
    static {
        logger = StatusLogger.getLogger();
    }
}
