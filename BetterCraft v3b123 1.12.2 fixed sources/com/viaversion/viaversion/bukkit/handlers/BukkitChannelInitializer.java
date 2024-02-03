// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.handlers;

import io.netty.channel.ChannelPipeline;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.bukkit.platform.PaperViaInjector;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import java.lang.reflect.Method;
import com.viaversion.viaversion.platform.WrappedChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public final class BukkitChannelInitializer extends ChannelInitializer<Channel> implements WrappedChannelInitializer
{
    public static final String VIA_ENCODER = "via-encoder";
    public static final String VIA_DECODER = "via-decoder";
    public static final String MINECRAFT_ENCODER = "encoder";
    public static final String MINECRAFT_DECODER = "decoder";
    public static final String MINECRAFT_COMPRESSOR = "compress";
    public static final String MINECRAFT_DECOMPRESSOR = "decompress";
    public static final Object COMPRESSION_ENABLED_EVENT;
    private static final Method INIT_CHANNEL_METHOD;
    private final ChannelInitializer<Channel> original;
    
    private static Object paperCompressionEnabledEvent() {
        try {
            final Class<?> eventClass = Class.forName("io.papermc.paper.network.ConnectionEvent");
            return eventClass.getDeclaredField("COMPRESSION_THRESHOLD_SET").get(null);
        }
        catch (final ReflectiveOperationException e) {
            return null;
        }
    }
    
    public BukkitChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
    }
    
    @Deprecated
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
    
    @Override
    protected void initChannel(final Channel channel) throws Exception {
        BukkitChannelInitializer.INIT_CHANNEL_METHOD.invoke(this.original, channel);
        afterChannelInitialize(channel);
    }
    
    public static void afterChannelInitialize(final Channel channel) {
        final UserConnection connection = new UserConnectionImpl(channel);
        new ProtocolPipelineImpl(connection);
        if (PaperViaInjector.PAPER_PACKET_LIMITER) {
            connection.setPacketLimiterEnabled(false);
        }
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore("encoder", "via-encoder", new BukkitEncodeHandler(connection));
        pipeline.addBefore("decoder", "via-decoder", new BukkitDecodeHandler(connection));
    }
    
    @Override
    public ChannelInitializer<Channel> original() {
        return this.original;
    }
    
    static {
        COMPRESSION_ENABLED_EVENT = paperCompressionEnabledEvent();
        try {
            (INIT_CHANNEL_METHOD = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
