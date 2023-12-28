// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;

public interface Reference2FloatMap<K> extends Reference2FloatFunction<K>, Map<K, Float>
{
    ObjectSet<Map.Entry<K, Float>> entrySet();
    
    ObjectSet<Entry<K>> reference2FloatEntrySet();
    
    ReferenceSet<K> keySet();
    
    FloatCollection values();
    
    boolean containsValue(final float p0);
    
    public interface Entry<K> extends Map.Entry<K, Float>
    {
        @Deprecated
        Float getValue();
        
        float setValue(final float p0);
        
        float getFloatValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
