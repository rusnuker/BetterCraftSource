// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ipfilter;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.SocketAddress;

public abstract class AbstractRemoteAddressFilter<T extends SocketAddress> extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        this.handleNewChannel(ctx);
        ctx.fireChannelRegistered();
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (!this.handleNewChannel(ctx)) {
            throw new IllegalStateException("cannot determine to accept or reject a channel: " + ctx.channel());
        }
        ctx.fireChannelActive();
    }
    
    private boolean handleNewChannel(final ChannelHandlerContext ctx) throws Exception {
        final T remoteAddress = (T)ctx.channel().remoteAddress();
        if (remoteAddress == null) {
            return false;
        }
        ctx.pipeline().remove(this);
        if (this.accept(ctx, remoteAddress)) {
            this.channelAccepted(ctx, remoteAddress);
        }
        else {
            final ChannelFuture rejectedFuture = this.channelRejected(ctx, remoteAddress);
            if (rejectedFuture != null) {
                rejectedFuture.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
            }
            else {
                ctx.close();
            }
        }
        return true;
    }
    
    protected abstract boolean accept(final ChannelHandlerContext p0, final T p1) throws Exception;
    
    protected void channelAccepted(final ChannelHandlerContext ctx, final T remoteAddress) {
    }
    
    protected ChannelFuture channelRejected(final ChannelHandlerContext ctx, final T remoteAddress) {
        return null;
    }
}
