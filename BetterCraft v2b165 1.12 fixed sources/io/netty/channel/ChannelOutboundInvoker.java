// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import java.net.SocketAddress;

public interface ChannelOutboundInvoker
{
    ChannelFuture bind(final SocketAddress p0);
    
    ChannelFuture connect(final SocketAddress p0);
    
    ChannelFuture connect(final SocketAddress p0, final SocketAddress p1);
    
    ChannelFuture disconnect();
    
    ChannelFuture close();
    
    ChannelFuture deregister();
    
    ChannelFuture bind(final SocketAddress p0, final ChannelPromise p1);
    
    ChannelFuture connect(final SocketAddress p0, final ChannelPromise p1);
    
    ChannelFuture connect(final SocketAddress p0, final SocketAddress p1, final ChannelPromise p2);
    
    ChannelFuture disconnect(final ChannelPromise p0);
    
    ChannelFuture close(final ChannelPromise p0);
    
    ChannelFuture deregister(final ChannelPromise p0);
    
    ChannelOutboundInvoker read();
    
    ChannelFuture write(final Object p0);
    
    ChannelFuture write(final Object p0, final ChannelPromise p1);
    
    ChannelOutboundInvoker flush();
    
    ChannelFuture writeAndFlush(final Object p0, final ChannelPromise p1);
    
    ChannelFuture writeAndFlush(final Object p0);
    
    ChannelPromise newPromise();
    
    ChannelProgressivePromise newProgressivePromise();
    
    ChannelFuture newSucceededFuture();
    
    ChannelFuture newFailedFuture(final Throwable p0);
    
    ChannelPromise voidPromise();
}
