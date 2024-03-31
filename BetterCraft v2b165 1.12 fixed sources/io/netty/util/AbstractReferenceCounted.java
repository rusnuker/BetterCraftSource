// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCounted implements ReferenceCounted
{
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> refCntUpdater;
    private volatile int refCnt;
    
    public AbstractReferenceCounted() {
        this.refCnt = 1;
    }
    
    @Override
    public final int refCnt() {
        return this.refCnt;
    }
    
    protected final void setRefCnt(final int refCnt) {
        this.refCnt = refCnt;
    }
    
    @Override
    public ReferenceCounted retain() {
        return this.retain0(1);
    }
    
    @Override
    public ReferenceCounted retain(final int increment) {
        return this.retain0(ObjectUtil.checkPositive(increment, "increment"));
    }
    
    private ReferenceCounted retain0(final int increment) {
        while (true) {
            final int refCnt = this.refCnt;
            final int nextCnt = refCnt + increment;
            if (nextCnt <= increment) {
                throw new IllegalReferenceCountException(refCnt, increment);
            }
            if (AbstractReferenceCounted.refCntUpdater.compareAndSet(this, refCnt, nextCnt)) {
                return this;
            }
        }
    }
    
    @Override
    public ReferenceCounted touch() {
        return this.touch(null);
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
            if (!AbstractReferenceCounted.refCntUpdater.compareAndSet(this, refCnt, refCnt - decrement)) {
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
        refCntUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt");
    }
}
