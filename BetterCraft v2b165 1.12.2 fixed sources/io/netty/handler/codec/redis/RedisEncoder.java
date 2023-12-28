// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.CodecException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.MessageToMessageEncoder;

public class RedisEncoder extends MessageToMessageEncoder<RedisMessage>
{
    private final RedisMessagePool messagePool;
    
    public RedisEncoder() {
        this(FixedRedisMessagePool.INSTANCE);
    }
    
    public RedisEncoder(final RedisMessagePool messagePool) {
        this.messagePool = ObjectUtil.checkNotNull(messagePool, "messagePool");
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final RedisMessage msg, final List<Object> out) throws Exception {
        try {
            this.writeRedisMessage(ctx.alloc(), msg, out);
        }
        catch (final CodecException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw new CodecException(e2);
        }
    }
    
    private void writeRedisMessage(final ByteBufAllocator allocator, final RedisMessage msg, final List<Object> out) {
        if (msg instanceof SimpleStringRedisMessage) {
            writeSimpleStringMessage(allocator, (SimpleStringRedisMessage)msg, out);
        }
        else if (msg instanceof ErrorRedisMessage) {
            writeErrorMessage(allocator, (ErrorRedisMessage)msg, out);
        }
        else if (msg instanceof IntegerRedisMessage) {
            this.writeIntegerMessage(allocator, (IntegerRedisMessage)msg, out);
        }
        else if (msg instanceof FullBulkStringRedisMessage) {
            this.writeFullBulkStringMessage(allocator, (FullBulkStringRedisMessage)msg, out);
        }
        else if (msg instanceof BulkStringRedisContent) {
            writeBulkStringContent(allocator, (BulkStringRedisContent)msg, out);
        }
        else if (msg instanceof BulkStringHeaderRedisMessage) {
            this.writeBulkStringHeader(allocator, (BulkStringHeaderRedisMessage)msg, out);
        }
        else if (msg instanceof ArrayHeaderRedisMessage) {
            this.writeArrayHeader(allocator, (ArrayHeaderRedisMessage)msg, out);
        }
        else {
            if (!(msg instanceof ArrayRedisMessage)) {
                throw new CodecException("unknown message type: " + msg);
            }
            this.writeArrayMessage(allocator, (ArrayRedisMessage)msg, out);
        }
    }
    
    private static void writeSimpleStringMessage(final ByteBufAllocator allocator, final SimpleStringRedisMessage msg, final List<Object> out) {
        writeString(allocator, RedisMessageType.SIMPLE_STRING.value(), msg.content(), out);
    }
    
    private static void writeErrorMessage(final ByteBufAllocator allocator, final ErrorRedisMessage msg, final List<Object> out) {
        writeString(allocator, RedisMessageType.ERROR.value(), msg.content(), out);
    }
    
    private static void writeString(final ByteBufAllocator allocator, final byte type, final String content, final List<Object> out) {
        final ByteBuf buf = allocator.ioBuffer(1 + ByteBufUtil.utf8MaxBytes(content) + 2);
        buf.writeByte(type);
        ByteBufUtil.writeUtf8(buf, content);
        buf.writeShort(RedisConstants.EOL_SHORT);
        out.add(buf);
    }
    
    private void writeIntegerMessage(final ByteBufAllocator allocator, final IntegerRedisMessage msg, final List<Object> out) {
        final ByteBuf buf = allocator.ioBuffer(23);
        buf.writeByte(RedisMessageType.INTEGER.value());
        buf.writeBytes(this.numberToBytes(msg.value()));
        buf.writeShort(RedisConstants.EOL_SHORT);
        out.add(buf);
    }
    
    private void writeBulkStringHeader(final ByteBufAllocator allocator, final BulkStringHeaderRedisMessage msg, final List<Object> out) {
        final ByteBuf buf = allocator.ioBuffer(1 + (msg.isNull() ? 2 : 22));
        buf.writeByte(RedisMessageType.BULK_STRING.value());
        if (msg.isNull()) {
            buf.writeShort(RedisConstants.NULL_SHORT);
        }
        else {
            buf.writeBytes(this.numberToBytes(msg.bulkStringLength()));
            buf.writeShort(RedisConstants.EOL_SHORT);
        }
        out.add(buf);
    }
    
    private static void writeBulkStringContent(final ByteBufAllocator allocator, final BulkStringRedisContent msg, final List<Object> out) {
        out.add(msg.content().retain());
        if (msg instanceof LastBulkStringRedisContent) {
            out.add(allocator.ioBuffer(2).writeShort(RedisConstants.EOL_SHORT));
        }
    }
    
    private void writeFullBulkStringMessage(final ByteBufAllocator allocator, final FullBulkStringRedisMessage msg, final List<Object> out) {
        if (msg.isNull()) {
            final ByteBuf buf = allocator.ioBuffer(5);
            buf.writeByte(RedisMessageType.BULK_STRING.value());
            buf.writeShort(RedisConstants.NULL_SHORT);
            buf.writeShort(RedisConstants.EOL_SHORT);
            out.add(buf);
        }
        else {
            final ByteBuf headerBuf = allocator.ioBuffer(23);
            headerBuf.writeByte(RedisMessageType.BULK_STRING.value());
            headerBuf.writeBytes(this.numberToBytes(msg.content().readableBytes()));
            headerBuf.writeShort(RedisConstants.EOL_SHORT);
            out.add(headerBuf);
            out.add(msg.content().retain());
            out.add(allocator.ioBuffer(2).writeShort(RedisConstants.EOL_SHORT));
        }
    }
    
    private void writeArrayHeader(final ByteBufAllocator allocator, final ArrayHeaderRedisMessage msg, final List<Object> out) {
        this.writeArrayHeader(allocator, msg.isNull(), msg.length(), out);
    }
    
    private void writeArrayMessage(final ByteBufAllocator allocator, final ArrayRedisMessage msg, final List<Object> out) {
        if (msg.isNull()) {
            this.writeArrayHeader(allocator, msg.isNull(), -1L, out);
        }
        else {
            this.writeArrayHeader(allocator, msg.isNull(), msg.children().size(), out);
            for (final RedisMessage child : msg.children()) {
                this.writeRedisMessage(allocator, child, out);
            }
        }
    }
    
    private void writeArrayHeader(final ByteBufAllocator allocator, final boolean isNull, final long length, final List<Object> out) {
        if (isNull) {
            final ByteBuf buf = allocator.ioBuffer(5);
            buf.writeByte(RedisMessageType.ARRAY_HEADER.value());
            buf.writeShort(RedisConstants.NULL_SHORT);
            buf.writeShort(RedisConstants.EOL_SHORT);
            out.add(buf);
        }
        else {
            final ByteBuf buf = allocator.ioBuffer(23);
            buf.writeByte(RedisMessageType.ARRAY_HEADER.value());
            buf.writeBytes(this.numberToBytes(length));
            buf.writeShort(RedisConstants.EOL_SHORT);
            out.add(buf);
        }
    }
    
    private byte[] numberToBytes(final long value) {
        final byte[] bytes = this.messagePool.getByteBufOfInteger(value);
        return (bytes != null) ? bytes : RedisCodecUtil.longToAsciiBytes(value);
    }
}
