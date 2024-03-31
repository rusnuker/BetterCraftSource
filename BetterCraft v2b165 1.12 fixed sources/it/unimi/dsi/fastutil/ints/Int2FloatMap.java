// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2FloatMap extends Int2FloatFunction, Map<Integer, Float>
{
    ObjectSet<Map.Entry<Integer, Float>> entrySet();
    
    ObjectSet<Entry> int2FloatEntrySet();
    
    IntSet keySet();
    
    FloatCollection values();
    
    boolean containsValue(final float p0);
    
    public interface Entry extends Map.Entry<Integer, Float>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
