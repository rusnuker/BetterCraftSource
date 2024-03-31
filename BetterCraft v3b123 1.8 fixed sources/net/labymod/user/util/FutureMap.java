// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.util;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.function.Consumer;
import com.google.common.util.concurrent.SettableFuture;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class FutureMap<K, V>
{
    private static ScheduledExecutorService scheduledExecutorService;
    private Map<K, SettableFuture<V>> futureMap;
    private Map<K, Long> startLoadTimesMap;
    private Consumer<K> loadCallback;
    private long timeout;
    private V defaultValue;
    
    static {
        FutureMap.scheduledExecutorService = new ScheduledThreadPoolExecutor(0, new ThreadFactoryBuilder().setNameFormat("LabyMod Futuremap Resolver #%d").setDaemon(true).build());
    }
    
    public FutureMap(final Consumer<K> loadCallback, final long timeout, final V defaultValue) {
        this.futureMap = new ConcurrentHashMap<K, SettableFuture<V>>();
        this.startLoadTimesMap = new ConcurrentHashMap<K, Long>();
        this.loadCallback = loadCallback;
        this.timeout = timeout;
        this.defaultValue = defaultValue;
    }
    
    public ListenableFuture<V> get(final K key) {
        if (this.futureMap.containsKey(key)) {
            return this.futureMap.get(key);
        }
        final SettableFuture<V> valueFuture = SettableFuture.create();
        this.futureMap.put(key, valueFuture);
        this.startLoadTimesMap.put(key, System.currentTimeMillis());
        this.loadCallback.accept(key);
        return valueFuture;
    }
    
    public void resolve(final K key, final V value) {
        final SettableFuture<V> valueFuture = this.futureMap.remove(key);
        if (valueFuture != null) {
            valueFuture.set(value);
            this.startLoadTimesMap.remove(key);
        }
    }
}
