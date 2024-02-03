// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketBuf;
import java.io.IOException;
import net.labymod.support.util.Debug;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.packets.Protocol;
import net.labymod.core.LabyModCore;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> objects) throws Exception {
        final PacketBuf packetBuffer = LabyModCore.getMinecraft().createPacketBuf(byteBuf);
        if (packetBuffer.readableBytes() < 1) {
            return;
        }
        final int id = packetBuffer.readVarIntFromBuffer();
        final Packet packet = Protocol.getProtocol().getPacket(id);
        if ((id != 62 && id != 63) || LabyMod.getInstance().getLabyConnect().getClientConnection().getCustomIp() != null) {
            Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "[IN] " + id + " " + packet.getClass().getSimpleName());
        }
        packet.read(packetBuffer);
        if (packetBuffer.readableBytes() > 0) {
            throw new IOException("Packet  (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetBuffer.readableBytes() + " bytes extra whilst reading packet " + packet);
        }
        objects.add(packet);
    }
}
