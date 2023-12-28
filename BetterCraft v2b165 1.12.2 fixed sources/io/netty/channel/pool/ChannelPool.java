// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.pool;

import io.netty.util.concurrent.Promise;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.io.Closeable;

public interface ChannelPool extends Closeable
{
    Future<Channel> acquire();
    
    Future<Channel> acquire(final Promise<Channel> p0);
    
    Future<Void> release(final Channel p0);
    
    Future<Void> release(final Channel p0, final Promise<Void> p1);
    
    void close();
}
