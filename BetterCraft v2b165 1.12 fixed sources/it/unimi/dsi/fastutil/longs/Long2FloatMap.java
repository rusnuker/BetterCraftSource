// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2FloatMap extends Long2FloatFunction, Map<Long, Float>
{
    ObjectSet<Map.Entry<Long, Float>> entrySet();
    
    ObjectSet<Entry> long2FloatEntrySet();
    
    LongSet keySet();
    
    FloatCollection values();
    
    boolean containsValue(final float p0);
    
    public interface Entry extends Map.Entry<Long, Float>
    {
        @Deprecated
        Long getKey();
        
        long getLongKey();
        
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
