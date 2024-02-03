// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.PacketBuf;
import net.labymod.support.util.Debug;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.packets.Protocol;
import net.labymod.core.LabyModCore;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.labymod.labyconnect.packets.Packet;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet>
{
    @Override
    protected void encode(final ChannelHandlerContext channelHandlerContext, final Packet packet, final ByteBuf byteBuf) throws Exception {
        final PacketBuf packetBuffer = LabyModCore.getMinecraft().createPacketBuf(byteBuf);
        final int id = Protocol.getProtocol().getPacketId(packet);
        if ((id != 62 && id != 63) || LabyMod.getInstance().getLabyConnect().getClientConnection().getCustomIp() != null) {
            Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "[OUT] " + id + " " + packet.getClass().getSimpleName());
        }
        packetBuffer.writeVarIntToBuffer(Protocol.getProtocol().getPacketId(packet));
        packet.write(packetBuffer);
    }
}
