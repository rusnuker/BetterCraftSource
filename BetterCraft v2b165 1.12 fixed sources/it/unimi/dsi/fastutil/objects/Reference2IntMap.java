// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;

public interface Reference2IntMap<K> extends Reference2IntFunction<K>, Map<K, Integer>
{
    ObjectSet<Map.Entry<K, Integer>> entrySet();
    
    ObjectSet<Entry<K>> reference2IntEntrySet();
    
    ReferenceSet<K> keySet();
    
    IntCollection values();
    
    boolean containsValue(final int p0);
    
    public interface Entry<K> extends Map.Entry<K, Integer>
    {
        @Deprecated
        Integer getValue();
        
        int setValue(final int p0);
        
        int getIntValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
