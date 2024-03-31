// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.stream;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

public interface ChunkedInput<B>
{
    boolean isEndOfInput() throws Exception;
    
    void close() throws Exception;
    
    @Deprecated
    B readChunk(final ChannelHandlerContext p0) throws Exception;
    
    B readChunk(final ByteBufAllocator p0) throws Exception;
    
    long length();
    
    long progress();
}
