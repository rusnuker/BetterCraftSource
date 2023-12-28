// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory
{
    LoggerContext getContext(final String p0, final ClassLoader p1, final Object p2, final boolean p3);
    
    LoggerContext getContext(final String p0, final ClassLoader p1, final Object p2, final boolean p3, final URI p4, final String p5);
    
    void removeContext(final LoggerContext p0);
}
