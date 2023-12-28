// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

public interface HttpRequest extends HttpMessage
{
    @Deprecated
    HttpMethod getMethod();
    
    HttpMethod method();
    
    HttpRequest setMethod(final HttpMethod p0);
    
    @Deprecated
    String getUri();
    
    String uri();
    
    HttpRequest setUri(final String p0);
    
    HttpRequest setProtocolVersion(final HttpVersion p0);
}
