// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.AttributeMap;

public interface ChannelHandlerContext extends AttributeMap, ChannelInboundInvoker, ChannelOutboundInvoker
{
    Channel channel();
    
    EventExecutor executor();
    
    String name();
    
    ChannelHandler handler();
    
    boolean isRemoved();
    
    ChannelHandlerContext fireChannelRegistered();
    
    ChannelHandlerContext fireChannelUnregistered();
    
    ChannelHandlerContext fireChannelActive();
    
    ChannelHandlerContext fireChannelInactive();
    
    ChannelHandlerContext fireExceptionCaught(final Throwable p0);
    
    ChannelHandlerContext fireUserEventTriggered(final Object p0);
    
    ChannelHandlerContext fireChannelRead(final Object p0);
    
    ChannelHandlerContext fireChannelReadComplete();
    
    ChannelHandlerContext fireChannelWritabilityChanged();
    
    ChannelHandlerContext read();
    
    ChannelHandlerContext flush();
    
    ChannelPipeline pipeline();
    
    ByteBufAllocator alloc();
    
    @Deprecated
     <T> Attribute<T> attr(final AttributeKey<T> p0);
    
    @Deprecated
     <T> boolean hasAttr(final AttributeKey<T> p0);
}
