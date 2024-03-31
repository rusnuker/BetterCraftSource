// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface DomainSocketChannelConfig extends ChannelConfig
{
    @Deprecated
    DomainSocketChannelConfig setMaxMessagesPerRead(final int p0);
    
    DomainSocketChannelConfig setConnectTimeoutMillis(final int p0);
    
    DomainSocketChannelConfig setWriteSpinCount(final int p0);
    
    DomainSocketChannelConfig setAllocator(final ByteBufAllocator p0);
    
    DomainSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator p0);
    
    DomainSocketChannelConfig setAutoRead(final boolean p0);
    
    DomainSocketChannelConfig setAutoClose(final boolean p0);
    
    @Deprecated
    DomainSocketChannelConfig setWriteBufferHighWaterMark(final int p0);
    
    @Deprecated
    DomainSocketChannelConfig setWriteBufferLowWaterMark(final int p0);
    
    DomainSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark p0);
    
    DomainSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator p0);
    
    DomainSocketChannelConfig setReadMode(final DomainSocketReadMode p0);
    
    DomainSocketReadMode getReadMode();
}
