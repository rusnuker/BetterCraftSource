// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketPrepender extends ByteToMessageDecoder
{
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> objects) {
        buffer.markReaderIndex();
        final byte[] abyte = new byte[3];
        for (int i = 0; i < abyte.length; ++i) {
            if (!buffer.isReadable()) {
                buffer.resetReaderIndex();
                return;
            }
            abyte[i] = buffer.readByte();
            if (abyte[i] >= 0) {
                final PacketBufOld packetbuf = new PacketBufOld(Unpooled.wrappedBuffer(abyte));
                try {
                    final int j = packetbuf.readVarIntFromBuffer();
                    if (buffer.readableBytes() >= j) {
                        objects.add(buffer.readBytes(j));
                        return;
                    }
                    buffer.resetReaderIndex();
                }
                finally {
                    packetbuf.release();
                }
                packetbuf.release();
                packetbuf.release();
                return;
            }
        }
        throw new RuntimeException("length wider than 21-bit");
    }
}
