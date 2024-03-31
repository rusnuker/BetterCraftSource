// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.handlers;

import com.viaversion.viaversion.classgenerator.generated.HandlerConstructor;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import com.viaversion.viaversion.bukkit.classgenerator.ClassGenerator;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class BukkitChannelInitializer extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<Channel> original;
    private Method method;
    
    public BukkitChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            (this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
    
    @Override
    protected void initChannel(final Channel channel) throws Exception {
        this.method.invoke(this.original, channel);
        afterChannelInitialize(channel);
    }
    
    public static void afterChannelInitialize(final Channel channel) {
        final UserConnection connection = new UserConnectionImpl(channel);
        new ProtocolPipelineImpl(connection);
        final HandlerConstructor constructor = ClassGenerator.getConstructor();
        final MessageToByteEncoder encoder = constructor.newEncodeHandler(connection, (MessageToByteEncoder)channel.pipeline().get("encoder"));
        final ByteToMessageDecoder decoder = constructor.newDecodeHandler(connection, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
        channel.pipeline().replace("encoder", "encoder", encoder);
        channel.pipeline().replace("decoder", "decoder", decoder);
    }
}
