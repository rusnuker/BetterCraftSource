// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2FloatMap extends Double2FloatFunction, Map<Double, Float>
{
    ObjectSet<Map.Entry<Double, Float>> entrySet();
    
    ObjectSet<Entry> double2FloatEntrySet();
    
    DoubleSet keySet();
    
    FloatCollection values();
    
    boolean containsValue(final float p0);
    
    public interface Entry extends Map.Entry<Double, Float>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
        @Deprecated
        Float getValue();
        
        float setValue(final float p0);
        
        float getFloatValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
