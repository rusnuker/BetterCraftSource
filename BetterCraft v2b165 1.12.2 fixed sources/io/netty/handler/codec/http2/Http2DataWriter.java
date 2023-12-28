// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2DataWriter
{
    ChannelFuture writeData(final ChannelHandlerContext p0, final int p1, final ByteBuf p2, final int p3, final boolean p4, final ChannelPromise p5);
}
