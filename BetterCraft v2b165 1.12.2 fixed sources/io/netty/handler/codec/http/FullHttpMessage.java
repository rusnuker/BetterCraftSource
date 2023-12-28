// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpMessage extends HttpMessage, LastHttpContent
{
    FullHttpMessage copy();
    
    FullHttpMessage duplicate();
    
    FullHttpMessage retainedDuplicate();
    
    FullHttpMessage replace(final ByteBuf p0);
    
    FullHttpMessage retain(final int p0);
    
    FullHttpMessage retain();
    
    FullHttpMessage touch();
    
    FullHttpMessage touch(final Object p0);
}
