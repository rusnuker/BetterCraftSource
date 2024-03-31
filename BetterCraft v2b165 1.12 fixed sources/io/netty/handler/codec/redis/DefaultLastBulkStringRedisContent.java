// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;

public final class DefaultLastBulkStringRedisContent extends DefaultBulkStringRedisContent implements LastBulkStringRedisContent
{
    public DefaultLastBulkStringRedisContent(final ByteBuf content) {
        super(content);
    }
    
    @Override
    public LastBulkStringRedisContent copy() {
        return (LastBulkStringRedisContent)super.copy();
    }
    
    @Override
    public LastBulkStringRedisContent duplicate() {
        return (LastBulkStringRedisContent)super.duplicate();
    }
    
    @Override
    public LastBulkStringRedisContent retainedDuplicate() {
        return (LastBulkStringRedisContent)super.retainedDuplicate();
    }
    
    @Override
    public LastBulkStringRedisContent replace(final ByteBuf content) {
        return new DefaultLastBulkStringRedisContent(content);
    }
    
    @Override
    public LastBulkStringRedisContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public LastBulkStringRedisContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public LastBulkStringRedisContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public LastBulkStringRedisContent touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
