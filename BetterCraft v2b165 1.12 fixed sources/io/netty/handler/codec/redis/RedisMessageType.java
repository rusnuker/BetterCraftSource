// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

public enum RedisMessageType
{
    SIMPLE_STRING((byte)43, true), 
    ERROR((byte)45, true), 
    INTEGER((byte)58, true), 
    BULK_STRING((byte)36, false), 
    ARRAY_HEADER((byte)42, false), 
    ARRAY((byte)42, false);
    
    private final byte value;
    private final boolean inline;
    
    private RedisMessageType(final byte value, final boolean inline) {
        this.value = value;
        this.inline = inline;
    }
    
    public byte value() {
        return this.value;
    }
    
    public boolean isInline() {
        return this.inline;
    }
    
    public static RedisMessageType valueOf(final byte value) {
        switch (value) {
            case 43: {
                return RedisMessageType.SIMPLE_STRING;
            }
            case 45: {
                return RedisMessageType.ERROR;
            }
            case 58: {
                return RedisMessageType.INTEGER;
            }
            case 36: {
                return RedisMessageType.BULK_STRING;
            }
            case 42: {
                return RedisMessageType.ARRAY_HEADER;
            }
            default: {
                throw new RedisCodecException("Unknown RedisMessageType: " + value);
            }
        }
    }
}
