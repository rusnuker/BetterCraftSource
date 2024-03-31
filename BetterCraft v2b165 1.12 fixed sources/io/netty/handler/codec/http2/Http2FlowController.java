// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public interface Http2FlowController
{
    void channelHandlerContext(final ChannelHandlerContext p0) throws Http2Exception;
    
    void initialWindowSize(final int p0) throws Http2Exception;
    
    int initialWindowSize();
    
    int windowSize(final Http2Stream p0);
    
    void incrementWindowSize(final Http2Stream p0, final int p1) throws Http2Exception;
}
