// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import java.net.InetSocketAddress;

public final class DefaultAddressResolverGroup extends AddressResolverGroup<InetSocketAddress>
{
    public static final DefaultAddressResolverGroup INSTANCE;
    
    private DefaultAddressResolverGroup() {
    }
    
    @Override
    protected AddressResolver<InetSocketAddress> newResolver(final EventExecutor executor) throws Exception {
        return new DefaultNameResolver(executor).asAddressResolver();
    }
    
    static {
        INSTANCE = new DefaultAddressResolverGroup();
    }
}
