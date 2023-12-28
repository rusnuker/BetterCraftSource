// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public interface Http2FrameReader extends Closeable
{
    void readFrame(final ChannelHandlerContext p0, final ByteBuf p1, final Http2FrameListener p2) throws Http2Exception;
    
    Configuration configuration();
    
    void close();
    
    public interface Configuration
    {
        Http2HeadersDecoder.Configuration headersConfiguration();
        
        Http2FrameSizePolicy frameSizePolicy();
    }
}
