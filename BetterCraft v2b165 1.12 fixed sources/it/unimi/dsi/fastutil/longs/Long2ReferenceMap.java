// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2ReferenceMap<V> extends Long2ReferenceFunction<V>, Map<Long, V>
{
    ObjectSet<Map.Entry<Long, V>> entrySet();
    
    ObjectSet<Entry<V>> long2ReferenceEntrySet();
    
    LongSet keySet();
    
    ReferenceCollection<V> values();
    
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
