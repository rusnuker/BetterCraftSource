// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.pool;

import io.netty.channel.Channel;

public abstract class AbstractChannelPoolHandler implements ChannelPoolHandler
{
    @Override
    public void channelAcquired(final Channel ch) throws Exception {
    }
    
    @Override
    public void channelReleased(final Channel ch) throws Exception {
    }
}
