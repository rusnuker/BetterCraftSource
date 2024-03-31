// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.resolver.InetSocketAddressResolver;
import io.netty.resolver.NameResolver;
import io.netty.util.internal.StringUtil;
import io.netty.channel.EventLoop;
import io.netty.resolver.AddressResolver;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ReflectiveChannelFactory;
import java.util.List;
import java.net.InetAddress;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.ConcurrentMap;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.ChannelFactory;
import java.net.InetSocketAddress;
import io.netty.resolver.AddressResolverGroup;

public class DnsAddressResolverGroup extends AddressResolverGroup<InetSocketAddress>
{
    private final ChannelFactory<? extends DatagramChannel> channelFactory;
    private final DnsServerAddresses nameServerAddresses;
    private final ConcurrentMap<String, Promise<InetAddress>> resolvesInProgress;
    private final ConcurrentMap<String, Promise<List<InetAddress>>> resolveAllsInProgress;
    
    public DnsAddressResolverGroup(final Class<? extends DatagramChannel> channelType, final DnsServerAddresses nameServerAddresses) {
        this(new ReflectiveChannelFactory<DatagramChannel>(channelType), nameServerAddresses);
    }
    
    public DnsAddressResolverGroup(final ChannelFactory<? extends DatagramChannel> channelFactory, final DnsServerAddresses nameServerAddresses) {
        this.resolvesInProgress = PlatformDependent.newConcurrentHashMap();
        this.resolveAllsInProgress = PlatformDependent.newConcurrentHashMap();
        this.channelFactory = channelFactory;
        this.nameServerAddresses = nameServerAddresses;
    }
    
    @Override
    protected final AddressResolver<InetSocketAddress> newResolver(final EventExecutor executor) throws Exception {
        if (!(executor instanceof EventLoop)) {
            throw new IllegalStateException("unsupported executor type: " + StringUtil.simpleClassName(executor) + " (expected: " + StringUtil.simpleClassName(EventLoop.class));
        }
        return this.newResolver((EventLoop)executor, this.channelFactory, this.nameServerAddresses);
    }
    
    @Deprecated
    protected AddressResolver<InetSocketAddress> newResolver(final EventLoop eventLoop, final ChannelFactory<? extends DatagramChannel> channelFactory, final DnsServerAddresses nameServerAddresses) throws Exception {
        final NameResolver<InetAddress> resolver = new InflightNameResolver<InetAddress>(eventLoop, this.newNameResolver(eventLoop, channelFactory, nameServerAddresses), this.resolvesInProgress, this.resolveAllsInProgress);
        return this.newAddressResolver(eventLoop, resolver);
    }
    
    protected NameResolver<InetAddress> newNameResolver(final EventLoop eventLoop, final ChannelFactory<? extends DatagramChannel> channelFactory, final DnsServerAddresses nameServerAddresses) throws Exception {
        return new DnsNameResolverBuilder(eventLoop).channelFactory(channelFactory).nameServerAddresses(nameServerAddresses).build();
    }
    
    protected AddressResolver<InetSocketAddress> newAddressResolver(final EventLoop eventLoop, final NameResolver<InetAddress> resolver) throws Exception {
        return new InetSocketAddressResolver(eventLoop, resolver);
    }
}
