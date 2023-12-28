// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;

final class UnsafeHeapSwappedByteBuf extends AbstractUnsafeSwappedByteBuf
{
    UnsafeHeapSwappedByteBuf(final AbstractByteBuf buf) {
        super(buf);
    }
    
    private static int idx(final ByteBuf wrapped, final int index) {
        return wrapped.arrayOffset() + index;
    }
    
    @Override
    protected long _getLong(final AbstractByteBuf wrapped, final int index) {
        return PlatformDependent.getLong(wrapped.array(), idx(wrapped, index));
    }
    
    @Override
    protected int _getInt(final AbstractByteBuf wrapped, final int index) {
        return PlatformDependent.getInt(wrapped.array(), idx(wrapped, index));
    }
    
    @Override
    protected short _getShort(final AbstractByteBuf wrapped, final int index) {
        return PlatformDependent.getShort(wrapped.array(), idx(wrapped, index));
    }
    
    @Override
    protected void _setShort(final AbstractByteBuf wrapped, final int index, final short value) {
        PlatformDependent.putShort(wrapped.array(), idx(wrapped, index), value);
    }
    
    @Override
    protected void _setInt(final AbstractByteBuf wrapped, final int index, final int value) {
        PlatformDependent.putInt(wrapped.array(), idx(wrapped, index), value);
    }
    
    @Override
    protected void _setLong(final AbstractByteBuf wrapped, final int index, final long value) {
        PlatformDependent.putLong(wrapped.array(), idx(wrapped, index), value);
    }
}
