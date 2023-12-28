// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network;

import java.io.IOException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>>
{
    private static final Logger LOGGER;
    private static final Marker RECEIVED_PACKET_MARKER;
    private final EnumPacketDirection direction;
    
    static {
        LOGGER = LogManager.getLogger();
        RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.NETWORK_PACKETS_MARKER);
    }
    
    public NettyPacketEncoder(final EnumPacketDirection direction) {
        this.direction = direction;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext p_encode_1_, final Packet<?> p_encode_2_, final ByteBuf p_encode_3_) throws IOException, Exception {
        final EnumConnectionState enumconnectionstate = p_encode_1_.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get();
        if (enumconnectionstate == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + p_encode_2_.toString());
        }
        final Integer integer = enumconnectionstate.getPacketId(this.direction, p_encode_2_);
        if (NettyPacketEncoder.LOGGER.isDebugEnabled()) {
            NettyPacketEncoder.LOGGER.debug(NettyPacketEncoder.RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", p_encode_1_.channel().attr(NetworkManager.PROTOCOL_ATTRIBUTE_KEY).get(), integer, p_encode_2_.getClass().getName());
        }
        if (integer == null) {
            throw new IOException("Can't serialize unregistered packet");
        }
        final PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
        packetbuffer.writeVarIntToBuffer(integer);
        try {
            p_encode_2_.writePacketData(packetbuffer);
        }
        catch (final Throwable throwable) {
            NettyPacketEncoder.LOGGER.error(throwable);
        }
    }
}
