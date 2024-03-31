// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

class UnpooledDuplicatedByteBuf extends DuplicatedByteBuf
{
    UnpooledDuplicatedByteBuf(final AbstractByteBuf buffer) {
        super(buffer);
    }
    
    @Override
    public AbstractByteBuf unwrap() {
        return (AbstractByteBuf)super.unwrap();
    }
    
    @Override
    protected byte _getByte(final int index) {
        return this.unwrap()._getByte(index);
    }
    
    @Override
    protected short _getShort(final int index) {
        return this.unwrap()._getShort(index);
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return this.unwrap()._getShortLE(index);
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return this.unwrap()._getUnsignedMedium(index);
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return this.unwrap()._getUnsignedMediumLE(index);
    }
    
    @Override
    protected int _getInt(final int index) {
        return this.unwrap()._getInt(index);
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return this.unwrap()._getIntLE(index);
    }
    
    @Override
    protected long _getLong(final int index) {
        return this.unwrap()._getLong(index);
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return this.unwrap()._getLongLE(index);
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        this.unwrap()._setByte(index, value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        this.unwrap()._setShort(index, value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        this.unwrap()._setShortLE(index, value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        this.unwrap()._setMedium(index, value);
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        this.unwrap()._setMediumLE(index, value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        this.unwrap()._setInt(index, value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        this.unwrap()._setIntLE(index, value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        this.unwrap()._setLong(index, value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        this.unwrap()._setLongLE(index, value);
    }
}
