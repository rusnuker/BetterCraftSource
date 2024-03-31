// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.redis;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.CharsetUtil;

final class RedisCodecUtil
{
    private RedisCodecUtil() {
    }
    
    static byte[] longToAsciiBytes(final long value) {
        return Long.toString(value).getBytes(CharsetUtil.US_ASCII);
    }
    
    static short makeShort(final char first, final char second) {
        return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? ((short)(second << 8 | first)) : ((short)(first << 8 | second));
    }
    
    static byte[] shortToBytes(final short value) {
        final byte[] bytes = new byte[2];
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            bytes[1] = (byte)(value >> 8 & 0xFF);
            bytes[0] = (byte)(value & 0xFF);
        }
        else {
            bytes[0] = (byte)(value >> 8 & 0xFF);
            bytes[1] = (byte)(value & 0xFF);
        }
        return bytes;
    }
}
