// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.pool;

import io.netty.util.internal.ReadOnlyIterator;
import java.util.Iterator;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;
import java.io.Closeable;
import java.util.Map;

public abstract class AbstractChannelPoolMap<K, P extends ChannelPool> implements ChannelPoolMap<K, P>, Iterable<Map.Entry<K, P>>, Closeable
{
    private final ConcurrentMap<K, P> map;
    
    public AbstractChannelPoolMap() {
        this.map = PlatformDependent.newConcurrentHashMap();
    }
    
    @Override
    public final P get(final K key) {
        P pool = this.map.get(ObjectUtil.checkNotNull(key, "key"));
        if (pool == null) {
            pool = this.newPool(key);
            final P old = this.map.putIfAbsent(key, pool);
            if (old != null) {
                pool.close();
                pool = old;
            }
        }
        return pool;
    }
    
    public final boolean remove(final K key) {
        final P pool = this.map.remove(ObjectUtil.checkNotNull(key, "key"));
        if (pool != null) {
            pool.close();
            return true;
        }
        return false;
    }
    
    @Override
    public final Iterator<Map.Entry<K, P>> iterator() {
        return new ReadOnlyIterator<Map.Entry<K, P>>((Iterator<? extends Map.Entry<K, P>>)this.map.entrySet().iterator());
    }
    
    public final int size() {
        return this.map.size();
    }
    
    public final boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    @Override
    public final boolean contains(final K key) {
        return this.map.containsKey(ObjectUtil.checkNotNull(key, "key"));
    }
    
    protected abstract P newPool(final K p0);
    
    @Override
    public final void close() {
        for (final K key : this.map.keySet()) {
            this.remove(key);
        }
    }
}
