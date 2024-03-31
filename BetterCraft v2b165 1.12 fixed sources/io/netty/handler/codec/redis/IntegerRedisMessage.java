// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;

public final class IntegerRedisMessage implements RedisMessage
{
    private final long value;
    
    public IntegerRedisMessage(final long value) {
        this.value = value;
    }
    
    public long value() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "value=" + this.value + ']';
    }
}
