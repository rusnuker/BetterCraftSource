// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.util.concurrent.EventExecutor;
import io.netty.resolver.RoundRobinInetAddressResolver;
import java.net.InetSocketAddress;
import io.netty.resolver.AddressResolver;
import java.net.InetAddress;
import io.netty.resolver.NameResolver;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelFactory;
import io.netty.channel.socket.DatagramChannel;

public class RoundRobinDnsAddressResolverGroup extends DnsAddressResolverGroup
{
    public RoundRobinDnsAddressResolverGroup(final Class<? extends DatagramChannel> channelType, final DnsServerAddresses nameServerAddresses) {
        super(channelType, nameServerAddresses);
    }
    
    public RoundRobinDnsAddressResolverGroup(final ChannelFactory<? extends DatagramChannel> channelFactory, final DnsServerAddresses nameServerAddresses) {
        super(channelFactory, nameServerAddresses);
    }
    
    @Override
    protected final AddressResolver<InetSocketAddress> newAddressResolver(final EventLoop eventLoop, final NameResolver<InetAddress> resolver) throws Exception {
        return new RoundRobinInetAddressResolver(eventLoop, resolver).asAddressResolver();
    }
}
