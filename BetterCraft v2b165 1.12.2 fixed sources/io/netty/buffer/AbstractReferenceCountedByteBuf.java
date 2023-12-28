// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCountedByteBuf extends AbstractByteBuf
{
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater;
    private volatile int refCnt;
    
    protected AbstractReferenceCountedByteBuf(final int maxCapacity) {
        super(maxCapacity);
        this.refCnt = 1;
    }
    
    @Override
    public int refCnt() {
        return this.refCnt;
    }
    
    protected final void setRefCnt(final int refCnt) {
        this.refCnt = refCnt;
    }
    
    @Override
    public ByteBuf retain() {
        return this.retain0(1);
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        return this.retain0(ObjectUtil.checkPositive(increment, "increment"));
    }
    
    private ByteBuf retain0(final int increment) {
        while (true) {
            final int refCnt = this.refCnt;
            final int nextCnt = refCnt + increment;
            if (nextCnt <= increment) {
                throw new IllegalReferenceCountException(refCnt, increment);
            }
            if (AbstractReferenceCountedByteBuf.refCntUpdater.compareAndSet(this, refCnt, nextCnt)) {
                return this;
            }
        }
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
    public boolean release() {
        return this.release0(1);
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.release0(ObjectUtil.checkPositive(decrement, "decrement"));
    }
    
    private boolean release0(final int decrement) {
        while (true) {
            final int refCnt = this.refCnt;
            if (refCnt < decrement) {
                throw new IllegalReferenceCountException(refCnt, -decrement);
            }
            if (!AbstractReferenceCountedByteBuf.refCntUpdater.compareAndSet(this, refCnt, refCnt - decrement)) {
                continue;
            }
            if (refCnt == decrement) {
                this.deallocate();
                return true;
            }
            return false;
        }
    }
    
    protected abstract void deallocate();
    
    static {
        refCntUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
    }
}
