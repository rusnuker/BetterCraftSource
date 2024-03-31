// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2ObjectMap<V> extends Long2ObjectFunction<V>, Map<Long, V>
{
    ObjectSet<Map.Entry<Long, V>> entrySet();
    
    ObjectSet<Entry<V>> long2ObjectEntrySet();
    
    LongSet keySet();
    
    ObjectCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Long, V>
    {
        @Deprecated
        Long getKey();
        
        long getLongKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
