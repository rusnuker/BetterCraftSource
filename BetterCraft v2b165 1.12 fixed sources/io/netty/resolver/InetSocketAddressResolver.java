// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.EventExecutor;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class InetSocketAddressResolver extends AbstractAddressResolver<InetSocketAddress>
{
    final NameResolver<InetAddress> nameResolver;
    
    public InetSocketAddressResolver(final EventExecutor executor, final NameResolver<InetAddress> nameResolver) {
        super(executor, InetSocketAddress.class);
        this.nameResolver = nameResolver;
    }
    
    @Override
    protected boolean doIsResolved(final InetSocketAddress address) {
        return !address.isUnresolved();
    }
    
    @Override
    protected void doResolve(final InetSocketAddress unresolvedAddress, final Promise<InetSocketAddress> promise) throws Exception {
        this.nameResolver.resolve(unresolvedAddress.getHostName()).addListener(new FutureListener<InetAddress>() {
            @Override
            public void operationComplete(final Future<InetAddress> future) throws Exception {
                if (future.isSuccess()) {
                    promise.setSuccess(new InetSocketAddress(future.getNow(), unresolvedAddress.getPort()));
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }
    
    @Override
    protected void doResolveAll(final InetSocketAddress unresolvedAddress, final Promise<List<InetSocketAddress>> promise) throws Exception {
        this.nameResolver.resolveAll(unresolvedAddress.getHostName()).addListener(new FutureListener<List<InetAddress>>() {
            @Override
            public void operationComplete(final Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    final List<InetAddress> inetAddresses = future.getNow();
                    final List<InetSocketAddress> socketAddresses = new ArrayList<InetSocketAddress>(inetAddresses.size());
                    for (final InetAddress inetAddress : inetAddresses) {
                        socketAddresses.add(new InetSocketAddress(inetAddress, unresolvedAddress.getPort()));
                    }
                    promise.setSuccess(socketAddresses);
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }
    
    @Override
    public void close() {
        this.nameResolver.close();
    }
}
