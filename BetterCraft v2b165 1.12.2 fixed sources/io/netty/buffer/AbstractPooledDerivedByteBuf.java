// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.util.Recycler;

abstract class AbstractPooledDerivedByteBuf extends AbstractReferenceCountedByteBuf
{
    private final Recycler.Handle<AbstractPooledDerivedByteBuf> recyclerHandle;
    private AbstractByteBuf rootParent;
    private ByteBuf parent;
    
    AbstractPooledDerivedByteBuf(final Recycler.Handle<? extends AbstractPooledDerivedByteBuf> recyclerHandle) {
        super(0);
        this.recyclerHandle = (Recycler.Handle<AbstractPooledDerivedByteBuf>)recyclerHandle;
    }
    
    final void parent(final ByteBuf newParent) {
        assert newParent instanceof SimpleLeakAwareByteBuf;
        this.parent = newParent;
    }
    
    @Override
    public final AbstractByteBuf unwrap() {
        return this.rootParent;
    }
    
    final <U extends AbstractPooledDerivedByteBuf> U init(final AbstractByteBuf unwrapped, ByteBuf wrapped, final int readerIndex, final int writerIndex, final int maxCapacity) {
        wrapped.retain();
        this.parent = wrapped;
        this.rootParent = unwrapped;
        try {
            this.maxCapacity(maxCapacity);
            this.setIndex0(readerIndex, writerIndex);
            this.setRefCnt(1);
            final U castThis = (U)this;
            wrapped = null;
            return castThis;
        }
        finally {
            if (wrapped != null) {
                final AbstractByteBuf abstractByteBuf = null;
                this.rootParent = abstractByteBuf;
                this.parent = abstractByteBuf;
                wrapped.release();
            }
        }
    }
    
    @Override
    protected final void deallocate() {
        final ByteBuf parent = this.parent;
        this.recyclerHandle.recycle(this);
        parent.release();
    }
    
    @Override
    public final ByteBufAllocator alloc() {
        return this.unwrap().alloc();
    }
    
    @Deprecated
    @Override
    public final ByteOrder order() {
        return this.unwrap().order();
    }
    
    @Override
    public boolean isReadOnly() {
        return this.unwrap().isReadOnly();
    }
    
    @Override
    public final boolean isDirect() {
        return this.unwrap().isDirect();
    }
    
    @Override
    public boolean hasArray() {
        return this.unwrap().hasArray();
    }
    
    @Override
    public byte[] array() {
        return this.unwrap().array();
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return this.unwrap().hasMemoryAddress();
    }
    
    @Override
    public final int nioBufferCount() {
        return this.unwrap().nioBufferCount();
    }
    
    @Override
    public final ByteBuffer internalNioBuffer(final int index, final int length) {
        return this.nioBuffer(index, length);
    }
    
    @Override
    public final ByteBuf retainedSlice() {
        final int index = this.readerIndex();
        return this.retainedSlice(index, this.writerIndex() - index);
    }
    
    @Override
    public ByteBuf slice(final int index, final int length) {
        return new PooledNonRetainedSlicedByteBuf(this, this.unwrap(), index, length);
    }
    
    final ByteBuf duplicate0() {
        return new PooledNonRetainedDuplicateByteBuf(this, this.unwrap());
    }
    
    private static final class PooledNonRetainedDuplicateByteBuf extends UnpooledDuplicatedByteBuf
    {
        private final ReferenceCounted referenceCountDelegate;
        
        PooledNonRetainedDuplicateByteBuf(final ReferenceCounted referenceCountDelegate, final AbstractByteBuf buffer) {
            super(buffer);
            this.referenceCountDelegate = referenceCountDelegate;
        }
        
        @Override
        int refCnt0() {
            return this.referenceCountDelegate.refCnt();
        }
        
        @Override
        ByteBuf retain0() {
            this.referenceCountDelegate.retain();
            return this;
        }
        
        @Override
        ByteBuf retain0(final int increment) {
            this.referenceCountDelegate.retain(increment);
            return this;
        }
        
        @Override
        ByteBuf touch0() {
            this.referenceCountDelegate.touch();
            return this;
        }
        
        @Override
        ByteBuf touch0(final Object hint) {
            this.referenceCountDelegate.touch(hint);
            return this;
        }
        
        @Override
        boolean release0() {
            return this.referenceCountDelegate.release();
        }
        
        @Override
        boolean release0(final int decrement) {
            return this.referenceCountDelegate.release(decrement);
        }
        
        @Override
        public ByteBuf duplicate() {
            return new PooledNonRetainedDuplicateByteBuf(this.referenceCountDelegate, this);
        }
        
        @Override
        public ByteBuf retainedDuplicate() {
            return PooledDuplicatedByteBuf.newInstance(this.unwrap(), this, this.readerIndex(), this.writerIndex());
        }
        
        @Override
        public ByteBuf slice(final int index, final int length) {
            this.checkIndex0(index, length);
            return new PooledNonRetainedSlicedByteBuf(this.referenceCountDelegate, this.unwrap(), index, length);
        }
        
        @Override
        public ByteBuf retainedSlice() {
            return this.retainedSlice(this.readerIndex(), this.capacity());
        }
        
        @Override
        public ByteBuf retainedSlice(final int index, final int length) {
            return PooledSlicedByteBuf.newInstance(this.unwrap(), this, index, length);
        }
    }
    
    private static final class PooledNonRetainedSlicedByteBuf extends UnpooledSlicedByteBuf
    {
        private final ReferenceCounted referenceCountDelegate;
        
        PooledNonRetainedSlicedByteBuf(final ReferenceCounted referenceCountDelegate, final AbstractByteBuf buffer, final int index, final int length) {
            super(buffer, index, length);
            this.referenceCountDelegate = referenceCountDelegate;
        }
        
        @Override
        int refCnt0() {
            return this.referenceCountDelegate.refCnt();
        }
        
        @Override
        ByteBuf retain0() {
            this.referenceCountDelegate.retain();
            return this;
        }
        
        @Override
        ByteBuf retain0(final int increment) {
            this.referenceCountDelegate.retain(increment);
            return this;
        }
        
        @Override
        ByteBuf touch0() {
            this.referenceCountDelegate.touch();
            return this;
        }
        
        @Override
        ByteBuf touch0(final Object hint) {
            this.referenceCountDelegate.touch(hint);
            return this;
        }
        
        @Override
        boolean release0() {
            return this.referenceCountDelegate.release();
        }
        
        @Override
        boolean release0(final int decrement) {
            return this.referenceCountDelegate.release(decrement);
        }
        
        @Override
        public ByteBuf duplicate() {
            return new PooledNonRetainedDuplicateByteBuf(this.referenceCountDelegate, this.unwrap()).setIndex(this.idx(this.readerIndex()), this.idx(this.writerIndex()));
        }
        
        @Override
        public ByteBuf retainedDuplicate() {
            return PooledDuplicatedByteBuf.newInstance(this.unwrap(), this, this.idx(this.readerIndex()), this.idx(this.writerIndex()));
        }
        
        @Override
        public ByteBuf slice(final int index, final int length) {
            this.checkIndex0(index, length);
            return new PooledNonRetainedSlicedByteBuf(this.referenceCountDelegate, this.unwrap(), this.idx(index), length);
        }
        
        @Override
        public ByteBuf retainedSlice() {
            return this.retainedSlice(0, this.capacity());
        }
        
        @Override
        public ByteBuf retainedSlice(final int index, final int length) {
            return PooledSlicedByteBuf.newInstance(this.unwrap(), this, this.idx(index), length);
        }
    }
}
