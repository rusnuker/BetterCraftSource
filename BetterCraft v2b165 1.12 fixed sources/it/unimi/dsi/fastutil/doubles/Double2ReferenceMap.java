// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2ReferenceMap<V> extends Double2ReferenceFunction<V>, Map<Double, V>
{
    ObjectSet<Map.Entry<Double, V>> entrySet();
    
    ObjectSet<Entry<V>> double2ReferenceEntrySet();
    
    DoubleSet keySet();
    
    ReferenceCollection<V> values();
    
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
