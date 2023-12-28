// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.Future;
import java.io.Closeable;

public interface NameResolver<T> extends Closeable
{
    Future<T> resolve(final String p0);
    
    Future<T> resolve(final String p0, final Promise<T> p1);
    
    Future<List<T>> resolveAll(final String p0);
    
    Future<List<T>> resolveAll(final String p0, final Promise<List<T>> p1);
    
    void close();
}
