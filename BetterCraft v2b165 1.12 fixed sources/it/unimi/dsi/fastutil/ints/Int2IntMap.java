// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2IntMap extends Int2IntFunction, Map<Integer, Integer>
{
    ObjectSet<Map.Entry<Integer, Integer>> entrySet();
    
    ObjectSet<Entry> int2IntEntrySet();
    
    IntSet keySet();
    
    IntCollection values();
    
    boolean containsValue(final int p0);
    
    public interface Entry extends Map.Entry<Integer, Integer>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
