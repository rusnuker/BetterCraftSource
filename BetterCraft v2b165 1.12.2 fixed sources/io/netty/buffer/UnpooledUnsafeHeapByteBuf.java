// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

class UnpooledUnsafeHeapByteBuf extends UnpooledHeapByteBuf
{
    UnpooledUnsafeHeapByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        super(alloc, initialCapacity, maxCapacity);
    }
    
    @Override
    public byte getByte(final int index) {
        this.checkIndex(index);
        return this._getByte(index);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return UnsafeByteBufUtil.getByte(this.array, index);
    }
    
    @Override
    public short getShort(final int index) {
        this.checkIndex(index, 2);
        return this._getShort(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return UnsafeByteBufUtil.getShort(this.array, index);
    }
    
    @Override
    public short getShortLE(final int index) {
        this.checkIndex(index, 2);
        return this._getShortLE(index);
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return UnsafeByteBufUtil.getShortLE(this.array, index);
    }
    
    @Override
    public int getUnsignedMedium(final int index) {
        this.checkIndex(index, 3);
        return this._getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return UnsafeByteBufUtil.getUnsignedMedium(this.array, index);
    }
    
    @Override
    public int getUnsignedMediumLE(final int index) {
        this.checkIndex(index, 3);
        return this._getUnsignedMediumLE(index);
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return UnsafeByteBufUtil.getUnsignedMediumLE(this.array, index);
    }
    
    @Override
    public int getInt(final int index) {
        this.checkIndex(index, 4);
        return this._getInt(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return UnsafeByteBufUtil.getInt(this.array, index);
    }
    
    @Override
    public int getIntLE(final int index) {
        this.checkIndex(index, 4);
        return this._getIntLE(index);
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return UnsafeByteBufUtil.getIntLE(this.array, index);
    }
    
    @Override
    public long getLong(final int index) {
        this.checkIndex(index, 8);
        return this._getLong(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return UnsafeByteBufUtil.getLong(this.array, index);
    }
    
    @Override
    public long getLongLE(final int index) {
        this.checkIndex(index, 8);
        return this._getLongLE(index);
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return UnsafeByteBufUtil.getLongLE(this.array, index);
    }
    
    @Override
    public ByteBuf setByte(final int index, final int value) {
        this.checkIndex(index);
        this._setByte(index, value);
        return this;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        UnsafeByteBufUtil.setByte(this.array, index, value);
    }
    
    @Override
    public ByteBuf setShort(final int index, final int value) {
        this.checkIndex(index, 2);
        this._setShort(index, value);
        return this;
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        UnsafeByteBufUtil.setShort(this.array, index, value);
    }
    
    @Override
    public ByteBuf setShortLE(final int index, final int value) {
        this.checkIndex(index, 2);
        this._setShortLE(index, value);
        return this;
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        UnsafeByteBufUtil.setShortLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setMedium(final int index, final int value) {
        this.checkIndex(index, 3);
        this._setMedium(index, value);
        return this;
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        UnsafeByteBufUtil.setMedium(this.array, index, value);
    }
    
    @Override
    public ByteBuf setMediumLE(final int index, final int value) {
        this.checkIndex(index, 3);
        this._setMediumLE(index, value);
        return this;
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        UnsafeByteBufUtil.setMediumLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setInt(final int index, final int value) {
        this.checkIndex(index, 4);
        this._setInt(index, value);
        return this;
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        UnsafeByteBufUtil.setInt(this.array, index, value);
    }
    
    @Override
    public ByteBuf setIntLE(final int index, final int value) {
        this.checkIndex(index, 4);
        this._setIntLE(index, value);
        return this;
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        UnsafeByteBufUtil.setIntLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setLong(final int index, final long value) {
        this.checkIndex(index, 8);
        this._setLong(index, value);
        return this;
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        UnsafeByteBufUtil.setLong(this.array, index, value);
    }
    
    @Override
    public ByteBuf setLongLE(final int index, final long value) {
        this.checkIndex(index, 8);
        this._setLongLE(index, value);
        return this;
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        UnsafeByteBufUtil.setLongLE(this.array, index, value);
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        if (PlatformDependent.javaVersion() >= 7) {
            this._setZero(index, length);
            return this;
        }
        return super.setZero(index, length);
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        if (PlatformDependent.javaVersion() >= 7) {
            this.ensureWritable(length);
            final int wIndex = this.writerIndex;
            this._setZero(wIndex, length);
            this.writerIndex = wIndex + length;
            return this;
        }
        return super.writeZero(length);
    }
    
    private void _setZero(final int index, final int length) {
        this.checkIndex(index, length);
        UnsafeByteBufUtil.setZero(this.array, index, length);
    }
    
    @Deprecated
    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        if (PlatformDependent.isUnaligned()) {
            return new UnsafeHeapSwappedByteBuf(this);
        }
        return super.newSwappedByteBuf();
    }
}
