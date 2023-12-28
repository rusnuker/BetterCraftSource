// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;

public interface Reference2DoubleMap<K> extends Reference2DoubleFunction<K>, Map<K, Double>
{
    ObjectSet<Map.Entry<K, Double>> entrySet();
    
    ObjectSet<Entry<K>> reference2DoubleEntrySet();
    
    ReferenceSet<K> keySet();
    
    DoubleCollection values();
    
    boolean containsValue(final double p0);
    
    public interface Entry<K> extends Map.Entry<K, Double>
    {
        @Deprecated
        Double getValue();
        
        double setValue(final double p0);
        
        double getDoubleValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
