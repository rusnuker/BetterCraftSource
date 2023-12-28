// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class ProtobufVarint32LengthFieldPrepender extends MessageToByteEncoder<ByteBuf>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) throws Exception {
        final int bodyLen = msg.readableBytes();
        final int headerLen = computeRawVarint32Size(bodyLen);
        out.ensureWritable(headerLen + bodyLen);
        writeRawVarint32(out, bodyLen);
        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }
    
    static void writeRawVarint32(final ByteBuf out, int value) {
        while ((value & 0xFFFFFF80) != 0x0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }
    
    static int computeRawVarint32Size(final int value) {
        if ((value & 0xFFFFFF80) == 0x0) {
            return 1;
        }
        if ((value & 0xFFFFC000) == 0x0) {
            return 2;
        }
        if ((value & 0xFFE00000) == 0x0) {
            return 3;
        }
        if ((value & 0xF0000000) == 0x0) {
            return 4;
        }
        return 5;
    }
}
