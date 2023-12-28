// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.pool;

import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.channel.Channel;

public interface ChannelHealthChecker
{
    public static final ChannelHealthChecker ACTIVE = new ChannelHealthChecker() {
        @Override
        public Future<Boolean> isHealthy(final Channel channel) {
            final EventLoop loop = channel.eventLoop();
            return channel.isActive() ? loop.newSucceededFuture(Boolean.TRUE) : loop.newSucceededFuture(Boolean.FALSE);
        }
    };
    
    Future<Boolean> isHealthy(final Channel p0);
}
