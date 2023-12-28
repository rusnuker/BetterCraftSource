// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Deprecated
public class PromiseAggregator<V, F extends Future<V>> implements GenericFutureListener<F>
{
    private final Promise<?> aggregatePromise;
    private final boolean failPending;
    private Set<Promise<V>> pendingPromises;
    
    public PromiseAggregator(final Promise<Void> aggregatePromise, final boolean failPending) {
        if (aggregatePromise == null) {
            throw new NullPointerException("aggregatePromise");
        }
        this.aggregatePromise = aggregatePromise;
        this.failPending = failPending;
    }
    
    public PromiseAggregator(final Promise<Void> aggregatePromise) {
        this(aggregatePromise, true);
    }
    
    @SafeVarargs
    public final PromiseAggregator<V, F> add(final Promise<V>... promises) {
        if (promises == null) {
            throw new NullPointerException("promises");
        }
        if (promises.length == 0) {
            return this;
        }
        synchronized (this) {
            if (this.pendingPromises == null) {
                int size;
                if (promises.length > 1) {
                    size = promises.length;
                }
                else {
                    size = 2;
                }
                this.pendingPromises = new LinkedHashSet<Promise<V>>(size);
            }
            for (final Promise<V> p : promises) {
                if (p != null) {
                    this.pendingPromises.add(p);
                    p.addListener((GenericFutureListener<? extends Future<? super V>>)this);
                }
            }
        }
        return this;
    }
    
    @Override
    public synchronized void operationComplete(final F future) throws Exception {
        if (this.pendingPromises == null) {
            this.aggregatePromise.setSuccess(null);
        }
        else {
            this.pendingPromises.remove(future);
            if (!future.isSuccess()) {
                final Throwable cause = future.cause();
                this.aggregatePromise.setFailure(cause);
                if (this.failPending) {
                    for (final Promise<V> pendingFuture : this.pendingPromises) {
                        pendingFuture.setFailure(cause);
                    }
                }
            }
            else if (this.pendingPromises.isEmpty()) {
                this.aggregatePromise.setSuccess(null);
            }
        }
    }
}
