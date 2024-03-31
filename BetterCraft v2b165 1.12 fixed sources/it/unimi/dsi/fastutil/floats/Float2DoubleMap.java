// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2DoubleMap extends Float2DoubleFunction, Map<Float, Double>
{
    ObjectSet<Map.Entry<Float, Double>> entrySet();
    
    ObjectSet<Entry> float2DoubleEntrySet();
    
    FloatSet keySet();
    
    DoubleCollection values();
    
    boolean containsValue(final double p0);
    
    public interface Entry extends Map.Entry<Float, Double>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
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
