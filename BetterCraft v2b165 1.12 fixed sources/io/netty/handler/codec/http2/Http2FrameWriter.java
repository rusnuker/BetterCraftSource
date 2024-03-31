// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public interface Http2FrameWriter extends Http2DataWriter, Closeable
{
    ChannelFuture writeHeaders(final ChannelHandlerContext p0, final int p1, final Http2Headers p2, final int p3, final boolean p4, final ChannelPromise p5);
    
    ChannelFuture writeHeaders(final ChannelHandlerContext p0, final int p1, final Http2Headers p2, final int p3, final short p4, final boolean p5, final int p6, final boolean p7, final ChannelPromise p8);
    
    ChannelFuture writePriority(final ChannelHandlerContext p0, final int p1, final int p2, final short p3, final boolean p4, final ChannelPromise p5);
    
    ChannelFuture writeRstStream(final ChannelHandlerContext p0, final int p1, final long p2, final ChannelPromise p3);
    
    ChannelFuture writeSettings(final ChannelHandlerContext p0, final Http2Settings p1, final ChannelPromise p2);
    
    ChannelFuture writeSettingsAck(final ChannelHandlerContext p0, final ChannelPromise p1);
    
    ChannelFuture writePing(final ChannelHandlerContext p0, final boolean p1, final ByteBuf p2, final ChannelPromise p3);
    
    ChannelFuture writePushPromise(final ChannelHandlerContext p0, final int p1, final int p2, final Http2Headers p3, final int p4, final ChannelPromise p5);
    
    ChannelFuture writeGoAway(final ChannelHandlerContext p0, final int p1, final long p2, final ByteBuf p3, final ChannelPromise p4);
    
    ChannelFuture writeWindowUpdate(final ChannelHandlerContext p0, final int p1, final int p2, final ChannelPromise p3);
    
    ChannelFuture writeFrame(final ChannelHandlerContext p0, final byte p1, final int p2, final Http2Flags p3, final ByteBuf p4, final ChannelPromise p5);
    
    Configuration configuration();
    
    void close();
    
    public interface Configuration
    {
        Http2HeadersEncoder.Configuration headersConfiguration();
        
        Http2FrameSizePolicy frameSizePolicy();
    }
}
