// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Map;

public interface Object2ReferenceMap<K, V> extends Object2ReferenceFunction<K, V>, Map<K, V>
{
    ObjectSet<Map.Entry<K, V>> entrySet();
    
    ObjectSet<Entry<K, V>> object2ReferenceEntrySet();
    
    ObjectSet<K> keySet();
    
    ReferenceCollection<V> values();
    
    public interface Entry<K, V> extends Map.Entry<K, V>
    {
    }
    
    public interface FastEntrySet<K, V> extends ObjectSet<Entry<K, V>>
    {
        ObjectIterator<Entry<K, V>> fastIterator();
    }
}
