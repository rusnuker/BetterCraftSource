// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2IntMap extends Float2IntFunction, Map<Float, Integer>
{
    ObjectSet<Map.Entry<Float, Integer>> entrySet();
    
    ObjectSet<Entry> float2IntEntrySet();
    
    FloatSet keySet();
    
    IntCollection values();
    
    boolean containsValue(final int p0);
    
    public interface Entry extends Map.Entry<Float, Integer>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
        @Deprecated
        Integer getValue();
        
        int setValue(final int p0);
        
        int getIntValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
