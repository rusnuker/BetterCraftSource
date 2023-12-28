// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public class DefaultBulkStringRedisContent extends DefaultByteBufHolder implements BulkStringRedisContent
{
    public DefaultBulkStringRedisContent(final ByteBuf content) {
        super(content);
    }
    
    @Override
    public BulkStringRedisContent copy() {
        return (BulkStringRedisContent)super.copy();
    }
    
    @Override
    public BulkStringRedisContent duplicate() {
        return (BulkStringRedisContent)super.duplicate();
    }
    
    @Override
    public BulkStringRedisContent retainedDuplicate() {
        return (BulkStringRedisContent)super.retainedDuplicate();
    }
    
    @Override
    public BulkStringRedisContent replace(final ByteBuf content) {
        return new DefaultBulkStringRedisContent(content);
    }
    
    @Override
    public BulkStringRedisContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public BulkStringRedisContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public BulkStringRedisContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public BulkStringRedisContent touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "content=" + this.content() + ']';
    }
}
