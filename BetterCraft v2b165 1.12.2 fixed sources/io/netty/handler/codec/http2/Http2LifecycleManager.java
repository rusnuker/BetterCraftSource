// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelFuture;

public interface Http2LifecycleManager
{
    void closeStreamLocal(final Http2Stream p0, final ChannelFuture p1);
    
    void closeStreamRemote(final Http2Stream p0, final ChannelFuture p1);
    
    void closeStream(final Http2Stream p0, final ChannelFuture p1);
    
    ChannelFuture resetStream(final ChannelHandlerContext p0, final int p1, final long p2, final ChannelPromise p3);
    
    ChannelFuture goAway(final ChannelHandlerContext p0, final int p1, final long p2, final ByteBuf p3, final ChannelPromise p4);
    
    void onError(final ChannelHandlerContext p0, final Throwable p1);
}
