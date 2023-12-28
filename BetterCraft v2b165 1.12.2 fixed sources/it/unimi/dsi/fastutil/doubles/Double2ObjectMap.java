// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2ObjectMap<V> extends Double2ObjectFunction<V>, Map<Double, V>
{
    ObjectSet<Map.Entry<Double, V>> entrySet();
    
    ObjectSet<Entry<V>> double2ObjectEntrySet();
    
    DoubleSet keySet();
    
    ObjectCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Double, V>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
