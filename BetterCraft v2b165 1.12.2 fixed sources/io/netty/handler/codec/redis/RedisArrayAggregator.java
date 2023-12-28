// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import java.util.ArrayList;
import io.netty.handler.codec.CodecException;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayDeque;
import java.util.Deque;
import io.netty.handler.codec.MessageToMessageDecoder;

public final class RedisArrayAggregator extends MessageToMessageDecoder<RedisMessage>
{
    private final Deque<AggregateState> depths;
    
    public RedisArrayAggregator() {
        this.depths = new ArrayDeque<AggregateState>(4);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, RedisMessage msg, final List<Object> out) throws Exception {
        if (msg instanceof ArrayHeaderRedisMessage) {
            msg = this.decodeRedisArrayHeader((ArrayHeaderRedisMessage)msg);
            if (msg == null) {
                return;
            }
        }
        else {
            ReferenceCountUtil.retain(msg);
        }
        while (!this.depths.isEmpty()) {
            final AggregateState current = this.depths.peek();
            current.children.add(msg);
            if (current.children.size() != current.length) {
                return;
            }
            msg = new ArrayRedisMessage(current.children);
            this.depths.pop();
        }
        out.add(msg);
    }
    
    private RedisMessage decodeRedisArrayHeader(final ArrayHeaderRedisMessage header) {
        if (header.isNull()) {
            return ArrayRedisMessage.NULL_INSTANCE;
        }
        if (header.length() == 0L) {
            return ArrayRedisMessage.EMPTY_INSTANCE;
        }
        if (header.length() <= 0L) {
            throw new CodecException("bad length: " + header.length());
        }
        if (header.length() > 2147483647L) {
            throw new CodecException("this codec doesn't support longer length than 2147483647");
        }
        this.depths.push(new AggregateState((int)header.length()));
        return null;
    }
    
    private static final class AggregateState
    {
        private final int length;
        private final List<RedisMessage> children;
        
        AggregateState(final int length) {
            this.length = length;
            this.children = new ArrayList<RedisMessage>(length);
        }
    }
}
