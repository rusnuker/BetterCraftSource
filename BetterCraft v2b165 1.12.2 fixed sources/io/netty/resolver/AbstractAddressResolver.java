// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.Collections;
import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import java.nio.channels.UnsupportedAddressTypeException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.util.concurrent.EventExecutor;
import java.net.SocketAddress;

public abstract class AbstractAddressResolver<T extends SocketAddress> implements AddressResolver<T>
{
    private final EventExecutor executor;
    private final TypeParameterMatcher matcher;
    
    protected AbstractAddressResolver(final EventExecutor executor) {
        this.executor = ObjectUtil.checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.find(this, AbstractAddressResolver.class, "T");
    }
    
    protected AbstractAddressResolver(final EventExecutor executor, final Class<? extends T> addressType) {
        this.executor = ObjectUtil.checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.get(addressType);
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    @Override
    public boolean isSupported(final SocketAddress address) {
        return this.matcher.match(address);
    }
    
    @Override
    public final boolean isResolved(final SocketAddress address) {
        if (!this.isSupported(address)) {
            throw new UnsupportedAddressTypeException();
        }
        final T castAddress = (T)address;
        return this.doIsResolved(castAddress);
    }
    
    protected abstract boolean doIsResolved(final T p0);
    
    @Override
    public final Future<T> resolve(final SocketAddress address) {
        if (!this.isSupported(ObjectUtil.checkNotNull(address, "address"))) {
            return this.executor().newFailedFuture(new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return this.executor.newSucceededFuture(cast);
        }
        try {
            final T cast = (T)address;
            final Promise<T> promise = this.executor().newPromise();
            this.doResolve(cast, promise);
            return promise;
        }
        catch (final Exception e) {
            return this.executor().newFailedFuture(e);
        }
    }
    
    @Override
    public final Future<T> resolve(final SocketAddress address, final Promise<T> promise) {
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(promise, "promise");
        if (!this.isSupported(address)) {
            return promise.setFailure(new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return promise.setSuccess(cast);
        }
        try {
            final T cast = (T)address;
            this.doResolve(cast, promise);
            return promise;
        }
        catch (final Exception e) {
            return promise.setFailure(e);
        }
    }
    
    @Override
    public final Future<List<T>> resolveAll(final SocketAddress address) {
        if (!this.isSupported(ObjectUtil.checkNotNull(address, "address"))) {
            return this.executor().newFailedFuture(new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return this.executor.newSucceededFuture(Collections.singletonList(cast));
        }
        try {
            final T cast = (T)address;
            final Promise<List<T>> promise = this.executor().newPromise();
            this.doResolveAll(cast, promise);
            return promise;
        }
        catch (final Exception e) {
            return this.executor().newFailedFuture(e);
        }
    }
    
    @Override
    public final Future<List<T>> resolveAll(final SocketAddress address, final Promise<List<T>> promise) {
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(promise, "promise");
        if (!this.isSupported(address)) {
            return promise.setFailure(new UnsupportedAddressTypeException());
        }
        if (this.isResolved(address)) {
            final T cast = (T)address;
            return promise.setSuccess(Collections.singletonList(cast));
        }
        try {
            final T cast = (T)address;
            this.doResolveAll(cast, promise);
            return promise;
        }
        catch (final Exception e) {
            return promise.setFailure(e);
        }
    }
    
    protected abstract void doResolve(final T p0, final Promise<T> p1) throws Exception;
    
    protected abstract void doResolveAll(final T p0, final Promise<List<T>> p1) throws Exception;
    
    @Override
    public void close() {
    }
}
