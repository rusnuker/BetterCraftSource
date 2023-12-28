// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.pool;

import io.netty.channel.Channel;

public interface ChannelPoolHandler
{
    void channelReleased(final Channel p0) throws Exception;
    
    void channelAcquired(final Channel p0) throws Exception;
    
    void channelCreated(final Channel p0) throws Exception;
}
