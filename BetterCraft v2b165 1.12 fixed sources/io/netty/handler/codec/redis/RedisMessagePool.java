// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;

public interface RedisMessagePool
{
    SimpleStringRedisMessage getSimpleString(final String p0);
    
    SimpleStringRedisMessage getSimpleString(final ByteBuf p0);
    
    ErrorRedisMessage getError(final String p0);
    
    ErrorRedisMessage getError(final ByteBuf p0);
    
    IntegerRedisMessage getInteger(final long p0);
    
    IntegerRedisMessage getInteger(final ByteBuf p0);
    
    byte[] getByteBufOfInteger(final long p0);
}
