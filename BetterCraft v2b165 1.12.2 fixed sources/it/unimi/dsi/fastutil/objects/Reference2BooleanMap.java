// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;

public interface Reference2BooleanMap<K> extends Reference2BooleanFunction<K>, Map<K, Boolean>
{
    ObjectSet<Map.Entry<K, Boolean>> entrySet();
    
    ObjectSet<Entry<K>> reference2BooleanEntrySet();
    
    ReferenceSet<K> keySet();
    
    BooleanCollection values();
    
    boolean containsValue(final boolean p0);
    
    public interface Entry<K> extends Map.Entry<K, Boolean>
    {
        @Deprecated
        Boolean getValue();
        
        boolean setValue(final boolean p0);
        
        boolean getBooleanValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
