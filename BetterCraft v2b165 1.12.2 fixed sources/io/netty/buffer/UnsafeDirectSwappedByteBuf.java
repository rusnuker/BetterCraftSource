// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

final class UnsafeDirectSwappedByteBuf extends AbstractUnsafeSwappedByteBuf
{
    UnsafeDirectSwappedByteBuf(final AbstractByteBuf buf) {
        super(buf);
    }
    
    private static long addr(final AbstractByteBuf wrapped, final int index) {
        return wrapped.memoryAddress() + index;
    }
    
    @Override
    protected long _getLong(final AbstractByteBuf wrapped, final int index) {
        return PlatformDependent.getLong(addr(wrapped, index));
    }
    
    @Override
    protected int _getInt(final AbstractByteBuf wrapped, final int index) {
        return PlatformDependent.getInt(addr(wrapped, index));
    }
    
    @Override
    protected short _getShort(final AbstractByteBuf wrapped, final int index) {
        return PlatformDependent.getShort(addr(wrapped, index));
    }
    
    @Override
    protected void _setShort(final AbstractByteBuf wrapped, final int index, final short value) {
        PlatformDependent.putShort(addr(wrapped, index), value);
    }
    
    @Override
    protected void _setInt(final AbstractByteBuf wrapped, final int index, final int value) {
        PlatformDependent.putInt(addr(wrapped, index), value);
    }
    
    @Override
    protected void _setLong(final AbstractByteBuf wrapped, final int index, final long value) {
        PlatformDependent.putLong(addr(wrapped, index), value);
    }
}
