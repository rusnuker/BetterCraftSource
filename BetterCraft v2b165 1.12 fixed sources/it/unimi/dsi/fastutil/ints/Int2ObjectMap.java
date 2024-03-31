// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2ObjectMap<V> extends Int2ObjectFunction<V>, Map<Integer, V>
{
    ObjectSet<Map.Entry<Integer, V>> entrySet();
    
    ObjectSet<Entry<V>> int2ObjectEntrySet();
    
    IntSet keySet();
    
    ObjectCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Integer, V>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
