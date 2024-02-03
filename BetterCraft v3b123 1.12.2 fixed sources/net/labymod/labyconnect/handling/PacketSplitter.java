// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.core.LabyModCore;
import net.labymod.labyconnect.packets.PacketBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketSplitter extends MessageToByteEncoder<ByteBuf>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf buffer, final ByteBuf byteBuf) {
        final int var4 = buffer.readableBytes();
        final int var5 = PacketBuf.getVarIntSize(var4);
        if (var5 > 3) {
            throw new IllegalArgumentException("unable to fit " + var4 + " into " + 3);
        }
        final PacketBuf packetBuffer = LabyModCore.getMinecraft().createPacketBuf(byteBuf);
        packetBuffer.ensureWritable(var5 + var4);
        packetBuffer.writeVarIntToBuffer(var4);
        packetBuffer.writeBytes(buffer, buffer.readerIndex(), var4);
    }
}
