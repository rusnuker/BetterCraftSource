// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2ConnectionEncoder extends Http2FrameWriter
{
    void lifecycleManager(final Http2LifecycleManager p0);
    
    Http2Connection connection();
    
    Http2RemoteFlowController flowController();
    
    Http2FrameWriter frameWriter();
    
    Http2Settings pollSentSettings();
    
    void remoteSettings(final Http2Settings p0) throws Http2Exception;
    
    ChannelFuture writeFrame(final ChannelHandlerContext p0, final byte p1, final int p2, final Http2Flags p3, final ByteBuf p4, final ChannelPromise p5);
}
