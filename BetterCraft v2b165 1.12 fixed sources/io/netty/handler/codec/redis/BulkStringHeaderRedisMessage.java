// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

public class BulkStringHeaderRedisMessage implements RedisMessage
{
    private final int bulkStringLength;
    
    public BulkStringHeaderRedisMessage(final int bulkStringLength) {
        if (bulkStringLength <= 0) {
            throw new RedisCodecException("bulkStringLength: " + bulkStringLength + " (expected: > 0)");
        }
        this.bulkStringLength = bulkStringLength;
    }
    
    public final int bulkStringLength() {
        return this.bulkStringLength;
    }
    
    public boolean isNull() {
        return this.bulkStringLength == -1;
    }
}
