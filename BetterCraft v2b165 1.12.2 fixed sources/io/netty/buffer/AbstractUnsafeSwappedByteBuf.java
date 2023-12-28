// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import io.netty.util.internal.PlatformDependent;

abstract class AbstractUnsafeSwappedByteBuf extends SwappedByteBuf
{
    private final boolean nativeByteOrder;
    private final AbstractByteBuf wrapped;
    
    AbstractUnsafeSwappedByteBuf(final AbstractByteBuf buf) {
        super(buf);
        assert PlatformDependent.isUnaligned();
        this.wrapped = buf;
        this.nativeByteOrder = (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER == (this.order() == ByteOrder.BIG_ENDIAN));
    }
    
    @Override
    public final long getLong(final int index) {
        this.wrapped.checkIndex(index, 8);
        final long v = this._getLong(this.wrapped, index);
        return this.nativeByteOrder ? v : Long.reverseBytes(v);
    }
    
    @Override
    public final float getFloat(final int index) {
        return Float.intBitsToFloat(this.getInt(index));
    }
    
    @Override
    public final double getDouble(final int index) {
        return Double.longBitsToDouble(this.getLong(index));
    }
    
    @Override
    public final char getChar(final int index) {
        return (char)this.getShort(index);
    }
    
    @Override
    public final long getUnsignedInt(final int index) {
        return (long)this.getInt(index) & 0xFFFFFFFFL;
    }
    
    @Override
    public final int getInt(final int index) {
        this.wrapped.checkIndex0(index, 4);
        final int v = this._getInt(this.wrapped, index);
        return this.nativeByteOrder ? v : Integer.reverseBytes(v);
    }
    
    @Override
    public final int getUnsignedShort(final int index) {
        return this.getShort(index) & 0xFFFF;
    }
    
    @Override
    public final short getShort(final int index) {
        this.wrapped.checkIndex0(index, 2);
        final short v = this._getShort(this.wrapped, index);
        return this.nativeByteOrder ? v : Short.reverseBytes(v);
    }
    
    @Override
    public final ByteBuf setShort(final int index, final int value) {
        this.wrapped.checkIndex0(index, 2);
        this._setShort(this.wrapped, index, this.nativeByteOrder ? ((short)value) : Short.reverseBytes((short)value));
        return this;
    }
    
    @Override
    public final ByteBuf setInt(final int index, final int value) {
        this.wrapped.checkIndex0(index, 4);
        this._setInt(this.wrapped, index, this.nativeByteOrder ? value : Integer.reverseBytes(value));
        return this;
    }
    
    @Override
    public final ByteBuf setLong(final int index, final long value) {
        this.wrapped.checkIndex(index, 8);
        this._setLong(this.wrapped, index, this.nativeByteOrder ? value : Long.reverseBytes(value));
        return this;
    }
    
    @Override
    public final ByteBuf setChar(final int index, final int value) {
        this.setShort(index, value);
        return this;
    }
    
    @Override
    public final ByteBuf setFloat(final int index, final float value) {
        this.setInt(index, Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public final ByteBuf setDouble(final int index, final double value) {
        this.setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }
    
    @Override
    public final ByteBuf writeShort(final int value) {
        this.wrapped.ensureWritable(2);
        this._setShort(this.wrapped, this.wrapped.writerIndex, this.nativeByteOrder ? ((short)value) : Short.reverseBytes((short)value));
        final AbstractByteBuf wrapped = this.wrapped;
        wrapped.writerIndex += 2;
        return this;
    }
    
    @Override
    public final ByteBuf writeInt(final int value) {
        this.wrapped.ensureWritable(4);
        this._setInt(this.wrapped, this.wrapped.writerIndex, this.nativeByteOrder ? value : Integer.reverseBytes(value));
        final AbstractByteBuf wrapped = this.wrapped;
        wrapped.writerIndex += 4;
        return this;
    }
    
    @Override
    public final ByteBuf writeLong(final long value) {
        this.wrapped.ensureWritable(8);
        this._setLong(this.wrapped, this.wrapped.writerIndex, this.nativeByteOrder ? value : Long.reverseBytes(value));
        final AbstractByteBuf wrapped = this.wrapped;
        wrapped.writerIndex += 8;
        return this;
    }
    
    @Override
    public final ByteBuf writeChar(final int value) {
        this.writeShort(value);
        return this;
    }
    
    @Override
    public final ByteBuf writeFloat(final float value) {
        this.writeInt(Float.floatToRawIntBits(value));
        return this;
    }
    
    @Override
    public final ByteBuf writeDouble(final double value) {
        this.writeLong(Double.doubleToRawLongBits(value));
        return this;
    }
    
    protected abstract short _getShort(final AbstractByteBuf p0, final int p1);
    
    protected abstract int _getInt(final AbstractByteBuf p0, final int p1);
    
    protected abstract long _getLong(final AbstractByteBuf p0, final int p1);
    
    protected abstract void _setShort(final AbstractByteBuf p0, final int p1, final short p2);
    
    protected abstract void _setInt(final AbstractByteBuf p0, final int p1, final int p2);
    
    protected abstract void _setLong(final AbstractByteBuf p0, final int p1, final long p2);
}
