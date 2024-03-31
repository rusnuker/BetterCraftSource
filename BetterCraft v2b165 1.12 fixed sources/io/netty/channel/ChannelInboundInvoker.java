// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public interface ChannelInboundInvoker
{
    ChannelInboundInvoker fireChannelRegistered();
    
    ChannelInboundInvoker fireChannelUnregistered();
    
    ChannelInboundInvoker fireChannelActive();
    
    ChannelInboundInvoker fireChannelInactive();
    
    ChannelInboundInvoker fireExceptionCaught(final Throwable p0);
    
    ChannelInboundInvoker fireUserEventTriggered(final Object p0);
    
    ChannelInboundInvoker fireChannelRead(final Object p0);
    
    ChannelInboundInvoker fireChannelReadComplete();
    
    ChannelInboundInvoker fireChannelWritabilityChanged();
}
