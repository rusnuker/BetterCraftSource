// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.logging;

public interface InternalLogger
{
    String name();
    
    boolean isTraceEnabled();
    
    void trace(final String p0);
    
    void trace(final String p0, final Object p1);
    
    void trace(final String p0, final Object p1, final Object p2);
    
    void trace(final String p0, final Object... p1);
    
    void trace(final String p0, final Throwable p1);
    
    void trace(final Throwable p0);
    
    boolean isDebugEnabled();
    
    void debug(final String p0);
    
    void debug(final String p0, final Object p1);
    
    void debug(final String p0, final Object p1, final Object p2);
    
    void debug(final String p0, final Object... p1);
    
    void debug(final String p0, final Throwable p1);
    
    void debug(final Throwable p0);
    
    boolean isInfoEnabled();
    
    void info(final String p0);
    
    void info(final String p0, final Object p1);
    
    void info(final String p0, final Object p1, final Object p2);
    
    void info(final String p0, final Object... p1);
    
    void info(final String p0, final Throwable p1);
    
    void info(final Throwable p0);
    
    boolean isWarnEnabled();
    
    void warn(final String p0);
    
    void warn(final String p0, final Object p1);
    
    void warn(final String p0, final Object... p1);
    
    void warn(final String p0, final Object p1, final Object p2);
    
    void warn(final String p0, final Throwable p1);
    
    void warn(final Throwable p0);
    
    boolean isErrorEnabled();
    
    void error(final String p0);
    
    void error(final String p0, final Object p1);
    
    void error(final String p0, final Object p1, final Object p2);
    
    void error(final String p0, final Object... p1);
    
    void error(final String p0, final Throwable p1);
    
    void error(final Throwable p0);
    
    boolean isEnabled(final InternalLogLevel p0);
    
    void log(final InternalLogLevel p0, final String p1);
    
    void log(final InternalLogLevel p0, final String p1, final Object p2);
    
    void log(final InternalLogLevel p0, final String p1, final Object p2, final Object p3);
    
    void log(final InternalLogLevel p0, final String p1, final Object... p2);
    
    void log(final InternalLogLevel p0, final String p1, final Throwable p2);
    
    void log(final InternalLogLevel p0, final Throwable p1);
}
