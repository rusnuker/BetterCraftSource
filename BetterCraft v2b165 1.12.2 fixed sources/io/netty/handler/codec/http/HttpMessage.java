// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

public interface HttpMessage extends HttpObject
{
    @Deprecated
    HttpVersion getProtocolVersion();
    
    HttpVersion protocolVersion();
    
    HttpMessage setProtocolVersion(final HttpVersion p0);
    
    HttpHeaders headers();
}
