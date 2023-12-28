// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.io.OutputStream;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteBuffer;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.io.InputStream;
import io.netty.util.internal.PlatformDependent;

final class UnsafeByteBufUtil
{
    private static final boolean UNALIGNED;
    private static final byte ZERO = 0;
    
    static byte getByte(final long address) {
        return PlatformDependent.getByte(address);
    }
    
    static short getShort(final long address) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final short v = PlatformDependent.getShort(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Short.reverseBytes(v);
        }
        return (short)(PlatformDependent.getByte(address) << 8 | (PlatformDependent.getByte(address + 1L) & 0xFF));
    }
    
    static short getShortLE(final long address) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final short v = PlatformDependent.getShort(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes(v) : v;
        }
        return (short)((PlatformDependent.getByte(address) & 0xFF) | PlatformDependent.getByte(address + 1L) << 8);
    }
    
    static int getUnsignedMedium(final long address) {
        if (!UnsafeByteBufUtil.UNALIGNED) {
            return (PlatformDependent.getByte(address) & 0xFF) << 16 | (PlatformDependent.getByte(address + 1L) & 0xFF) << 8 | (PlatformDependent.getByte(address + 2L) & 0xFF);
        }
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (PlatformDependent.getByte(address) & 0xFF) | (PlatformDependent.getShort(address + 1L) & 0xFFFF) << 8;
        }
        return (Short.reverseBytes(PlatformDependent.getShort(address)) & 0xFFFF) << 8 | (PlatformDependent.getByte(address + 2L) & 0xFF);
    }
    
    static int getUnsignedMediumLE(final long address) {
        if (!UnsafeByteBufUtil.UNALIGNED) {
            return (PlatformDependent.getByte(address) & 0xFF) | (PlatformDependent.getByte(address + 1L) & 0xFF) << 8 | (PlatformDependent.getByte(address + 1L) & 0xFF) << 16;
        }
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (Short.reverseBytes(PlatformDependent.getShort(address)) & 0xFFFF) << 8 | (PlatformDependent.getByte(address + 2L) & 0xFF);
        }
        return (PlatformDependent.getByte(address) & 0xFF) | (PlatformDependent.getShort(address + 1L) & 0xFFFF) << 8;
    }
    
    static int getInt(final long address) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final int v = PlatformDependent.getInt(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Integer.reverseBytes(v);
        }
        return PlatformDependent.getByte(address) << 24 | (PlatformDependent.getByte(address + 1L) & 0xFF) << 16 | (PlatformDependent.getByte(address + 2L) & 0xFF) << 8 | (PlatformDependent.getByte(address + 3L) & 0xFF);
    }
    
    static int getIntLE(final long address) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final int v = PlatformDependent.getInt(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(v) : v;
        }
        return (PlatformDependent.getByte(address) & 0xFF) | (PlatformDependent.getByte(address + 1L) & 0xFF) << 8 | (PlatformDependent.getByte(address + 2L) & 0xFF) << 16 | PlatformDependent.getByte(address + 3L) << 24;
    }
    
    static long getLong(final long address) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final long v = PlatformDependent.getLong(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Long.reverseBytes(v);
        }
        return (long)PlatformDependent.getByte(address) << 56 | ((long)PlatformDependent.getByte(address + 1L) & 0xFFL) << 48 | ((long)PlatformDependent.getByte(address + 2L) & 0xFFL) << 40 | ((long)PlatformDependent.getByte(address + 3L) & 0xFFL) << 32 | ((long)PlatformDependent.getByte(address + 4L) & 0xFFL) << 24 | ((long)PlatformDependent.getByte(address + 5L) & 0xFFL) << 16 | ((long)PlatformDependent.getByte(address + 6L) & 0xFFL) << 8 | ((long)PlatformDependent.getByte(address + 7L) & 0xFFL);
    }
    
    static long getLongLE(final long address) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final long v = PlatformDependent.getLong(address);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(v) : v;
        }
        return ((long)PlatformDependent.getByte(address) & 0xFFL) | ((long)PlatformDependent.getByte(address + 1L) & 0xFFL) << 8 | ((long)PlatformDependent.getByte(address + 2L) & 0xFFL) << 16 | ((long)PlatformDependent.getByte(address + 3L) & 0xFFL) << 24 | ((long)PlatformDependent.getByte(address + 4L) & 0xFFL) << 32 | ((long)PlatformDependent.getByte(address + 5L) & 0xFFL) << 40 | ((long)PlatformDependent.getByte(address + 6L) & 0xFFL) << 48 | (long)PlatformDependent.getByte(address + 7L) << 56;
    }
    
    static void setByte(final long address, final int value) {
        PlatformDependent.putByte(address, (byte)value);
    }
    
    static void setShort(final long address, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putShort(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? ((short)value) : Short.reverseBytes((short)value));
        }
        else {
            PlatformDependent.putByte(address, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 1L, (byte)value);
        }
    }
    
    static void setShortLE(final long address, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putShort(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes((short)value) : ((short)value));
        }
        else {
            PlatformDependent.putByte(address, (byte)value);
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 8));
        }
    }
    
    static void setMedium(final long address, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                PlatformDependent.putByte(address, (byte)value);
                PlatformDependent.putShort(address + 1L, (short)(value >>> 8));
            }
            else {
                PlatformDependent.putShort(address, Short.reverseBytes((short)(value >>> 8)));
                PlatformDependent.putByte(address + 2L, (byte)value);
            }
        }
        else {
            PlatformDependent.putByte(address, (byte)(value >>> 16));
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 2L, (byte)value);
        }
    }
    
    static void setMediumLE(final long address, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                PlatformDependent.putShort(address, Short.reverseBytes((short)(value >>> 8)));
                PlatformDependent.putByte(address + 2L, (byte)value);
            }
            else {
                PlatformDependent.putByte(address, (byte)value);
                PlatformDependent.putShort(address + 1L, (short)(value >>> 8));
            }
        }
        else {
            PlatformDependent.putByte(address, (byte)value);
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 2L, (byte)(value >>> 16));
        }
    }
    
    static void setInt(final long address, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putInt(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
        }
        else {
            PlatformDependent.putByte(address, (byte)(value >>> 24));
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 16));
            PlatformDependent.putByte(address + 2L, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 3L, (byte)value);
        }
    }
    
    static void setIntLE(final long address, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putInt(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(value) : value);
        }
        else {
            PlatformDependent.putByte(address, (byte)value);
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 2L, (byte)(value >>> 16));
            PlatformDependent.putByte(address + 3L, (byte)(value >>> 24));
        }
    }
    
    static void setLong(final long address, final long value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putLong(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
        }
        else {
            PlatformDependent.putByte(address, (byte)(value >>> 56));
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 48));
            PlatformDependent.putByte(address + 2L, (byte)(value >>> 40));
            PlatformDependent.putByte(address + 3L, (byte)(value >>> 32));
            PlatformDependent.putByte(address + 4L, (byte)(value >>> 24));
            PlatformDependent.putByte(address + 5L, (byte)(value >>> 16));
            PlatformDependent.putByte(address + 6L, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 7L, (byte)value);
        }
    }
    
    static void setLongLE(final long address, final long value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putLong(address, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(value) : value);
        }
        else {
            PlatformDependent.putByte(address, (byte)value);
            PlatformDependent.putByte(address + 1L, (byte)(value >>> 8));
            PlatformDependent.putByte(address + 2L, (byte)(value >>> 16));
            PlatformDependent.putByte(address + 3L, (byte)(value >>> 24));
            PlatformDependent.putByte(address + 4L, (byte)(value >>> 32));
            PlatformDependent.putByte(address + 5L, (byte)(value >>> 40));
            PlatformDependent.putByte(address + 6L, (byte)(value >>> 48));
            PlatformDependent.putByte(address + 7L, (byte)(value >>> 56));
        }
    }
    
    static byte getByte(final byte[] array, final int index) {
        return PlatformDependent.getByte(array, index);
    }
    
    static short getShort(final byte[] array, final int index) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final short v = PlatformDependent.getShort(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Short.reverseBytes(v);
        }
        return (short)(PlatformDependent.getByte(array, index) << 8 | (PlatformDependent.getByte(array, index + 1) & 0xFF));
    }
    
    static short getShortLE(final byte[] array, final int index) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final short v = PlatformDependent.getShort(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes(v) : v;
        }
        return (short)((PlatformDependent.getByte(index) & 0xFF) | PlatformDependent.getByte(index + 1) << 8);
    }
    
    static int getUnsignedMedium(final byte[] array, final int index) {
        if (!UnsafeByteBufUtil.UNALIGNED) {
            return (PlatformDependent.getByte(array, index) & 0xFF) << 16 | (PlatformDependent.getByte(array, index + 1) & 0xFF) << 8 | (PlatformDependent.getByte(array, index + 2) & 0xFF);
        }
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (PlatformDependent.getByte(array, index) & 0xFF) | (PlatformDependent.getShort(array, index + 1) & 0xFFFF) << 8;
        }
        return (Short.reverseBytes(PlatformDependent.getShort(array, index)) & 0xFFFF) << 8 | (PlatformDependent.getByte(array, index + 2) & 0xFF);
    }
    
    static int getUnsignedMediumLE(final byte[] array, final int index) {
        if (!UnsafeByteBufUtil.UNALIGNED) {
            return (PlatformDependent.getByte(array, index) & 0xFF) | (PlatformDependent.getByte(array, index + 1) & 0xFF) << 8 | (PlatformDependent.getByte(array, index + 2) & 0xFF) << 16;
        }
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (Short.reverseBytes(PlatformDependent.getShort(array, index)) & 0xFFFF) << 8 | (PlatformDependent.getByte(array, index + 2) & 0xFF);
        }
        return (PlatformDependent.getByte(array, index) & 0xFF) | (PlatformDependent.getShort(array, index + 1) & 0xFFFF) << 8;
    }
    
    static int getInt(final byte[] array, final int index) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final int v = PlatformDependent.getInt(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Integer.reverseBytes(v);
        }
        return PlatformDependent.getByte(array, index) << 24 | (PlatformDependent.getByte(array, index + 1) & 0xFF) << 16 | (PlatformDependent.getByte(array, index + 2) & 0xFF) << 8 | (PlatformDependent.getByte(array, index + 3) & 0xFF);
    }
    
    static int getIntLE(final byte[] array, final int index) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final int v = PlatformDependent.getInt(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(v) : v;
        }
        return (PlatformDependent.getByte(array, index) & 0xFF) | (PlatformDependent.getByte(array, index + 1) & 0xFF) << 8 | (PlatformDependent.getByte(array, index + 2) & 0xFF) << 16 | PlatformDependent.getByte(array, index + 3) << 24;
    }
    
    static long getLong(final byte[] array, final int index) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final long v = PlatformDependent.getLong(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? v : Long.reverseBytes(v);
        }
        return (long)PlatformDependent.getByte(array, index) << 56 | ((long)PlatformDependent.getByte(array, index + 1) & 0xFFL) << 48 | ((long)PlatformDependent.getByte(array, index + 2) & 0xFFL) << 40 | ((long)PlatformDependent.getByte(array, index + 3) & 0xFFL) << 32 | ((long)PlatformDependent.getByte(array, index + 4) & 0xFFL) << 24 | ((long)PlatformDependent.getByte(array, index + 5) & 0xFFL) << 16 | ((long)PlatformDependent.getByte(array, index + 6) & 0xFFL) << 8 | ((long)PlatformDependent.getByte(array, index + 7) & 0xFFL);
    }
    
    static long getLongLE(final byte[] array, final int index) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            final long v = PlatformDependent.getLong(array, index);
            return PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(v) : v;
        }
        return ((long)PlatformDependent.getByte(array, index) & 0xFFL) | ((long)PlatformDependent.getByte(array, index + 1) & 0xFFL) << 8 | ((long)PlatformDependent.getByte(array, index + 2) & 0xFFL) << 16 | ((long)PlatformDependent.getByte(array, index + 3) & 0xFFL) << 24 | ((long)PlatformDependent.getByte(array, index + 4) & 0xFFL) << 32 | ((long)PlatformDependent.getByte(array, index + 5) & 0xFFL) << 40 | ((long)PlatformDependent.getByte(array, index + 6) & 0xFFL) << 48 | (long)PlatformDependent.getByte(array, index + 7) << 56;
    }
    
    static void setByte(final byte[] array, final int index, final int value) {
        PlatformDependent.putByte(array, index, (byte)value);
    }
    
    static void setShort(final byte[] array, final int index, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putShort(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? ((short)value) : Short.reverseBytes((short)value));
        }
        else {
            PlatformDependent.putByte(array, index, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 1, (byte)value);
        }
    }
    
    static void setShortLE(final byte[] array, final int index, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putShort(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Short.reverseBytes((short)value) : ((short)value));
        }
        else {
            PlatformDependent.putByte(array, index, (byte)value);
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 8));
        }
    }
    
    static void setMedium(final byte[] array, final int index, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                PlatformDependent.putByte(array, index, (byte)value);
                PlatformDependent.putShort(array, index + 1, (short)(value >>> 8));
            }
            else {
                PlatformDependent.putShort(array, index, Short.reverseBytes((short)(value >>> 8)));
                PlatformDependent.putByte(array, index + 2, (byte)value);
            }
        }
        else {
            PlatformDependent.putByte(array, index, (byte)(value >>> 16));
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte)value);
        }
    }
    
    static void setMediumLE(final byte[] array, final int index, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
                PlatformDependent.putShort(array, index, Short.reverseBytes((short)(value >>> 8)));
                PlatformDependent.putByte(array, index + 2, (byte)value);
            }
            else {
                PlatformDependent.putByte(array, index, (byte)value);
                PlatformDependent.putShort(array, index + 1, (short)(value >>> 8));
            }
        }
        else {
            PlatformDependent.putByte(array, index, (byte)value);
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte)(value >>> 16));
        }
    }
    
    static void setInt(final byte[] array, final int index, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putInt(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
        }
        else {
            PlatformDependent.putByte(array, index, (byte)(value >>> 24));
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 16));
            PlatformDependent.putByte(array, index + 2, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 3, (byte)value);
        }
    }
    
    static void setIntLE(final byte[] array, final int index, final int value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putInt(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Integer.reverseBytes(value) : value);
        }
        else {
            PlatformDependent.putByte(array, index, (byte)value);
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte)(value >>> 16));
            PlatformDependent.putByte(array, index + 3, (byte)(value >>> 24));
        }
    }
    
    static void setLong(final byte[] array, final int index, final long value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putLong(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
        }
        else {
            PlatformDependent.putByte(array, index, (byte)(value >>> 56));
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 48));
            PlatformDependent.putByte(array, index + 2, (byte)(value >>> 40));
            PlatformDependent.putByte(array, index + 3, (byte)(value >>> 32));
            PlatformDependent.putByte(array, index + 4, (byte)(value >>> 24));
            PlatformDependent.putByte(array, index + 5, (byte)(value >>> 16));
            PlatformDependent.putByte(array, index + 6, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 7, (byte)value);
        }
    }
    
    static void setLongLE(final byte[] array, final int index, final long value) {
        if (UnsafeByteBufUtil.UNALIGNED) {
            PlatformDependent.putLong(array, index, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? Long.reverseBytes(value) : value);
        }
        else {
            PlatformDependent.putByte(array, index, (byte)value);
            PlatformDependent.putByte(array, index + 1, (byte)(value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte)(value >>> 16));
            PlatformDependent.putByte(array, index + 3, (byte)(value >>> 24));
            PlatformDependent.putByte(array, index + 4, (byte)(value >>> 32));
            PlatformDependent.putByte(array, index + 5, (byte)(value >>> 40));
            PlatformDependent.putByte(array, index + 6, (byte)(value >>> 48));
            PlatformDependent.putByte(array, index + 7, (byte)(value >>> 56));
        }
    }
    
    static void setZero(final byte[] array, final int index, final int length) {
        if (length == 0) {
            return;
        }
        PlatformDependent.setMemory(array, index, length, (byte)0);
    }
    
    static ByteBuf copy(final AbstractByteBuf buf, final long addr, final int index, final int length) {
        buf.checkIndex(index, length);
        final ByteBuf copy = buf.alloc().directBuffer(length, buf.maxCapacity());
        if (length != 0) {
            if (copy.hasMemoryAddress()) {
                PlatformDependent.copyMemory(addr, copy.memoryAddress(), length);
                copy.setIndex(0, length);
            }
            else {
                copy.writeBytes(buf, index, length);
            }
        }
        return copy;
    }
    
    static int setBytes(final AbstractByteBuf buf, final long addr, final int index, final InputStream in, final int length) throws IOException {
        buf.checkIndex(index, length);
        final ByteBuf tmpBuf = buf.alloc().heapBuffer(length);
        try {
            final byte[] tmp = tmpBuf.array();
            final int offset = tmpBuf.arrayOffset();
            final int readBytes = in.read(tmp, offset, length);
            if (readBytes > 0) {
                PlatformDependent.copyMemory(tmp, offset, addr, readBytes);
            }
            return readBytes;
        }
        finally {
            tmpBuf.release();
        }
    }
    
    static void getBytes(final AbstractByteBuf buf, final long addr, final int index, final ByteBuf dst, final int dstIndex, final int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(dst, "dst");
        if (MathUtil.isOutOfBounds(dstIndex, length, dst.capacity())) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(addr, dst.memoryAddress() + dstIndex, length);
        }
        else if (dst.hasArray()) {
            PlatformDependent.copyMemory(addr, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else {
            dst.setBytes(dstIndex, buf, index, length);
        }
    }
    
    static void getBytes(final AbstractByteBuf buf, final long addr, final int index, final byte[] dst, final int dstIndex, final int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(dst, "dst");
        if (MathUtil.isOutOfBounds(dstIndex, length, dst.length)) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (length != 0) {
            PlatformDependent.copyMemory(addr, dst, dstIndex, length);
        }
    }
    
    static void getBytes(final AbstractByteBuf buf, final long addr, final int index, final ByteBuffer dst) {
        buf.checkIndex(index, dst.remaining());
        if (dst.remaining() == 0) {
            return;
        }
        if (dst.isDirect()) {
            if (dst.isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
            final long dstAddress = PlatformDependent.directBufferAddress(dst);
            PlatformDependent.copyMemory(addr, dstAddress + dst.position(), dst.remaining());
            dst.position(dst.position() + dst.remaining());
        }
        else if (dst.hasArray()) {
            PlatformDependent.copyMemory(addr, dst.array(), dst.arrayOffset() + dst.position(), dst.remaining());
            dst.position(dst.position() + dst.remaining());
        }
        else {
            dst.put(buf.nioBuffer());
        }
    }
    
    static void setBytes(final AbstractByteBuf buf, final long addr, final int index, final ByteBuf src, final int srcIndex, final int length) {
        buf.checkIndex(index, length);
        ObjectUtil.checkNotNull(src, "src");
        if (MathUtil.isOutOfBounds(srcIndex, length, src.capacity())) {
            throw new IndexOutOfBoundsException("srcIndex: " + srcIndex);
        }
        if (length != 0) {
            if (src.hasMemoryAddress()) {
                PlatformDependent.copyMemory(src.memoryAddress() + srcIndex, addr, length);
            }
            else if (src.hasArray()) {
                PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcIndex, addr, length);
            }
            else {
                src.getBytes(srcIndex, buf, index, length);
            }
        }
    }
    
    static void setBytes(final AbstractByteBuf buf, final long addr, final int index, final byte[] src, final int srcIndex, final int length) {
        buf.checkIndex(index, length);
        if (length != 0) {
            PlatformDependent.copyMemory(src, srcIndex, addr, length);
        }
    }
    
    static void setBytes(final AbstractByteBuf buf, final long addr, final int index, final ByteBuffer src) {
        buf.checkIndex(index, src.remaining());
        final int length = src.remaining();
        if (length == 0) {
            return;
        }
        if (src.isDirect()) {
            final long srcAddress = PlatformDependent.directBufferAddress(src);
            PlatformDependent.copyMemory(srcAddress + src.position(), addr, src.remaining());
            src.position(src.position() + length);
        }
        else if (src.hasArray()) {
            PlatformDependent.copyMemory(src.array(), src.arrayOffset() + src.position(), addr, length);
            src.position(src.position() + length);
        }
        else {
            final ByteBuf tmpBuf = buf.alloc().heapBuffer(length);
            try {
                final byte[] tmp = tmpBuf.array();
                src.get(tmp, tmpBuf.arrayOffset(), length);
                PlatformDependent.copyMemory(tmp, tmpBuf.arrayOffset(), addr, length);
            }
            finally {
                tmpBuf.release();
            }
        }
    }
    
    static void getBytes(final AbstractByteBuf buf, final long addr, final int index, final OutputStream out, final int length) throws IOException {
        buf.checkIndex(index, length);
        if (length != 0) {
            final ByteBuf tmpBuf = buf.alloc().heapBuffer(length);
            try {
                final byte[] tmp = tmpBuf.array();
                final int offset = tmpBuf.arrayOffset();
                PlatformDependent.copyMemory(addr, tmp, offset, length);
                out.write(tmp, offset, length);
            }
            finally {
                tmpBuf.release();
            }
        }
    }
    
    static void setZero(final AbstractByteBuf buf, final long addr, final int index, final int length) {
        if (length == 0) {
            return;
        }
        buf.checkIndex(index, length);
        PlatformDependent.setMemory(addr, length, (byte)0);
    }
    
    static UnpooledUnsafeDirectByteBuf newUnsafeDirectByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        if (PlatformDependent.useDirectBufferNoCleaner()) {
            return new UnpooledUnsafeNoCleanerDirectByteBuf(alloc, initialCapacity, maxCapacity);
        }
        return new UnpooledUnsafeDirectByteBuf(alloc, initialCapacity, maxCapacity);
    }
    
    private UnsafeByteBufUtil() {
    }
    
    static {
        UNALIGNED = PlatformDependent.isUnaligned();
    }
}
