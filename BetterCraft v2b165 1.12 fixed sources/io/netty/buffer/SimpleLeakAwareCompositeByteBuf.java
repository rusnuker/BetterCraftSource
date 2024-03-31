// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.nio.ByteOrder;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.ResourceLeakTracker;

class SimpleLeakAwareCompositeByteBuf extends WrappedCompositeByteBuf
{
    final ResourceLeakTracker<ByteBuf> leak;
    
    SimpleLeakAwareCompositeByteBuf(final CompositeByteBuf wrapped, final ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped);
        this.leak = ObjectUtil.checkNotNull(leak, "leak");
    }
    
    @Override
    public final boolean release() {
        final ByteBuf unwrapped = this.unwrap();
        if (super.release()) {
            this.closeLeak(unwrapped);
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean release(final int decrement) {
        final ByteBuf unwrapped = this.unwrap();
        if (super.release(decrement)) {
            this.closeLeak(unwrapped);
            return true;
        }
        return false;
    }
    
    private void closeLeak(final ByteBuf trackedByteBuf) {
        final boolean closed = this.leak.close(trackedByteBuf);
        assert closed;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (this.order() == endianness) {
            return this;
        }
        return this.newLeakAwareByteBuf(super.order(endianness));
    }
    
    @Override
    public ByteBuf slice() {
        return this.newLeakAwareByteBuf(super.slice());
    }
    
    @Override
    public ByteBuf retainedSlice() {
        return this.newLeakAwareByteBuf(super.retainedSlice());
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return this.newLeakAwareByteBuf(super.slice(index, length));
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        return this.newLeakAwareByteBuf(super.retainedSlice(index, length));
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.newLeakAwareByteBuf(super.duplicate());
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        return this.newLeakAwareByteBuf(super.retainedDuplicate());
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return this.newLeakAwareByteBuf(super.readSlice(length));
    }
    
    @Override
    public ByteBuf readRetainedSlice(final int length) {
        return this.newLeakAwareByteBuf(super.readRetainedSlice(length));
    }
    
    @Override
    public ByteBuf asReadOnly() {
        return this.newLeakAwareByteBuf(super.asReadOnly());
    }
    
    private SimpleLeakAwareByteBuf newLeakAwareByteBuf(final ByteBuf wrapped) {
        return this.newLeakAwareByteBuf(wrapped, this.unwrap(), this.leak);
    }
    
    protected SimpleLeakAwareByteBuf newLeakAwareByteBuf(final ByteBuf wrapped, final ByteBuf trackedByteBuf, final ResourceLeakTracker<ByteBuf> leakTracker) {
        return new SimpleLeakAwareByteBuf(wrapped, trackedByteBuf, leakTracker);
    }
}
