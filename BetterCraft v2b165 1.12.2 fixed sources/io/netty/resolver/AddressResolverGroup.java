// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import java.util.IdentityHashMap;
import io.netty.util.concurrent.EventExecutor;
import java.util.Map;
import io.netty.util.internal.logging.InternalLogger;
import java.io.Closeable;
import java.net.SocketAddress;

public abstract class AddressResolverGroup<T extends SocketAddress> implements Closeable
{
    private static final InternalLogger logger;
    private final Map<EventExecutor, AddressResolver<T>> resolvers;
    
    protected AddressResolverGroup() {
        this.resolvers = new IdentityHashMap<EventExecutor, AddressResolver<T>>();
    }
    
    public AddressResolver<T> getResolver(final EventExecutor executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        if (executor.isShuttingDown()) {
            throw new IllegalStateException("executor not accepting a task");
        }
        AddressResolver<T> r;
        synchronized (this.resolvers) {
            r = this.resolvers.get(executor);
            if (r == null) {
                AddressResolver<T> newResolver;
                try {
                    newResolver = this.newResolver(executor);
                }
                catch (final Exception e) {
                    throw new IllegalStateException("failed to create a new resolver", e);
                }
                this.resolvers.put(executor, newResolver);
                executor.terminationFuture().addListener(new FutureListener<Object>() {
                    @Override
                    public void operationComplete(final Future<Object> future) throws Exception {
                        synchronized (AddressResolverGroup.this.resolvers) {
                            AddressResolverGroup.this.resolvers.remove(executor);
                        }
                        newResolver.close();
                    }
                });
                r = newResolver;
            }
        }
        return r;
    }
    
    protected abstract AddressResolver<T> newResolver(final EventExecutor p0) throws Exception;
    
    @Override
    public void close() {
        final AddressResolver<T>[] rArray;
        synchronized (this.resolvers) {
            rArray = this.resolvers.values().toArray(new AddressResolver[this.resolvers.size()]);
            this.resolvers.clear();
        }
        for (final AddressResolver<T> r : rArray) {
            try {
                r.close();
            }
            catch (final Throwable t) {
                AddressResolverGroup.logger.warn("Failed to close a resolver:", t);
            }
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AddressResolverGroup.class);
    }
}
