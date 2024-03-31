// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

class UnpooledSlicedByteBuf extends AbstractUnpooledSlicedByteBuf
{
    UnpooledSlicedByteBuf(final AbstractByteBuf buffer, final int index, final int length) {
        super(buffer, index, length);
    }
    
    @Override
    public int capacity() {
        return this.maxCapacity();
    }
    
    @Override
    public AbstractByteBuf unwrap() {
        return (AbstractByteBuf)super.unwrap();
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.unwrap()._getByte(this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.unwrap()._getShort(this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return this.unwrap()._getShortLE(this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.unwrap()._getUnsignedMedium(this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return this.unwrap()._getUnsignedMediumLE(this.idx(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.unwrap()._getInt(this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return this.unwrap()._getIntLE(this.idx(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.unwrap()._getLong(this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return this.unwrap()._getLongLE(this.idx(index));
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.unwrap()._setByte(this.idx(index), value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.unwrap()._setShort(this.idx(index), value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        this.unwrap()._setShortLE(this.idx(index), value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.unwrap()._setMedium(this.idx(index), value);
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        this.unwrap()._setMediumLE(this.idx(index), value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.unwrap()._setInt(this.idx(index), value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        this.unwrap()._setIntLE(this.idx(index), value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.unwrap()._setLong(this.idx(index), value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        this.unwrap()._setLongLE(this.idx(index), value);
    }
}
