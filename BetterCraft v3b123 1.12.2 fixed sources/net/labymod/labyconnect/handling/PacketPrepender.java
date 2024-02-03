// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.PacketBuf;
import io.netty.buffer.Unpooled;
import net.labymod.core.LabyModCore;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketPrepender extends ByteToMessageDecoder
{
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> objects) {
        buffer.markReaderIndex();
        final byte[] a = new byte[3];
        for (int i = 0; i < a.length; ++i) {
            if (!buffer.isReadable()) {
                buffer.resetReaderIndex();
                return;
            }
            a[i] = buffer.readByte();
            if (a[i] >= 0) {
                final PacketBuf buf = LabyModCore.getMinecraft().createPacketBuf(Unpooled.wrappedBuffer(a));
                try {
                    final int varInt = buf.readVarIntFromBuffer();
                    if (buffer.readableBytes() < varInt) {
                        buffer.resetReaderIndex();
                        return;
                    }
                    objects.add(buffer.readBytes(varInt));
                }
                finally {
                    buf.release();
                }
                buf.release();
                return;
            }
        }
        throw new RuntimeException("length wider than 21-bit");
    }
}
