// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.util;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import sun.misc.Unsafe;

public enum UnsafeUtils
{
    private static final Unsafe UNSAFE;
    private static final long BYTE_ARRAY_OFFSET;
    private static final int BYTE_ARRAY_SCALE;
    private static final long INT_ARRAY_OFFSET;
    private static final int INT_ARRAY_SCALE;
    private static final long SHORT_ARRAY_OFFSET;
    private static final int SHORT_ARRAY_SCALE;
    
    public static void checkRange(final byte[] buf, final int off) {
        SafeUtils.checkRange(buf, off);
    }
    
    public static void checkRange(final byte[] buf, final int off, final int len) {
        SafeUtils.checkRange(buf, off, len);
    }
    
    public static void checkLength(final int len) {
        SafeUtils.checkLength(len);
    }
    
    public static byte readByte(final byte[] src, final int srcOff) {
        return UnsafeUtils.UNSAFE.getByte(src, UnsafeUtils.BYTE_ARRAY_OFFSET + UnsafeUtils.BYTE_ARRAY_SCALE * srcOff);
    }
    
    public static void writeByte(final byte[] src, final int srcOff, final byte value) {
        UnsafeUtils.UNSAFE.putByte(src, UnsafeUtils.BYTE_ARRAY_OFFSET + UnsafeUtils.BYTE_ARRAY_SCALE * srcOff, value);
    }
    
    public static void writeByte(final byte[] src, final int srcOff, final int value) {
        writeByte(src, srcOff, (byte)value);
    }
    
    public static long readLong(final byte[] src, final int srcOff) {
        return UnsafeUtils.UNSAFE.getLong(src, UnsafeUtils.BYTE_ARRAY_OFFSET + srcOff);
    }
    
    public static long readLongLE(final byte[] src, final int srcOff) {
        long i = readLong(src, srcOff);
        if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            i = Long.reverseBytes(i);
        }
        return i;
    }
    
    public static void writeLong(final byte[] dest, final int destOff, final long value) {
        UnsafeUtils.UNSAFE.putLong(dest, UnsafeUtils.BYTE_ARRAY_OFFSET + destOff, value);
    }
    
    public static int readInt(final byte[] src, final int srcOff) {
        return UnsafeUtils.UNSAFE.getInt(src, UnsafeUtils.BYTE_ARRAY_OFFSET + srcOff);
    }
    
    public static int readIntLE(final byte[] src, final int srcOff) {
        int i = readInt(src, srcOff);
        if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            i = Integer.reverseBytes(i);
        }
        return i;
    }
    
    public static void writeInt(final byte[] dest, final int destOff, final int value) {
        UnsafeUtils.UNSAFE.putInt(dest, UnsafeUtils.BYTE_ARRAY_OFFSET + destOff, value);
    }
    
    public static short readShort(final byte[] src, final int srcOff) {
        return UnsafeUtils.UNSAFE.getShort(src, UnsafeUtils.BYTE_ARRAY_OFFSET + srcOff);
    }
    
    public static int readShortLE(final byte[] src, final int srcOff) {
        short s = readShort(src, srcOff);
        if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            s = Short.reverseBytes(s);
        }
        return s & 0xFFFF;
    }
    
    public static void writeShort(final byte[] dest, final int destOff, final short value) {
        UnsafeUtils.UNSAFE.putShort(dest, UnsafeUtils.BYTE_ARRAY_OFFSET + destOff, value);
    }
    
    public static void writeShortLE(final byte[] buf, final int off, final int v) {
        writeByte(buf, off, (byte)v);
        writeByte(buf, off + 1, (byte)(v >>> 8));
    }
    
    public static int readInt(final int[] src, final int srcOff) {
        return UnsafeUtils.UNSAFE.getInt(src, UnsafeUtils.INT_ARRAY_OFFSET + UnsafeUtils.INT_ARRAY_SCALE * srcOff);
    }
    
    public static void writeInt(final int[] dest, final int destOff, final int value) {
        UnsafeUtils.UNSAFE.putInt(dest, UnsafeUtils.INT_ARRAY_OFFSET + UnsafeUtils.INT_ARRAY_SCALE * destOff, value);
    }
    
    public static int readShort(final short[] src, final int srcOff) {
        return UnsafeUtils.UNSAFE.getShort(src, UnsafeUtils.SHORT_ARRAY_OFFSET + UnsafeUtils.SHORT_ARRAY_SCALE * srcOff) & 0xFFFF;
    }
    
    public static void writeShort(final short[] dest, final int destOff, final int value) {
        UnsafeUtils.UNSAFE.putShort(dest, UnsafeUtils.SHORT_ARRAY_OFFSET + UnsafeUtils.SHORT_ARRAY_SCALE * destOff, (short)value);
    }
    
    static {
        try {
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
            BYTE_ARRAY_OFFSET = UnsafeUtils.UNSAFE.arrayBaseOffset(byte[].class);
            BYTE_ARRAY_SCALE = UnsafeUtils.UNSAFE.arrayIndexScale(byte[].class);
            INT_ARRAY_OFFSET = UnsafeUtils.UNSAFE.arrayBaseOffset(int[].class);
            INT_ARRAY_SCALE = UnsafeUtils.UNSAFE.arrayIndexScale(int[].class);
            SHORT_ARRAY_OFFSET = UnsafeUtils.UNSAFE.arrayBaseOffset(short[].class);
            SHORT_ARRAY_SCALE = UnsafeUtils.UNSAFE.arrayIndexScale(short[].class);
        }
        catch (final IllegalAccessException e) {
            throw new ExceptionInInitializerError("Cannot access Unsafe");
        }
        catch (final NoSuchFieldException e2) {
            throw new ExceptionInInitializerError("Cannot access Unsafe");
        }
        catch (final SecurityException e3) {
            throw new ExceptionInInitializerError("Cannot access Unsafe");
        }
    }
}
