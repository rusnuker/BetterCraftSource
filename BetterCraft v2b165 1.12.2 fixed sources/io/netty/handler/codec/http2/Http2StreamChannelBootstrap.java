// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.lang.annotation.Annotation;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.LinkedHashMap;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelHandler;

public class Http2StreamChannelBootstrap
{
    private volatile ParentChannelAndMultiplexCodec channelAndCodec;
    private volatile ChannelHandler handler;
    private volatile EventLoopGroup group;
    private final Map<ChannelOption<?>, Object> options;
    private final Map<AttributeKey<?>, Object> attributes;
    
    public Http2StreamChannelBootstrap() {
        this.options = Collections.synchronizedMap(new LinkedHashMap<ChannelOption<?>, Object>());
        this.attributes = Collections.synchronizedMap(new LinkedHashMap<AttributeKey<?>, Object>());
    }
    
    Http2StreamChannelBootstrap(final Http2StreamChannelBootstrap bootstrap0) {
        ObjectUtil.checkNotNull(bootstrap0, "bootstrap must not be null");
        this.channelAndCodec = bootstrap0.channelAndCodec;
        this.handler = bootstrap0.handler;
        this.group = bootstrap0.group;
        this.options = Collections.synchronizedMap(new LinkedHashMap<ChannelOption<?>, Object>(bootstrap0.options));
        this.attributes = Collections.synchronizedMap(new LinkedHashMap<AttributeKey<?>, Object>(bootstrap0.attributes));
    }
    
    public ChannelFuture connect() {
        return this.connect(-1);
    }
    
    ChannelFuture connect(final int streamId) {
        this.validateState();
        final ParentChannelAndMultiplexCodec channelAndCodec0 = this.channelAndCodec;
        final Channel parentChannel = channelAndCodec0.parentChannel;
        final Http2MultiplexCodec multiplexCodec = channelAndCodec0.multiplexCodec;
        EventLoopGroup group0 = this.group;
        group0 = ((group0 == null) ? parentChannel.eventLoop() : group0);
        return multiplexCodec.createStreamChannel(parentChannel, group0, this.handler, this.options, this.attributes, streamId);
    }
    
    public Http2StreamChannelBootstrap parentChannel(final Channel parent) {
        this.channelAndCodec = new ParentChannelAndMultiplexCodec(parent);
        return this;
    }
    
    public Http2StreamChannelBootstrap handler(final ChannelHandler handler) {
        this.handler = checkSharable(ObjectUtil.checkNotNull(handler, "handler"));
        return this;
    }
    
    public Http2StreamChannelBootstrap group(final EventLoopGroup group) {
        this.group = group;
        return this;
    }
    
    public <T> Http2StreamChannelBootstrap option(final ChannelOption<T> option, final T value) {
        ObjectUtil.checkNotNull(option, "option must not be null");
        if (value == null) {
            this.options.remove(option);
        }
        else {
            this.options.put(option, value);
        }
        return this;
    }
    
    public <T> Http2StreamChannelBootstrap attr(final AttributeKey<T> key, final T value) {
        ObjectUtil.checkNotNull(key, "key must not be null");
        if (value == null) {
            this.attributes.remove(key);
        }
        else {
            this.attributes.put(key, value);
        }
        return this;
    }
    
    public Channel parentChannel() {
        final ParentChannelAndMultiplexCodec channelAndCodec0 = this.channelAndCodec;
        if (channelAndCodec0 != null) {
            return channelAndCodec0.parentChannel;
        }
        return null;
    }
    
    public ChannelHandler handler() {
        return this.handler;
    }
    
    public EventLoopGroup group() {
        return this.group;
    }
    
    public Map<ChannelOption<?>, Object> options() {
        return Collections.unmodifiableMap((Map<? extends ChannelOption<?>, ?>)new LinkedHashMap<ChannelOption<?>, Object>(this.options));
    }
    
    public Map<AttributeKey<?>, Object> attributes() {
        return Collections.unmodifiableMap((Map<? extends AttributeKey<?>, ?>)new LinkedHashMap<AttributeKey<?>, Object>(this.attributes));
    }
    
    private void validateState() {
        ObjectUtil.checkNotNull(this.handler, "handler must be set");
        ObjectUtil.checkNotNull(this.channelAndCodec, "parent channel must be set");
    }
    
    private static ChannelHandler checkSharable(final ChannelHandler handler) {
        if (!handler.getClass().isAnnotationPresent(ChannelHandler.Sharable.class)) {
            throw new IllegalArgumentException("The handler must be Sharable");
        }
        return handler;
    }
    
    private static class ParentChannelAndMultiplexCodec
    {
        final Channel parentChannel;
        final Http2MultiplexCodec multiplexCodec;
        
        ParentChannelAndMultiplexCodec(final Channel parentChannel) {
            this.parentChannel = checkRegistered(ObjectUtil.checkNotNull(parentChannel, "parentChannel"));
            this.multiplexCodec = requireMultiplexCodec(parentChannel.pipeline());
        }
        
        private static Http2MultiplexCodec requireMultiplexCodec(final ChannelPipeline pipeline) {
            final ChannelHandlerContext ctx = pipeline.context(Http2MultiplexCodec.class);
            if (ctx == null) {
                throw new IllegalArgumentException(Http2MultiplexCodec.class.getSimpleName() + " was not found in the channel pipeline.");
            }
            return (Http2MultiplexCodec)ctx.handler();
        }
        
        private static Channel checkRegistered(final Channel channel) {
            if (!channel.isRegistered()) {
                throw new IllegalArgumentException("The channel must be registered to an eventloop.");
            }
            return channel;
        }
    }
}
