// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.Collection;
import java.util.List;

@Deprecated
public final class ServerCookieEncoder
{
    @Deprecated
    public static String encode(final String name, final String value) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(name, value);
    }
    
    @Deprecated
    public static String encode(final Cookie cookie) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie);
    }
    
    @Deprecated
    public static List<String> encode(final Cookie... cookies) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode((io.netty.handler.codec.http.cookie.Cookie[])cookies);
    }
    
    @Deprecated
    public static List<String> encode(final Collection<Cookie> cookies) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
    }
    
    @Deprecated
    public static List<String> encode(final Iterable<Cookie> cookies) {
        return io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookies);
    }
    
    private ServerCookieEncoder() {
    }
}
