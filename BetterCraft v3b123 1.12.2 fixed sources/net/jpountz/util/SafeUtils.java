// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.util;

import java.nio.ByteOrder;

public enum SafeUtils
{
    public static void checkRange(final byte[] buf, final int off) {
        if (off < 0 || off >= buf.length) {
            throw new ArrayIndexOutOfBoundsException(off);
        }
    }
    
    public static void checkRange(final byte[] buf, final int off, final int len) {
        checkLength(len);
        if (len > 0) {
            checkRange(buf, off);
            checkRange(buf, off + len - 1);
        }
    }
    
    public static void checkLength(final int len) {
        if (len < 0) {
            throw new IllegalArgumentException("lengths must be >= 0");
        }
    }
    
    public static byte readByte(final byte[] buf, final int i) {
        return buf[i];
    }
    
    public static int readIntBE(final byte[] buf, final int i) {
        return (buf[i] & 0xFF) << 24 | (buf[i + 1] & 0xFF) << 16 | (buf[i + 2] & 0xFF) << 8 | (buf[i + 3] & 0xFF);
    }
    
    public static int readIntLE(final byte[] buf, final int i) {
        return (buf[i] & 0xFF) | (buf[i + 1] & 0xFF) << 8 | (buf[i + 2] & 0xFF) << 16 | (buf[i + 3] & 0xFF) << 24;
    }
    
    public static int readInt(final byte[] buf, final int i) {
        if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            return readIntBE(buf, i);
        }
        return readIntLE(buf, i);
    }
    
    public static long readLongLE(final byte[] buf, final int i) {
        return ((long)buf[i] & 0xFFL) | ((long)buf[i + 1] & 0xFFL) << 8 | ((long)buf[i + 2] & 0xFFL) << 16 | ((long)buf[i + 3] & 0xFFL) << 24 | ((long)buf[i + 4] & 0xFFL) << 32 | ((long)buf[i + 5] & 0xFFL) << 40 | ((long)buf[i + 6] & 0xFFL) << 48 | ((long)buf[i + 7] & 0xFFL) << 56;
    }
    
    public static void writeShortLE(final byte[] buf, int off, final int v) {
        buf[off++] = (byte)v;
        buf[off++] = (byte)(v >>> 8);
    }
    
    public static void writeInt(final int[] buf, final int off, final int v) {
        buf[off] = v;
    }
    
    public static int readInt(final int[] buf, final int off) {
        return buf[off];
    }
    
    public static void writeByte(final byte[] dest, final int off, final int i) {
        dest[off] = (byte)i;
    }
    
    public static void writeShort(final short[] buf, final int off, final int v) {
        buf[off] = (short)v;
    }
    
    public static int readShortLE(final byte[] buf, final int i) {
        return (buf[i] & 0xFF) | (buf[i + 1] & 0xFF) << 8;
    }
    
    public static int readShort(final short[] buf, final int off) {
        return buf[off] & 0xFFFF;
    }
}
