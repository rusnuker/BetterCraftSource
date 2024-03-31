// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ipfilter;

import java.net.SocketAddress;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ConcurrentSet;
import java.net.InetAddress;
import java.util.Set;
import io.netty.channel.ChannelHandler;
import java.net.InetSocketAddress;

@ChannelHandler.Sharable
public class UniqueIpFilter extends AbstractRemoteAddressFilter<InetSocketAddress>
{
    private final Set<InetAddress> connected;
    
    public UniqueIpFilter() {
        this.connected = new ConcurrentSet<InetAddress>();
    }
    
    @Override
    protected boolean accept(final ChannelHandlerContext ctx, final InetSocketAddress remoteAddress) throws Exception {
        final InetAddress remoteIp = remoteAddress.getAddress();
        if (this.connected.contains(remoteIp)) {
            return false;
        }
        this.connected.add(remoteIp);
        ctx.channel().closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                UniqueIpFilter.this.connected.remove(remoteIp);
            }
        });
        return true;
    }
}
