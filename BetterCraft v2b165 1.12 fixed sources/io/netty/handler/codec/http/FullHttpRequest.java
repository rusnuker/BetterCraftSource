// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpRequest extends HttpRequest, FullHttpMessage
{
    FullHttpRequest copy();
    
    FullHttpRequest duplicate();
    
    FullHttpRequest retainedDuplicate();
    
    FullHttpRequest replace(final ByteBuf p0);
    
    FullHttpRequest retain(final int p0);
    
    FullHttpRequest retain();
    
    FullHttpRequest touch();
    
    FullHttpRequest touch(final Object p0);
    
    FullHttpRequest setProtocolVersion(final HttpVersion p0);
    
    FullHttpRequest setMethod(final HttpMethod p0);
    
    FullHttpRequest setUri(final String p0);
}
