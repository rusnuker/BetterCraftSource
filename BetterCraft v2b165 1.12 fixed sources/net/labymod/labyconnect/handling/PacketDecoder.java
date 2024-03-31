// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.Packet;
import java.io.IOException;
import net.labymod.labyconnect.packets.PacketBuf;
import net.labymod.labyconnect.packets.Protocol;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.labymod.main.LabyMod;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder
{
    private LabyMod labyMod;
    
    public PacketDecoder(final LabyMod labyMod) {
        this.labyMod = labyMod;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> objects) throws Exception {
        final PacketBufOld packetbuf = new PacketBufOld(byteBuf);
        if (packetbuf.readableBytes() >= 1) {
            final int i = packetbuf.readVarIntFromBuffer();
            final Packet packet = Protocol.getProtocol().getPacket(i);
            if (i == 62 || i == 63) {
                this.labyMod.getLabyConnect().getClientConnection().getCustomIp();
            }
            packet.read(packetbuf);
            if (packetbuf.readableBytes() > 0) {
                throw new IOException("Packet  (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetbuf.readableBytes() + " bytes extra whilst reading packet " + packet);
            }
            objects.add(packet);
        }
    }
}
