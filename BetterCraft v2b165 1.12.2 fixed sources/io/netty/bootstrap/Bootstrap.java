// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.bootstrap;

import io.netty.resolver.DefaultAddressResolverGroup;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.EmptyArrays;
import java.net.SocketException;
import com.mojang.patchy.BlockedServers;
import io.netty.channel.EventLoopGroup;
import java.util.Iterator;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;
import java.util.Map;
import io.netty.channel.ChannelHandler;
import io.netty.resolver.AddressResolver;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import io.netty.resolver.AddressResolverGroup;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.Channel;

public class Bootstrap extends AbstractBootstrap<Bootstrap, Channel>
{
    private static final InternalLogger logger;
    private static final AddressResolverGroup<?> DEFAULT_RESOLVER;
    private final BootstrapConfig config;
    private volatile AddressResolverGroup<SocketAddress> resolver;
    private volatile SocketAddress remoteAddress;
    
    public Bootstrap() {
        this.config = new BootstrapConfig(this);
        this.resolver = (AddressResolverGroup<SocketAddress>)Bootstrap.DEFAULT_RESOLVER;
    }
    
    private Bootstrap(final Bootstrap bootstrap) {
        super(bootstrap);
        this.config = new BootstrapConfig(this);
        this.resolver = (AddressResolverGroup<SocketAddress>)Bootstrap.DEFAULT_RESOLVER;
        this.resolver = bootstrap.resolver;
        this.remoteAddress = bootstrap.remoteAddress;
    }
    
    public Bootstrap resolver(final AddressResolverGroup<?> resolver) {
        this.resolver = (AddressResolverGroup<SocketAddress>)((resolver == null) ? Bootstrap.DEFAULT_RESOLVER : resolver);
        return this;
    }
    
    public Bootstrap remoteAddress(final SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }
    
    public Bootstrap remoteAddress(final String inetHost, final int inetPort) {
        this.remoteAddress = InetSocketAddress.createUnresolved(inetHost, inetPort);
        return this;
    }
    
    public Bootstrap remoteAddress(final InetAddress inetHost, final int inetPort) {
        this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }
    
    public ChannelFuture connect() {
        this.validate();
        final SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            throw new IllegalStateException("remoteAddress not set");
        }
        return this.doResolveAndConnect(remoteAddress, this.config.localAddress());
    }
    
    public ChannelFuture connect(final String inetHost, final int inetPort) {
        return this.connect(InetSocketAddress.createUnresolved(inetHost, inetPort));
    }
    
    public ChannelFuture connect(final InetAddress inetHost, final int inetPort) {
        return this.connect(new InetSocketAddress(inetHost, inetPort));
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doResolveAndConnect(remoteAddress, this.config.localAddress());
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doResolveAndConnect(remoteAddress, localAddress);
    }
    
    private ChannelFuture doResolveAndConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        final ChannelFuture future = this.checkAddress(remoteAddress);
        if (future != null) {
            return future;
        }
        final ChannelFuture regFuture = this.initAndRegister();
        final Channel channel = regFuture.channel();
        if (!regFuture.isDone()) {
            final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
            regFuture.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    final Throwable cause = future.cause();
                    if (cause != null) {
                        promise.setFailure(cause);
                    }
                    else {
                        promise.registered();
                        Bootstrap.this.doResolveAndConnect0(channel, remoteAddress, localAddress, promise);
                    }
                }
            });
            return promise;
        }
        if (!regFuture.isSuccess()) {
            return regFuture;
        }
        return this.doResolveAndConnect0(channel, remoteAddress, localAddress, channel.newPromise());
    }
    
    private ChannelFuture doResolveAndConnect0(final Channel channel, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        try {
            final EventLoop eventLoop = channel.eventLoop();
            final AddressResolver<SocketAddress> resolver = this.resolver.getResolver(eventLoop);
            if (!resolver.isSupported(remoteAddress) || resolver.isResolved(remoteAddress)) {
                doConnect(remoteAddress, localAddress, promise);
                return promise;
            }
            final Future<SocketAddress> resolveFuture = resolver.resolve(remoteAddress);
            if (resolveFuture.isDone()) {
                final Throwable resolveFailureCause = resolveFuture.cause();
                if (resolveFailureCause != null) {
                    channel.close();
                    promise.setFailure(resolveFailureCause);
                }
                else {
                    doConnect(resolveFuture.getNow(), localAddress, promise);
                }
                return promise;
            }
            resolveFuture.addListener(new FutureListener<SocketAddress>() {
                @Override
                public void operationComplete(final Future<SocketAddress> future) throws Exception {
                    if (future.cause() != null) {
                        channel.close();
                        promise.setFailure(future.cause());
                    }
                    else {
                        doConnect(future.getNow(), localAddress, promise);
                    }
                }
            });
        }
        catch (final Throwable cause) {
            promise.tryFailure(cause);
        }
        return promise;
    }
    
    private static void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise connectPromise) {
        final Channel channel = connectPromise.channel();
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                if (localAddress == null) {
                    channel.connect(remoteAddress, connectPromise);
                }
                else {
                    channel.connect(remoteAddress, localAddress, connectPromise);
                }
                connectPromise.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        });
    }
    
    @Override
    void init(final Channel channel) throws Exception {
        final ChannelPipeline p = channel.pipeline();
        p.addLast(this.config.handler());
        final Map<ChannelOption<?>, Object> options = this.options0();
        synchronized (options) {
            AbstractBootstrap.setChannelOptions(channel, options, Bootstrap.logger);
        }
        final Map<AttributeKey<?>, Object> attrs = this.attrs0();
        synchronized (attrs) {
            for (final Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
                channel.attr(e.getKey()).set(e.getValue());
            }
        }
    }
    
    @Override
    public Bootstrap validate() {
        super.validate();
        if (this.config.handler() == null) {
            throw new IllegalStateException("handler not set");
        }
        return this;
    }
    
    @Override
    public Bootstrap clone() {
        return new Bootstrap(this);
    }
    
    public Bootstrap clone(final EventLoopGroup group) {
        final Bootstrap bs = new Bootstrap(this);
        bs.group = group;
        return bs;
    }
    
    @Override
    public final BootstrapConfig config() {
        return this.config;
    }
    
    final SocketAddress remoteAddress() {
        return this.remoteAddress;
    }
    
    final AddressResolverGroup<?> resolver() {
        return this.resolver;
    }
    
    ChannelFuture checkAddress(final SocketAddress remoteAddress) {
        if (remoteAddress instanceof InetSocketAddress) {
            final InetSocketAddress inetSocketAddress = (InetSocketAddress)remoteAddress;
            final InetAddress address = inetSocketAddress.getAddress();
            boolean isBlocked;
            if (address == null) {
                isBlocked = BlockedServers.isBlockedServer(inetSocketAddress.getHostName());
            }
            else {
                isBlocked = (BlockedServers.isBlockedServer(address.getHostAddress()) || BlockedServers.isBlockedServer(address.getHostName()));
            }
            if (isBlocked) {
                final Channel channel = (Channel)this.channelFactory().newChannel();
                channel.unsafe().closeForcibly();
                final SocketException cause = new SocketException("Network is unreachable");
                cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
                return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(cause);
            }
        }
        return null;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Bootstrap.class);
        DEFAULT_RESOLVER = DefaultAddressResolverGroup.INSTANCE;
    }
}
