// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import io.netty.util.internal.ObjectUtil;

public final class PromiseCombiner
{
    private int expectedCount;
    private int doneCount;
    private boolean doneAdding;
    private Promise<Void> aggregatePromise;
    private Throwable cause;
    private final GenericFutureListener<Future<?>> listener;
    
    public PromiseCombiner() {
        this.listener = new GenericFutureListener<Future<?>>() {
            @Override
            public void operationComplete(final Future<?> future) throws Exception {
                ++PromiseCombiner.this.doneCount;
                if (!future.isSuccess() && PromiseCombiner.this.cause == null) {
                    PromiseCombiner.this.cause = future.cause();
                }
                if (PromiseCombiner.this.doneCount == PromiseCombiner.this.expectedCount && PromiseCombiner.this.doneAdding) {
                    PromiseCombiner.this.tryPromise();
                }
            }
        };
    }
    
    @Deprecated
    public void add(final Promise promise) {
        this.add((Future)promise);
    }
    
    public void add(final Future future) {
        this.checkAddAllowed();
        ++this.expectedCount;
        future.addListener(this.listener);
    }
    
    @Deprecated
    public void addAll(final Promise... promises) {
        this.addAll((Future[])promises);
    }
    
    public void addAll(final Future... futures) {
        for (final Future future : futures) {
            this.add(future);
        }
    }
    
    public void finish(final Promise<Void> aggregatePromise) {
        if (this.doneAdding) {
            throw new IllegalStateException("Already finished");
        }
        this.doneAdding = true;
        this.aggregatePromise = ObjectUtil.checkNotNull(aggregatePromise, "aggregatePromise");
        if (this.doneCount == this.expectedCount) {
            this.tryPromise();
        }
    }
    
    private boolean tryPromise() {
        return (this.cause == null) ? this.aggregatePromise.trySuccess(null) : this.aggregatePromise.tryFailure(this.cause);
    }
    
    private void checkAddAllowed() {
        if (this.doneAdding) {
            throw new IllegalStateException("Adding promises is not allowed after finished adding");
        }
    }
}
