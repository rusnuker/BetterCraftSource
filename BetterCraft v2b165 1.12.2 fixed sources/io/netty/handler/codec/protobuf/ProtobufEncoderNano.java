// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.protobuf;

import io.netty.buffer.ByteBuf;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import com.google.protobuf.nano.MessageNano;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class ProtobufEncoderNano extends MessageToMessageEncoder<MessageNano>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final MessageNano msg, final List<Object> out) throws Exception {
        final int size = msg.getSerializedSize();
        final ByteBuf buffer = ctx.alloc().heapBuffer(size, size);
        final byte[] array = buffer.array();
        final CodedOutputByteBufferNano cobbn = CodedOutputByteBufferNano.newInstance(array, buffer.arrayOffset(), buffer.capacity());
        msg.writeTo(cobbn);
        buffer.writerIndex(size);
        out.add(buffer);
    }
}
