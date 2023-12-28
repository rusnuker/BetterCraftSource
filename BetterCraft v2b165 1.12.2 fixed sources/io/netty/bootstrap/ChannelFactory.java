// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.bootstrap;

import io.netty.channel.Channel;

@Deprecated
public interface ChannelFactory<T extends Channel>
{
    T newChannel();
}
