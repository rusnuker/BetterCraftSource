// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2FloatMap extends Float2FloatFunction, Map<Float, Float>
{
    ObjectSet<Map.Entry<Float, Float>> entrySet();
    
    ObjectSet<Entry> float2FloatEntrySet();
    
    FloatSet keySet();
    
    FloatCollection values();
    
    boolean containsValue(final float p0);
    
    public interface Entry extends Map.Entry<Float, Float>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
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
