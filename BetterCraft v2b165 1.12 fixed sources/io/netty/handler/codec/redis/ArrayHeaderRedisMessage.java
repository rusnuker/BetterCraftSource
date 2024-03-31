// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.internal.StringUtil;

public class ArrayHeaderRedisMessage implements RedisMessage
{
    private final long length;
    
    public ArrayHeaderRedisMessage(final long length) {
        if (length < -1L) {
            throw new RedisCodecException("length: " + length + " (expected: >= " + -1 + ")");
        }
        this.length = length;
    }
    
    public final long length() {
        return this.length;
    }
    
    public boolean isNull() {
        return this.length == -1L;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "length=" + this.length + ']';
    }
}
