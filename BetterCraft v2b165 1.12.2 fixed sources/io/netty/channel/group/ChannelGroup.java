// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.group;

import io.netty.channel.ChannelId;
import io.netty.channel.Channel;
import java.util.Set;

public interface ChannelGroup extends Set<Channel>, Comparable<ChannelGroup>
{
    String name();
    
    Channel find(final ChannelId p0);
    
    ChannelGroupFuture write(final Object p0);
    
    ChannelGroupFuture write(final Object p0, final ChannelMatcher p1);
    
    ChannelGroupFuture write(final Object p0, final ChannelMatcher p1, final boolean p2);
    
    ChannelGroup flush();
    
    ChannelGroup flush(final ChannelMatcher p0);
    
    ChannelGroupFuture writeAndFlush(final Object p0);
    
    @Deprecated
    ChannelGroupFuture flushAndWrite(final Object p0);
    
    ChannelGroupFuture writeAndFlush(final Object p0, final ChannelMatcher p1);
    
    ChannelGroupFuture writeAndFlush(final Object p0, final ChannelMatcher p1, final boolean p2);
    
    @Deprecated
    ChannelGroupFuture flushAndWrite(final Object p0, final ChannelMatcher p1);
    
    ChannelGroupFuture disconnect();
    
    ChannelGroupFuture disconnect(final ChannelMatcher p0);
    
    ChannelGroupFuture close();
    
    ChannelGroupFuture close(final ChannelMatcher p0);
    
    @Deprecated
    ChannelGroupFuture deregister();
    
    @Deprecated
    ChannelGroupFuture deregister(final ChannelMatcher p0);
    
    ChannelGroupFuture newCloseFuture();
    
    ChannelGroupFuture newCloseFuture(final ChannelMatcher p0);
}
