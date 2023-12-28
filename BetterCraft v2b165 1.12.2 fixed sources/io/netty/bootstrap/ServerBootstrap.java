// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.bootstrap;

import io.netty.channel.ChannelConfig;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.Channel;
import java.util.LinkedHashMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ServerChannel;

public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap, ServerChannel>
{
    private static final InternalLogger logger;
    private final Map<ChannelOption<?>, Object> childOptions;
    private final Map<AttributeKey<?>, Object> childAttrs;
    private final ServerBootstrapConfig config;
    private volatile EventLoopGroup childGroup;
    private volatile ChannelHandler childHandler;
    
    public ServerBootstrap() {
        this.childOptions = new LinkedHashMap<ChannelOption<?>, Object>();
        this.childAttrs = new LinkedHashMap<AttributeKey<?>, Object>();
        this.config = new ServerBootstrapConfig(this);
    }
    
    private ServerBootstrap(final ServerBootstrap bootstrap) {
        super(bootstrap);
        this.childOptions = new LinkedHashMap<ChannelOption<?>, Object>();
        this.childAttrs = new LinkedHashMap<AttributeKey<?>, Object>();
        this.config = new ServerBootstrapConfig(this);
        this.childGroup = bootstrap.childGroup;
        this.childHandler = bootstrap.childHandler;
        synchronized (bootstrap.childOptions) {
            this.childOptions.putAll(bootstrap.childOptions);
        }
        synchronized (bootstrap.childAttrs) {
            this.childAttrs.putAll(bootstrap.childAttrs);
        }
    }
    
    @Override
    public ServerBootstrap group(final EventLoopGroup group) {
        return this.group(group, group);
    }
    
    public ServerBootstrap group(final EventLoopGroup parentGroup, final EventLoopGroup childGroup) {
        super.group(parentGroup);
        if (childGroup == null) {
            throw new NullPointerException("childGroup");
        }
        if (this.childGroup != null) {
            throw new IllegalStateException("childGroup set already");
        }
        this.childGroup = childGroup;
        return this;
    }
    
    public <T> ServerBootstrap childOption(final ChannelOption<T> childOption, final T value) {
        if (childOption == null) {
            throw new NullPointerException("childOption");
        }
        if (value == null) {
            synchronized (this.childOptions) {
                this.childOptions.remove(childOption);
            }
        }
        else {
            synchronized (this.childOptions) {
                this.childOptions.put(childOption, value);
            }
        }
        return this;
    }
    
    public <T> ServerBootstrap childAttr(final AttributeKey<T> childKey, final T value) {
        if (childKey == null) {
            throw new NullPointerException("childKey");
        }
        if (value == null) {
            this.childAttrs.remove(childKey);
        }
        else {
            this.childAttrs.put(childKey, value);
        }
        return this;
    }
    
    public ServerBootstrap childHandler(final ChannelHandler childHandler) {
        if (childHandler == null) {
            throw new NullPointerException("childHandler");
        }
        this.childHandler = childHandler;
        return this;
    }
    
    @Override
    void init(final Channel channel) throws Exception {
        final Map<ChannelOption<?>, Object> options = this.options0();
        synchronized (options) {
            AbstractBootstrap.setChannelOptions(channel, options, ServerBootstrap.logger);
        }
        final Map<AttributeKey<?>, Object> attrs = this.attrs0();
        synchronized (attrs) {
            for (final Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
                final AttributeKey<Object> key = e.getKey();
                channel.attr(key).set(e.getValue());
            }
        }
        final ChannelPipeline p = channel.pipeline();
        final EventLoopGroup currentChildGroup = this.childGroup;
        final ChannelHandler currentChildHandler = this.childHandler;
        final Map.Entry<ChannelOption<?>, Object>[] currentChildOptions;
        synchronized (this.childOptions) {
            currentChildOptions = this.childOptions.entrySet().toArray(newOptionArray(this.childOptions.size()));
        }
        final Map.Entry<AttributeKey<?>, Object>[] currentChildAttrs;
        synchronized (this.childAttrs) {
            currentChildAttrs = this.childAttrs.entrySet().toArray(newAttrArray(this.childAttrs.size()));
        }
        p.addLast(new ChannelInitializer<Channel>() {
            public void initChannel(final Channel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                final ChannelHandler handler = ServerBootstrap.this.config.handler();
                if (handler != null) {
                    pipeline.addLast(handler);
                }
                ch.eventLoop().execute(new Runnable() {
                    @Override
                    public void run() {
                        pipeline.addLast(new ServerBootstrapAcceptor(ch, currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs));
                    }
                });
            }
        });
    }
    
    @Override
    public ServerBootstrap validate() {
        super.validate();
        if (this.childHandler == null) {
            throw new IllegalStateException("childHandler not set");
        }
        if (this.childGroup == null) {
            ServerBootstrap.logger.warn("childGroup is not set. Using parentGroup instead.");
            this.childGroup = this.config.group();
        }
        return this;
    }
    
    private static Map.Entry<AttributeKey<?>, Object>[] newAttrArray(final int size) {
        return new Map.Entry[size];
    }
    
    private static Map.Entry<ChannelOption<?>, Object>[] newOptionArray(final int size) {
        return new Map.Entry[size];
    }
    
    @Override
    public ServerBootstrap clone() {
        return new ServerBootstrap(this);
    }
    
    @Deprecated
    public EventLoopGroup childGroup() {
        return this.childGroup;
    }
    
    final ChannelHandler childHandler() {
        return this.childHandler;
    }
    
    final Map<ChannelOption<?>, Object> childOptions() {
        return AbstractBootstrap.copiedMap(this.childOptions);
    }
    
    final Map<AttributeKey<?>, Object> childAttrs() {
        return AbstractBootstrap.copiedMap(this.childAttrs);
    }
    
    @Override
    public final ServerBootstrapConfig config() {
        return this.config;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);
    }
    
    private static class ServerBootstrapAcceptor extends ChannelInboundHandlerAdapter
    {
        private final EventLoopGroup childGroup;
        private final ChannelHandler childHandler;
        private final Map.Entry<ChannelOption<?>, Object>[] childOptions;
        private final Map.Entry<AttributeKey<?>, Object>[] childAttrs;
        private final Runnable enableAutoReadTask;
        
        ServerBootstrapAcceptor(final Channel channel, final EventLoopGroup childGroup, final ChannelHandler childHandler, final Map.Entry<ChannelOption<?>, Object>[] childOptions, final Map.Entry<AttributeKey<?>, Object>[] childAttrs) {
            this.childGroup = childGroup;
            this.childHandler = childHandler;
            this.childOptions = childOptions;
            this.childAttrs = childAttrs;
            this.enableAutoReadTask = new Runnable() {
                @Override
                public void run() {
                    channel.config().setAutoRead(true);
                }
            };
        }
        
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
            final Channel child = (Channel)msg;
            child.pipeline().addLast(this.childHandler);
            AbstractBootstrap.setChannelOptions(child, this.childOptions, ServerBootstrap.logger);
            for (final Map.Entry<AttributeKey<?>, Object> e : this.childAttrs) {
                child.attr(e.getKey()).set(e.getValue());
            }
            try {
                this.childGroup.register(child).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            forceClose(child, future.cause());
                        }
                    }
                });
            }
            catch (final Throwable t) {
                forceClose(child, t);
            }
        }
        
        private static void forceClose(final Channel child, final Throwable t) {
            child.unsafe().closeForcibly();
            ServerBootstrap.logger.warn("Failed to register an accepted channel: " + child, t);
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            final ChannelConfig config = ctx.channel().config();
            if (config.isAutoRead()) {
                config.setAutoRead(false);
                ctx.channel().eventLoop().schedule(this.enableAutoReadTask, 1L, TimeUnit.SECONDS);
            }
            ctx.fireExceptionCaught(cause);
        }
    }
}
