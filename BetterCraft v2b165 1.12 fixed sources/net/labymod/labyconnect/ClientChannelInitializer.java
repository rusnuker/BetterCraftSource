// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect;

import io.netty.channel.Channel;
import net.labymod.labyconnect.handling.PacketEncoder;
import net.labymod.labyconnect.handling.PacketSplitter;
import net.labymod.labyconnect.handling.PacketDecoder;
import net.labymod.labyconnect.handling.PacketPrepender;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;
import net.labymod.main.LabyMod;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelInitializer;

public class ClientChannelInitializer extends ChannelInitializer<NioSocketChannel>
{
    private ClientConnection clientConnection;
    private LabyMod labyMod;
    
    public ClientChannelInitializer(final LabyMod labyMod, final ClientConnection clientConnection) {
        this.labyMod = labyMod;
        this.clientConnection = clientConnection;
    }
    
    @Override
    protected void initChannel(final NioSocketChannel channel) throws Exception {
        this.clientConnection.setNioSocketChannel(channel);
        channel.pipeline().addLast("timeout", new ReadTimeoutHandler(5000L, TimeUnit.DAYS)).addLast("splitter", new PacketPrepender()).addLast("decoder", new PacketDecoder(this.labyMod)).addLast("prepender", new PacketSplitter()).addLast("encoder", new PacketEncoder(this.labyMod)).addLast(this.getClientConnection());
    }
    
    public ClientConnection getClientConnection() {
        return this.clientConnection;
    }
}
