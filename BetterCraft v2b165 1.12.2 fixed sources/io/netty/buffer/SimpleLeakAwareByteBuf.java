// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteOrder;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.ResourceLeakTracker;

class SimpleLeakAwareByteBuf extends WrappedByteBuf
{
    private final ByteBuf trackedByteBuf;
    final ResourceLeakTracker<ByteBuf> leak;
    
    SimpleLeakAwareByteBuf(final ByteBuf wrapped, final ByteBuf trackedByteBuf, final ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped);
        this.trackedByteBuf = ObjectUtil.checkNotNull(trackedByteBuf, "trackedByteBuf");
        this.leak = ObjectUtil.checkNotNull(leak, "leak");
    }
    
    SimpleLeakAwareByteBuf(final ByteBuf wrapped, final ResourceLeakTracker<ByteBuf> leak) {
        this(wrapped, wrapped, leak);
    }
    
    @Override
    public ByteBuf slice() {
        return this.newSharedLeakAwareByteBuf(super.slice());
    }
    
    @Override
    public ByteBuf retainedSlice() {
        return this.unwrappedDerived(super.retainedSlice());
    }
    
    @Override
    public ByteBuf retainedSlice(final int index, final int length) {
        return this.unwrappedDerived(super.retainedSlice(index, length));
    }
    
    @Override
    public ByteBuf retainedDuplicate() {
        return this.unwrappedDerived(super.retainedDuplicate());
    }
    
    @Override
    public ByteBuf readRetainedSlice(final int length) {
        return this.unwrappedDerived(super.readRetainedSlice(length));
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return this.newSharedLeakAwareByteBuf(super.slice(index, length));
    }
    
    @Override
    public ByteBuf duplicate() {
        return this.newSharedLeakAwareByteBuf(super.duplicate());
    }
    
    @Override
    public ByteBuf readSlice(final int length) {
        return this.newSharedLeakAwareByteBuf(super.readSlice(length));
    }
    
    @Override
    public ByteBuf asReadOnly() {
        return this.newSharedLeakAwareByteBuf(super.asReadOnly());
    }
    
    @Override
    public ByteBuf touch() {
        return this;
    }
    
    @Override
    public ByteBuf touch(final Object hint) {
        return this;
    }
    
    @Override
    public final boolean release() {
        if (super.release()) {
            this.closeLeak();
            return true;
        }
        return false;
    }
    
    @Override
    public final boolean release(final int decrement) {
        if (super.release(decrement)) {
            this.closeLeak();
            return true;
        }
        return false;
    }
    
    private void closeLeak() {
        final boolean closed = this.leak.close(this.trackedByteBuf);
        assert closed;
    }
    
    @Override
    public ByteBuf order(final ByteOrder endianness) {
        if (this.order() == endianness) {
            return this;
        }
        return this.newSharedLeakAwareByteBuf(super.order(endianness));
    }
    
    private ByteBuf unwrappedDerived(final ByteBuf derived) {
        final ByteBuf unwrappedDerived = unwrapSwapped(derived);
        if (!(unwrappedDerived instanceof AbstractPooledDerivedByteBuf)) {
            return this.newSharedLeakAwareByteBuf(derived);
        }
        ((AbstractPooledDerivedByteBuf)unwrappedDerived).parent(this);
        final ResourceLeakTracker<ByteBuf> newLeak = AbstractByteBuf.leakDetector.track(derived);
        if (newLeak == null) {
            return derived;
        }
        return this.newLeakAwareByteBuf(derived, newLeak);
    }
    
    private static ByteBuf unwrapSwapped(ByteBuf buf) {
        if (buf instanceof SwappedByteBuf) {
            do {
                buf = buf.unwrap();
            } while (buf instanceof SwappedByteBuf);
            return buf;
        }
        return buf;
    }
    
    private SimpleLeakAwareByteBuf newSharedLeakAwareByteBuf(final ByteBuf wrapped) {
        return this.newLeakAwareByteBuf(wrapped, this.trackedByteBuf, this.leak);
    }
    
    private SimpleLeakAwareByteBuf newLeakAwareByteBuf(final ByteBuf wrapped, final ResourceLeakTracker<ByteBuf> leakTracker) {
        return this.newLeakAwareByteBuf(wrapped, wrapped, leakTracker);
    }
    
    protected SimpleLeakAwareByteBuf newLeakAwareByteBuf(final ByteBuf buf, final ByteBuf trackedByteBuf, final ResourceLeakTracker<ByteBuf> leakTracker) {
        return new SimpleLeakAwareByteBuf(buf, trackedByteBuf, leakTracker);
    }
}
