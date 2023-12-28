// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;

public interface Reference2ShortMap<K> extends Reference2ShortFunction<K>, Map<K, Short>
{
    ObjectSet<Map.Entry<K, Short>> entrySet();
    
    ObjectSet<Entry<K>> reference2ShortEntrySet();
    
    ReferenceSet<K> keySet();
    
    ShortCollection values();
    
    boolean containsValue(final short p0);
    
    public interface Entry<K> extends Map.Entry<K, Short>
    {
        @Deprecated
        Short getValue();
        
        short setValue(final short p0);
        
        short getShortValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
