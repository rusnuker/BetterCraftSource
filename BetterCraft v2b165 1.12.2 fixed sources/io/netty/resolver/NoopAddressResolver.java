// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.Collections;
import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.EventExecutor;
import java.net.SocketAddress;

public class NoopAddressResolver extends AbstractAddressResolver<SocketAddress>
{
    public NoopAddressResolver(final EventExecutor executor) {
        super(executor);
    }
    
    @Override
    protected boolean doIsResolved(final SocketAddress address) {
        return true;
    }
    
    @Override
    protected void doResolve(final SocketAddress unresolvedAddress, final Promise<SocketAddress> promise) throws Exception {
        promise.setSuccess(unresolvedAddress);
    }
    
    @Override
    protected void doResolveAll(final SocketAddress unresolvedAddress, final Promise<List<SocketAddress>> promise) throws Exception {
        promise.setSuccess(Collections.singletonList(unresolvedAddress));
    }
}
