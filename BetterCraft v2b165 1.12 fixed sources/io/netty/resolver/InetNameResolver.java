// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import java.net.InetSocketAddress;
import java.net.InetAddress;

public abstract class InetNameResolver extends SimpleNameResolver<InetAddress>
{
    private volatile AddressResolver<InetSocketAddress> addressResolver;
    
    protected InetNameResolver(final EventExecutor executor) {
        super(executor);
    }
    
    public AddressResolver<InetSocketAddress> asAddressResolver() {
        AddressResolver<InetSocketAddress> result = this.addressResolver;
        if (result == null) {
            synchronized (this) {
                result = this.addressResolver;
                if (result == null) {
                    result = (this.addressResolver = new InetSocketAddressResolver(this.executor(), this));
                }
            }
        }
        return result;
    }
}
