// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpResponse extends HttpResponse, FullHttpMessage
{
    FullHttpResponse copy();
    
    FullHttpResponse duplicate();
    
    FullHttpResponse retainedDuplicate();
    
    FullHttpResponse replace(final ByteBuf p0);
    
    FullHttpResponse retain(final int p0);
    
    FullHttpResponse retain();
    
    FullHttpResponse touch();
    
    FullHttpResponse touch(final Object p0);
    
    FullHttpResponse setProtocolVersion(final HttpVersion p0);
    
    FullHttpResponse setStatus(final HttpResponseStatus p0);
}
