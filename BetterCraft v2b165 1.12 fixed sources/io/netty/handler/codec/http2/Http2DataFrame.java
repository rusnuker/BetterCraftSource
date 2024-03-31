// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface Http2DataFrame extends Http2StreamFrame, ByteBufHolder
{
    boolean isEndStream();
    
    int padding();
    
    ByteBuf content();
    
    Http2DataFrame copy();
    
    Http2DataFrame duplicate();
    
    Http2DataFrame retainedDuplicate();
    
    Http2DataFrame replace(final ByteBuf p0);
    
    Http2DataFrame retain();
    
    Http2DataFrame retain(final int p0);
    
    Http2DataFrame touch();
    
    Http2DataFrame touch(final Object p0);
}
