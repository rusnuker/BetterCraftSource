// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.AsciiString;

public final class HttpScheme
{
    public static final HttpScheme HTTP;
    public static final HttpScheme HTTPS;
    private final int port;
    private final AsciiString name;
    
    private HttpScheme(final int port, final String name) {
        this.port = port;
        this.name = new AsciiString(name);
    }
    
    public AsciiString name() {
        return this.name;
    }
    
    public int port() {
        return this.port;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof HttpScheme)) {
            return false;
        }
        final HttpScheme other = (HttpScheme)o;
        return other.port() == this.port && other.name().equals(this.name);
    }
    
    @Override
    public int hashCode() {
        return this.port * 31 + this.name.hashCode();
    }
    
    @Override
    public String toString() {
        return this.name.toString();
    }
    
    static {
        HTTP = new HttpScheme(80, "http");
        HTTPS = new HttpScheme(443, "https");
    }
}
