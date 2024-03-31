// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.logging;

public interface ILogger
{
    String getId();
    
    String getType();
    
    void catching(final Level p0, final Throwable p1);
    
    void catching(final Throwable p0);
    
    void debug(final String p0, final Object... p1);
    
    void debug(final String p0, final Throwable p1);
    
    void error(final String p0, final Object... p1);
    
    void error(final String p0, final Throwable p1);
    
    void fatal(final String p0, final Object... p1);
    
    void fatal(final String p0, final Throwable p1);
    
    void info(final String p0, final Object... p1);
    
    void info(final String p0, final Throwable p1);
    
    void log(final Level p0, final String p1, final Object... p2);
    
    void log(final Level p0, final String p1, final Throwable p2);
    
     <T extends Throwable> T throwing(final T p0);
    
    void trace(final String p0, final Object... p1);
    
    void trace(final String p0, final Throwable p1);
    
    void warn(final String p0, final Object... p1);
    
    void warn(final String p0, final Throwable p1);
}
