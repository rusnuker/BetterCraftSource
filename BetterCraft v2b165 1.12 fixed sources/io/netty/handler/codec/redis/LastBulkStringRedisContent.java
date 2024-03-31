// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public interface LastBulkStringRedisContent extends BulkStringRedisContent
{
    public static final LastBulkStringRedisContent EMPTY_LAST_CONTENT = new LastBulkStringRedisContent() {
        @Override
        public ByteBuf content() {
            return Unpooled.EMPTY_BUFFER;
        }
        
        @Override
        public LastBulkStringRedisContent copy() {
            return this;
        }
        
        @Override
        public LastBulkStringRedisContent duplicate() {
            return this;
        }
        
        @Override
        public LastBulkStringRedisContent retainedDuplicate() {
            return this;
        }
        
        @Override
        public LastBulkStringRedisContent replace(final ByteBuf content) {
            return new DefaultLastBulkStringRedisContent(content);
        }
        
        @Override
        public LastBulkStringRedisContent retain(final int increment) {
            return this;
        }
        
        @Override
        public LastBulkStringRedisContent retain() {
            return this;
        }
        
        @Override
        public int refCnt() {
            return 1;
        }
        
        @Override
        public LastBulkStringRedisContent touch() {
            return this;
        }
        
        @Override
        public LastBulkStringRedisContent touch(final Object hint) {
            return this;
        }
        
        @Override
        public boolean release() {
            return false;
        }
        
        @Override
        public boolean release(final int decrement) {
            return false;
        }
    };
    
    LastBulkStringRedisContent copy();
    
    LastBulkStringRedisContent duplicate();
    
    LastBulkStringRedisContent retainedDuplicate();
    
    LastBulkStringRedisContent replace(final ByteBuf p0);
    
    LastBulkStringRedisContent retain();
    
    LastBulkStringRedisContent retain(final int p0);
    
    LastBulkStringRedisContent touch();
    
    LastBulkStringRedisContent touch(final Object p0);
}
