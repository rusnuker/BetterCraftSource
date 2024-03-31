// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import java.io.Closeable;
import java.net.SocketAddress;

public interface AddressResolver<T extends SocketAddress> extends Closeable
{
    boolean isSupported(final SocketAddress p0);
    
    boolean isResolved(final SocketAddress p0);
    
    Future<T> resolve(final SocketAddress p0);
    
    Future<T> resolve(final SocketAddress p0, final Promise<T> p1);
    
    Future<List<T>> resolveAll(final SocketAddress p0);
    
    Future<List<T>> resolveAll(final SocketAddress p0, final Promise<List<T>> p1);
    
    void close();
}
