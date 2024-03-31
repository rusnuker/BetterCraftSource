// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.handlers;

import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import io.netty.channel.socket.SocketChannel;
import com.viaversion.viaversion.api.Via;
import java.lang.reflect.Method;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class SpongeChannelInitializer extends ChannelInitializer<Channel>
{
    private final ChannelInitializer<Channel> original;
    private Method method;
    
    public SpongeChannelInitializer(final ChannelInitializer<Channel> oldInit) {
        this.original = oldInit;
        try {
            (this.method = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class)).setAccessible(true);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void initChannel(final Channel channel) throws Exception {
        if (Via.getAPI().getServerVersion().isKnown() && channel instanceof SocketChannel) {
            final UserConnection info = new UserConnectionImpl(channel);
            new ProtocolPipelineImpl(info);
            this.method.invoke(this.original, channel);
            final MessageToByteEncoder encoder = new SpongeEncodeHandler(info, (MessageToByteEncoder<?>)channel.pipeline().get("encoder"));
            final ByteToMessageDecoder decoder = new SpongeDecodeHandler(info, (ByteToMessageDecoder)channel.pipeline().get("decoder"));
            channel.pipeline().replace("encoder", "encoder", encoder);
            channel.pipeline().replace("decoder", "decoder", decoder);
        }
        else {
            this.method.invoke(this.original, channel);
        }
    }
    
    public ChannelInitializer<Channel> getOriginal() {
        return this.original;
    }
}
