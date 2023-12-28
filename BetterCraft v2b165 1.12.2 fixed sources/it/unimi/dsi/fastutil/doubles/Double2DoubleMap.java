// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2DoubleMap extends Double2DoubleFunction, Map<Double, Double>
{
    ObjectSet<Map.Entry<Double, Double>> entrySet();
    
    ObjectSet<Entry> double2DoubleEntrySet();
    
    DoubleSet keySet();
    
    DoubleCollection values();
    
    boolean containsValue(final double p0);
    
    public interface Entry extends Map.Entry<Double, Double>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
        @Deprecated
        Double getValue();
        
        double setValue(final double p0);
        
        double getDoubleValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
