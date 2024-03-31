// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.handler.codec.memcache.MemcacheMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.memcache.AbstractMemcacheObjectEncoder;

public abstract class AbstractBinaryMemcacheEncoder<M extends BinaryMemcacheMessage> extends AbstractMemcacheObjectEncoder<M>
{
    private static final int MINIMUM_HEADER_SIZE = 24;
    
    @Override
    protected ByteBuf encodeMessage(final ChannelHandlerContext ctx, final M msg) {
        final ByteBuf buf = ctx.alloc().buffer(24 + msg.extrasLength() + msg.keyLength());
        this.encodeHeader(buf, msg);
        encodeExtras(buf, msg.extras());
        encodeKey(buf, msg.key());
        return buf;
    }
    
    private static void encodeExtras(final ByteBuf buf, final ByteBuf extras) {
        if (extras == null || !extras.isReadable()) {
            return;
        }
        buf.writeBytes(extras);
    }
    
    private static void encodeKey(final ByteBuf buf, final ByteBuf key) {
        if (key == null || !key.isReadable()) {
            return;
        }
        buf.writeBytes(key);
    }
    
    protected abstract void encodeHeader(final ByteBuf p0, final M p1);
}
