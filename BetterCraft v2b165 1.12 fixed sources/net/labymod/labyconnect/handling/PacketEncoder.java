// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.PacketBuf;
import net.labymod.labyconnect.packets.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.packets.Packet;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet>
{
    private LabyMod labyMod;
    
    public PacketEncoder(final LabyMod labyMod) {
        this.labyMod = labyMod;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext channelHandlerContext, final Packet packet, final ByteBuf byteBuf) throws Exception {
        final PacketBufOld packetbuf = new PacketBufOld(byteBuf);
        final int i = Protocol.getProtocol().getPacketId(packet);
        if (i == 62 || i == 63) {
            this.labyMod.getLabyConnect().getClientConnection().getCustomIp();
        }
        packetbuf.writeVarIntToBuffer(Protocol.getProtocol().getPacketId(packet));
        packet.write(packetbuf);
    }
}
