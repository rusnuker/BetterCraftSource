// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import io.netty.util.internal.PlatformDependent;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.UnknownHostException;
import io.netty.util.concurrent.Future;
import java.util.List;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.EventExecutor;
import java.net.InetAddress;

public class RoundRobinInetAddressResolver extends InetNameResolver
{
    private final NameResolver<InetAddress> nameResolver;
    
    public RoundRobinInetAddressResolver(final EventExecutor executor, final NameResolver<InetAddress> nameResolver) {
        super(executor);
        this.nameResolver = nameResolver;
    }
    
    @Override
    protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
        this.nameResolver.resolveAll(inetHost).addListener(new FutureListener<List<InetAddress>>() {
            @Override
            public void operationComplete(final Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    final List<InetAddress> inetAddresses = future.getNow();
                    final int numAddresses = inetAddresses.size();
                    if (numAddresses > 0) {
                        promise.setSuccess(inetAddresses.get(randomIndex(numAddresses)));
                    }
                    else {
                        promise.setFailure(new UnknownHostException(inetHost));
                    }
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }
    
    @Override
    protected void doResolveAll(final String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
        this.nameResolver.resolveAll(inetHost).addListener(new FutureListener<List<InetAddress>>() {
            @Override
            public void operationComplete(final Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    final List<InetAddress> inetAddresses = future.getNow();
                    if (!inetAddresses.isEmpty()) {
                        final List<InetAddress> result = new ArrayList<InetAddress>(inetAddresses);
                        Collections.rotate(result, randomIndex(inetAddresses.size()));
                        promise.setSuccess(result);
                    }
                    else {
                        promise.setSuccess(inetAddresses);
                    }
                }
                else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }
    
    private static int randomIndex(final int numAddresses) {
        return (numAddresses == 1) ? 0 : PlatformDependent.threadLocalRandom().nextInt(numAddresses);
    }
}
