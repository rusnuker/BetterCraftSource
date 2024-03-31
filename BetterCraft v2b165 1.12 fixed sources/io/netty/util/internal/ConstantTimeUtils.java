// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

public final class ConstantTimeUtils
{
    private ConstantTimeUtils() {
    }
    
    public static int equalsConstantTime(final int x, final int y) {
        int z = -1 ^ (x ^ y);
        z &= z >> 16;
        z &= z >> 8;
        z &= z >> 4;
        z &= z >> 2;
        z &= z >> 1;
        return z & 0x1;
    }
    
    public static int equalsConstantTime(final long x, final long y) {
        long z = -1L ^ (x ^ y);
        z &= z >> 32;
        z &= z >> 16;
        z &= z >> 8;
        z &= z >> 4;
        z &= z >> 2;
        z &= z >> 1;
        return (int)(z & 0x1L);
    }
    
    public static int equalsConstantTime(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        int b = 0;
        for (int end = startPos1 + length, i = startPos1, j = startPos2; i < end; ++i, ++j) {
            b |= (bytes1[i] ^ bytes2[j]);
        }
        return equalsConstantTime(b, 0);
    }
    
    public static int equalsConstantTime(final CharSequence s1, final CharSequence s2) {
        if (s1.length() != s2.length()) {
            return 0;
        }
        int c = 0;
        for (int i = 0; i < s1.length(); ++i) {
            c |= (s1.charAt(i) ^ s2.charAt(i));
        }
        return equalsConstantTime(c, 0);
    }
}
