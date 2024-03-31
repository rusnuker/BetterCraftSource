// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import io.netty.channel.socket.DuplexChannel;

public interface DomainSocketChannel extends UnixChannel, DuplexChannel
{
    DomainSocketAddress remoteAddress();
    
    DomainSocketAddress localAddress();
    
    DomainSocketChannelConfig config();
}
