// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2LongMap extends Float2LongFunction, Map<Float, Long>
{
    ObjectSet<Map.Entry<Float, Long>> entrySet();
    
    ObjectSet<Entry> float2LongEntrySet();
    
    FloatSet keySet();
    
    LongCollection values();
    
    boolean containsValue(final long p0);
    
    public interface Entry extends Map.Entry<Float, Long>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
        @Deprecated
        Long getValue();
        
        long setValue(final long p0);
        
        long getLongValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
