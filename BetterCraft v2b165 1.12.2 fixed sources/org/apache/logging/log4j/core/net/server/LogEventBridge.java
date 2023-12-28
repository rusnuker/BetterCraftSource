// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import org.apache.logging.log4j.core.LogEventListener;
import java.io.InputStream;

public interface LogEventBridge<T extends InputStream>
{
    void logEvents(final T p0, final LogEventListener p1) throws IOException;
    
    T wrapStream(final InputStream p0) throws IOException;
}
