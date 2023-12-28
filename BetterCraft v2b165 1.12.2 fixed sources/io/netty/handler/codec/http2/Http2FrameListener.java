// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2FrameListener
{
    int onDataRead(final ChannelHandlerContext p0, final int p1, final ByteBuf p2, final int p3, final boolean p4) throws Http2Exception;
    
    void onHeadersRead(final ChannelHandlerContext p0, final int p1, final Http2Headers p2, final int p3, final boolean p4) throws Http2Exception;
    
    void onHeadersRead(final ChannelHandlerContext p0, final int p1, final Http2Headers p2, final int p3, final short p4, final boolean p5, final int p6, final boolean p7) throws Http2Exception;
    
    void onPriorityRead(final ChannelHandlerContext p0, final int p1, final int p2, final short p3, final boolean p4) throws Http2Exception;
    
    void onRstStreamRead(final ChannelHandlerContext p0, final int p1, final long p2) throws Http2Exception;
    
    void onSettingsAckRead(final ChannelHandlerContext p0) throws Http2Exception;
    
    void onSettingsRead(final ChannelHandlerContext p0, final Http2Settings p1) throws Http2Exception;
    
    void onPingRead(final ChannelHandlerContext p0, final ByteBuf p1) throws Http2Exception;
    
    void onPingAckRead(final ChannelHandlerContext p0, final ByteBuf p1) throws Http2Exception;
    
    void onPushPromiseRead(final ChannelHandlerContext p0, final int p1, final int p2, final Http2Headers p3, final int p4) throws Http2Exception;
    
    void onGoAwayRead(final ChannelHandlerContext p0, final int p1, final long p2, final ByteBuf p3) throws Http2Exception;
    
    void onWindowUpdateRead(final ChannelHandlerContext p0, final int p1, final int p2) throws Http2Exception;
    
    void onUnknownFrame(final ChannelHandlerContext p0, final byte p1, final int p2, final Http2Flags p3, final ByteBuf p4) throws Http2Exception;
}
