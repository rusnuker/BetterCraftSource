// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

public interface HttpResponse extends HttpMessage
{
    HttpResponseStatus getStatus();
    
    HttpResponse setStatus(final HttpResponseStatus p0);
    
    HttpResponse setProtocolVersion(final HttpVersion p0);
}
