// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext
{
    Object getExternalContext();
    
    ExtendedLogger getLogger(final String p0);
    
    ExtendedLogger getLogger(final String p0, final MessageFactory p1);
    
    boolean hasLogger(final String p0);
    
    boolean hasLogger(final String p0, final MessageFactory p1);
    
    boolean hasLogger(final String p0, final Class<? extends MessageFactory> p1);
}
