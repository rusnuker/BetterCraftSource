// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.buffer.Unpooled;
import io.netty.channel.FileRegion;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class AbstractMemcacheObjectEncoder<M extends MemcacheMessage> extends MessageToMessageEncoder<Object>
{
    private boolean expectingMoreContent;
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
        if (msg instanceof MemcacheMessage) {
            if (this.expectingMoreContent) {
                throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
            }
            final M m = (M)msg;
            out.add(this.encodeMessage(ctx, m));
        }
        if (msg instanceof MemcacheContent || msg instanceof ByteBuf || msg instanceof FileRegion) {
            final int contentLength = contentLength(msg);
            if (contentLength > 0) {
                out.add(encodeAndRetain(msg));
            }
            else {
                out.add(Unpooled.EMPTY_BUFFER);
            }
            this.expectingMoreContent = !(msg instanceof LastMemcacheContent);
        }
    }
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return msg instanceof MemcacheObject || msg instanceof ByteBuf || msg instanceof FileRegion;
    }
    
    protected abstract ByteBuf encodeMessage(final ChannelHandlerContext p0, final M p1);
    
    private static int contentLength(final Object msg) {
        if (msg instanceof MemcacheContent) {
            return ((MemcacheContent)msg).content().readableBytes();
        }
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).readableBytes();
        }
        if (msg instanceof FileRegion) {
            return (int)((FileRegion)msg).count();
        }
        throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
    }
    
    private static Object encodeAndRetain(final Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).retain();
        }
        if (msg instanceof MemcacheContent) {
            return ((MemcacheContent)msg).content().retain();
        }
        if (msg instanceof FileRegion) {
            return ((FileRegion)msg).retain();
        }
        throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(msg));
    }
}
