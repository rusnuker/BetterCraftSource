// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.status;

import org.apache.logging.log4j.Level;

public interface StatusListener
{
    void log(final StatusData p0);
    
    Level getStatusLevel();
}
