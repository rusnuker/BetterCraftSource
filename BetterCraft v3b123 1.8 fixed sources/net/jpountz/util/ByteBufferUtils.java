// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.util;

import java.nio.ReadOnlyBufferException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public enum ByteBufferUtils
{
    public static void checkRange(final ByteBuffer buf, final int off, final int len) {
        SafeUtils.checkLength(len);
        if (len > 0) {
            checkRange(buf, off);
            checkRange(buf, off + len - 1);
        }
    }
    
    public static void checkRange(final ByteBuffer buf, final int off) {
        if (off < 0 || off >= buf.capacity()) {
            throw new ArrayIndexOutOfBoundsException(off);
        }
    }
    
    public static ByteBuffer inLittleEndianOrder(final ByteBuffer buf) {
        if (buf.order().equals(ByteOrder.LITTLE_ENDIAN)) {
            return buf;
        }
        return buf.duplicate().order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public static ByteBuffer inNativeByteOrder(final ByteBuffer buf) {
        if (buf.order().equals(Utils.NATIVE_BYTE_ORDER)) {
            return buf;
        }
        return buf.duplicate().order(Utils.NATIVE_BYTE_ORDER);
    }
    
    public static byte readByte(final ByteBuffer buf, final int i) {
        return buf.get(i);
    }
    
    public static void writeInt(final ByteBuffer buf, final int i, final int v) {
        assert buf.order() == Utils.NATIVE_BYTE_ORDER;
        buf.putInt(i, v);
    }
    
    public static int readInt(final ByteBuffer buf, final int i) {
        assert buf.order() == Utils.NATIVE_BYTE_ORDER;
        return buf.getInt(i);
    }
    
    public static int readIntLE(final ByteBuffer buf, final int i) {
        assert buf.order() == ByteOrder.LITTLE_ENDIAN;
        return buf.getInt(i);
    }
    
    public static void writeLong(final ByteBuffer buf, final int i, final long v) {
        assert buf.order() == Utils.NATIVE_BYTE_ORDER;
        buf.putLong(i, v);
    }
    
    public static long readLong(final ByteBuffer buf, final int i) {
        assert buf.order() == Utils.NATIVE_BYTE_ORDER;
        return buf.getLong(i);
    }
    
    public static long readLongLE(final ByteBuffer buf, final int i) {
        assert buf.order() == ByteOrder.LITTLE_ENDIAN;
        return buf.getLong(i);
    }
    
    public static void writeByte(final ByteBuffer dest, final int off, final int i) {
        dest.put(off, (byte)i);
    }
    
    public static void writeShortLE(final ByteBuffer dest, final int off, final int i) {
        dest.put(off, (byte)i);
        dest.put(off + 1, (byte)(i >>> 8));
    }
    
    public static void checkNotReadOnly(final ByteBuffer buffer) {
        if (buffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
    }
    
    public static int readShortLE(final ByteBuffer buf, final int i) {
        return (buf.get(i) & 0xFF) | (buf.get(i + 1) & 0xFF) << 8;
    }
}
