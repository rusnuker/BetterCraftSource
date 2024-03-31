// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.socket;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;

public interface DuplexChannel extends Channel
{
    boolean isInputShutdown();
    
    ChannelFuture shutdownInput();
    
    ChannelFuture shutdownInput(final ChannelPromise p0);
    
    boolean isOutputShutdown();
    
    ChannelFuture shutdownOutput();
    
    ChannelFuture shutdownOutput(final ChannelPromise p0);
    
    boolean isShutdown();
    
    ChannelFuture shutdown();
    
    ChannelFuture shutdown(final ChannelPromise p0);
}
