// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

final class HeapByteBufUtil
{
    static byte getByte(final byte[] memory, final int index) {
        return memory[index];
    }
    
    static short getShort(final byte[] memory, final int index) {
        return (short)(memory[index] << 8 | (memory[index + 1] & 0xFF));
    }
    
    static short getShortLE(final byte[] memory, final int index) {
        return (short)((memory[index] & 0xFF) | memory[index + 1] << 8);
    }
    
    static int getUnsignedMedium(final byte[] memory, final int index) {
        return (memory[index] & 0xFF) << 16 | (memory[index + 1] & 0xFF) << 8 | (memory[index + 2] & 0xFF);
    }
    
    static int getUnsignedMediumLE(final byte[] memory, final int index) {
        return (memory[index] & 0xFF) | (memory[index + 1] & 0xFF) << 8 | (memory[index + 2] & 0xFF) << 16;
    }
    
    static int getInt(final byte[] memory, final int index) {
        return (memory[index] & 0xFF) << 24 | (memory[index + 1] & 0xFF) << 16 | (memory[index + 2] & 0xFF) << 8 | (memory[index + 3] & 0xFF);
    }
    
    static int getIntLE(final byte[] memory, final int index) {
        return (memory[index] & 0xFF) | (memory[index + 1] & 0xFF) << 8 | (memory[index + 2] & 0xFF) << 16 | (memory[index + 3] & 0xFF) << 24;
    }
    
    static long getLong(final byte[] memory, final int index) {
        return ((long)memory[index] & 0xFFL) << 56 | ((long)memory[index + 1] & 0xFFL) << 48 | ((long)memory[index + 2] & 0xFFL) << 40 | ((long)memory[index + 3] & 0xFFL) << 32 | ((long)memory[index + 4] & 0xFFL) << 24 | ((long)memory[index + 5] & 0xFFL) << 16 | ((long)memory[index + 6] & 0xFFL) << 8 | ((long)memory[index + 7] & 0xFFL);
    }
    
    static long getLongLE(final byte[] memory, final int index) {
        return ((long)memory[index] & 0xFFL) | ((long)memory[index + 1] & 0xFFL) << 8 | ((long)memory[index + 2] & 0xFFL) << 16 | ((long)memory[index + 3] & 0xFFL) << 24 | ((long)memory[index + 4] & 0xFFL) << 32 | ((long)memory[index + 5] & 0xFFL) << 40 | ((long)memory[index + 6] & 0xFFL) << 48 | ((long)memory[index + 7] & 0xFFL) << 56;
    }
    
    static void setByte(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)value;
    }
    
    static void setShort(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)(value >>> 8);
        memory[index + 1] = (byte)value;
    }
    
    static void setShortLE(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)value;
        memory[index + 1] = (byte)(value >>> 8);
    }
    
    static void setMedium(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)(value >>> 16);
        memory[index + 1] = (byte)(value >>> 8);
        memory[index + 2] = (byte)value;
    }
    
    static void setMediumLE(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)value;
        memory[index + 1] = (byte)(value >>> 8);
        memory[index + 2] = (byte)(value >>> 16);
    }
    
    static void setInt(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)(value >>> 24);
        memory[index + 1] = (byte)(value >>> 16);
        memory[index + 2] = (byte)(value >>> 8);
        memory[index + 3] = (byte)value;
    }
    
    static void setIntLE(final byte[] memory, final int index, final int value) {
        memory[index] = (byte)value;
        memory[index + 1] = (byte)(value >>> 8);
        memory[index + 2] = (byte)(value >>> 16);
        memory[index + 3] = (byte)(value >>> 24);
    }
    
    static void setLong(final byte[] memory, final int index, final long value) {
        memory[index] = (byte)(value >>> 56);
        memory[index + 1] = (byte)(value >>> 48);
        memory[index + 2] = (byte)(value >>> 40);
        memory[index + 3] = (byte)(value >>> 32);
        memory[index + 4] = (byte)(value >>> 24);
        memory[index + 5] = (byte)(value >>> 16);
        memory[index + 6] = (byte)(value >>> 8);
        memory[index + 7] = (byte)value;
    }
    
    static void setLongLE(final byte[] memory, final int index, final long value) {
        memory[index] = (byte)value;
        memory[index + 1] = (byte)(value >>> 8);
        memory[index + 2] = (byte)(value >>> 16);
        memory[index + 3] = (byte)(value >>> 24);
        memory[index + 4] = (byte)(value >>> 32);
        memory[index + 5] = (byte)(value >>> 40);
        memory[index + 6] = (byte)(value >>> 48);
        memory[index + 7] = (byte)(value >>> 56);
    }
    
    private HeapByteBufUtil() {
    }
}
