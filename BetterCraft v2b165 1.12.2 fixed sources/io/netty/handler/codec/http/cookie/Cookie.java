// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

public interface Cookie extends Comparable<Cookie>
{
    public static final long UNDEFINED_MAX_AGE = Long.MIN_VALUE;
    
    String name();
    
    String value();
    
    void setValue(final String p0);
    
    boolean wrap();
    
    void setWrap(final boolean p0);
    
    String domain();
    
    void setDomain(final String p0);
    
    String path();
    
    void setPath(final String p0);
    
    long maxAge();
    
    void setMaxAge(final long p0);
    
    boolean isSecure();
    
    void setSecure(final boolean p0);
    
    boolean isHttpOnly();
    
    void setHttpOnly(final boolean p0);
}
