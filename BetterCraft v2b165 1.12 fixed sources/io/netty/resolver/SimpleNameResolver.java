// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;

public abstract class SimpleNameResolver<T> implements NameResolver<T>
{
    private final EventExecutor executor;
    
    protected SimpleNameResolver(final EventExecutor executor) {
        this.executor = ObjectUtil.checkNotNull(executor, "executor");
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    @Override
    public final Future<T> resolve(final String inetHost) {
        final Promise<T> promise = this.executor().newPromise();
        return this.resolve(inetHost, promise);
    }
    
    @Override
    public Future<T> resolve(final String inetHost, final Promise<T> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        try {
            this.doResolve(inetHost, promise);
            return promise;
        }
        catch (final Exception e) {
            return promise.setFailure(e);
        }
    }
    
    @Override
    public final Future<List<T>> resolveAll(final String inetHost) {
        final Promise<List<T>> promise = this.executor().newPromise();
        return this.resolveAll(inetHost, promise);
    }
    
    @Override
    public Future<List<T>> resolveAll(final String inetHost, final Promise<List<T>> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        try {
            this.doResolveAll(inetHost, promise);
            return promise;
        }
        catch (final Exception e) {
            return promise.setFailure(e);
        }
    }
    
    protected abstract void doResolve(final String p0, final Promise<T> p1) throws Exception;
    
    protected abstract void doResolveAll(final String p0, final Promise<List<T>> p1) throws Exception;
    
    @Override
    public void close() {
    }
}
