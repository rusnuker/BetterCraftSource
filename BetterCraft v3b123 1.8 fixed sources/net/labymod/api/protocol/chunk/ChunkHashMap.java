// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.protocol.chunk;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class ChunkHashMap<K, V> extends HashMap<K, V>
{
    private List<K> sortedList;
    
    public ChunkHashMap() {
        this.sortedList = new LinkedList<K>();
    }
    
    @Override
    public V put(final K key, final V value) {
        if (this.sortedList.contains(key)) {
            this.sortedList.remove(key);
        }
        this.sortedList.add(key);
        return super.put(key, value);
    }
    
    @Override
    public V remove(final Object key) {
        this.sortedList.remove(key);
        return super.remove(key);
    }
    
    public K getEldestEntry() {
        return this.sortedList.get(0);
    }
    
    public V removeEldestEntry() {
        return this.remove(this.sortedList.get(0));
    }
    
    public void renewEntry(final K key) {
        if (this.containsKey(key)) {
            this.put(key, this.remove(key));
        }
    }
    
    @Override
    public void clear() {
        this.sortedList.clear();
        super.clear();
    }
}
