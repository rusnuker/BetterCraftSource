// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface BulkStringRedisContent extends RedisMessage, ByteBufHolder
{
    BulkStringRedisContent copy();
    
    BulkStringRedisContent duplicate();
    
    BulkStringRedisContent retainedDuplicate();
    
    BulkStringRedisContent replace(final ByteBuf p0);
    
    BulkStringRedisContent retain();
    
    BulkStringRedisContent retain(final int p0);
    
    BulkStringRedisContent touch();
    
    BulkStringRedisContent touch(final Object p0);
}
