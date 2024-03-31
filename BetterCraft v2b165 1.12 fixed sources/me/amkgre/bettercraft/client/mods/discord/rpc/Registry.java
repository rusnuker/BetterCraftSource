// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.function.Predicate;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Registry<V>
{
    private final CopyOnWriteArrayList<V> objects;
    
    public Registry() {
        this.objects = new CopyOnWriteArrayList<V>();
    }
    
    public V register(final V object) {
        this.objects.add(object);
        return object;
    }
    
    public V unregister(final V object) {
        this.objects.remove(object);
        return object;
    }
    
    public List<V> getEntries(final Predicate<V> filterPredicate) {
        return this.objects.stream().filter((Predicate<? super Object>)filterPredicate).collect((Collector<? super Object, ?, List<V>>)Collectors.toList());
    }
    
    public CopyOnWriteArrayList<V> getObjects() {
        return this.objects;
    }
}
