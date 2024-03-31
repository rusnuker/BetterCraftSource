// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.PacketBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketSplitter extends MessageToByteEncoder<ByteBuf>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf buffer, final ByteBuf byteBuf) {
        final int i = buffer.readableBytes();
        final int j = PacketBuf.getVarIntSize(i);
        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
        }
        final PacketBufOld packetbuf = new PacketBufOld(byteBuf);
        packetbuf.ensureWritable(j + i);
        packetbuf.writeVarIntToBuffer(i);
        packetbuf.writeBytes(buffer, buffer.readerIndex(), i);
    }
}
