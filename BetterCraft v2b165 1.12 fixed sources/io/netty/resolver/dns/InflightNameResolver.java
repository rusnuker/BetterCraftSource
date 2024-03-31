// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import java.util.List;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.concurrent.EventExecutor;
import io.netty.resolver.NameResolver;

final class InflightNameResolver<T> implements NameResolver<T>
{
    private final EventExecutor executor;
    private final NameResolver<T> delegate;
    private final ConcurrentMap<String, Promise<T>> resolvesInProgress;
    private final ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress;
    
    InflightNameResolver(final EventExecutor executor, final NameResolver<T> delegate, final ConcurrentMap<String, Promise<T>> resolvesInProgress, final ConcurrentMap<String, Promise<List<T>>> resolveAllsInProgress) {
        this.executor = ObjectUtil.checkNotNull(executor, "executor");
        this.delegate = ObjectUtil.checkNotNull(delegate, "delegate");
        this.resolvesInProgress = ObjectUtil.checkNotNull(resolvesInProgress, "resolvesInProgress");
        this.resolveAllsInProgress = ObjectUtil.checkNotNull(resolveAllsInProgress, "resolveAllsInProgress");
    }
    
    @Override
    public Future<T> resolve(final String inetHost) {
        return this.resolve(inetHost, this.executor.newPromise());
    }
    
    @Override
    public Future<List<T>> resolveAll(final String inetHost) {
        return this.resolveAll(inetHost, this.executor.newPromise());
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
    
    @Override
    public Promise<T> resolve(final String inetHost, final Promise<T> promise) {
        return this.resolve(this.resolvesInProgress, inetHost, promise, false);
    }
    
    @Override
    public Promise<List<T>> resolveAll(final String inetHost, final Promise<List<T>> promise) {
        return this.resolve(this.resolveAllsInProgress, inetHost, promise, true);
    }
    
    private <U> Promise<U> resolve(final ConcurrentMap<String, Promise<U>> resolveMap, final String inetHost, final Promise<U> promise, final boolean resolveAll) {
        final Promise<U> earlyPromise = resolveMap.putIfAbsent(inetHost, promise);
        if (earlyPromise != null) {
            if (earlyPromise.isDone()) {
                transferResult(earlyPromise, promise);
            }
            else {
                earlyPromise.addListener((GenericFutureListener<? extends Future<? super U>>)new FutureListener<U>() {
                    @Override
                    public void operationComplete(final Future<U> f) throws Exception {
                        transferResult(f, (Promise<Object>)promise);
                    }
                });
            }
        }
        else {
            try {
                if (resolveAll) {
                    final Promise<List<T>> castPromise = (Promise<List<T>>)promise;
                    this.delegate.resolveAll(inetHost, castPromise);
                }
                else {
                    final Promise<T> castPromise2 = (Promise<T>)promise;
                    this.delegate.resolve(inetHost, castPromise2);
                }
            }
            finally {
                if (promise.isDone()) {
                    resolveMap.remove(inetHost);
                }
                else {
                    promise.addListener((GenericFutureListener<? extends Future<? super U>>)new FutureListener<U>() {
                        @Override
                        public void operationComplete(final Future<U> f) throws Exception {
                            resolveMap.remove(inetHost);
                        }
                    });
                }
            }
        }
        return promise;
    }
    
    private static <T> void transferResult(final Future<T> src, final Promise<T> dst) {
        if (src.isSuccess()) {
            dst.trySuccess(src.getNow());
        }
        else {
            dst.tryFailure(src.cause());
        }
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.delegate + ')';
    }
}
