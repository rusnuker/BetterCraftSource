// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public interface ChannelFactory<T extends Channel> extends io.netty.bootstrap.ChannelFactory<T>
{
    T newChannel();
}
