// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

public final class RedisBulkStringAggregator extends MessageAggregator<RedisMessage, BulkStringHeaderRedisMessage, BulkStringRedisContent, FullBulkStringRedisMessage>
{
    public RedisBulkStringAggregator() {
        super(536870912);
    }
    
    @Override
    protected boolean isStartMessage(final RedisMessage msg) throws Exception {
        return msg instanceof BulkStringHeaderRedisMessage && !this.isAggregated(msg);
    }
    
    @Override
    protected boolean isContentMessage(final RedisMessage msg) throws Exception {
        return msg instanceof BulkStringRedisContent;
    }
    
    @Override
    protected boolean isLastContentMessage(final BulkStringRedisContent msg) throws Exception {
        return msg instanceof LastBulkStringRedisContent;
    }
    
    @Override
    protected boolean isAggregated(final RedisMessage msg) throws Exception {
        return msg instanceof FullBulkStringRedisMessage;
    }
    
    @Override
    protected boolean isContentLengthInvalid(final BulkStringHeaderRedisMessage start, final int maxContentLength) throws Exception {
        return start.bulkStringLength() > maxContentLength;
    }
    
    @Override
    protected Object newContinueResponse(final BulkStringHeaderRedisMessage start, final int maxContentLength, final ChannelPipeline pipeline) throws Exception {
        return null;
    }
    
    @Override
    protected boolean closeAfterContinueResponse(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean ignoreContentAfterContinueResponse(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected FullBulkStringRedisMessage beginAggregation(final BulkStringHeaderRedisMessage start, final ByteBuf content) throws Exception {
        return new FullBulkStringRedisMessage(content);
    }
}
