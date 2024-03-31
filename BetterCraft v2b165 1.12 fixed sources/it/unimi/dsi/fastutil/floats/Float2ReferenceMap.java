// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2ReferenceMap<V> extends Float2ReferenceFunction<V>, Map<Float, V>
{
    ObjectSet<Map.Entry<Float, V>> entrySet();
    
    ObjectSet<Entry<V>> float2ReferenceEntrySet();
    
    FloatSet keySet();
    
    ReferenceCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Float, V>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
