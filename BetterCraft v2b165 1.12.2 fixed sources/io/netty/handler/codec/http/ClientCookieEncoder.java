// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

@Deprecated
public final class ClientCookieEncoder
{
    @Deprecated
    public static String encode(final String name, final String value) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(name, value);
    }
    
    @Deprecated
    public static String encode(final Cookie cookie) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookie);
    }
    
    @Deprecated
    public static String encode(final Cookie... cookies) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode((io.netty.handler.codec.http.cookie.Cookie[])cookies);
    }
    
    @Deprecated
    public static String encode(final Iterable<Cookie> cookies) {
        return io.netty.handler.codec.http.cookie.ClientCookieEncoder.LAX.encode(cookies);
    }
    
    private ClientCookieEncoder() {
    }
}
