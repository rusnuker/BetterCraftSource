// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2LongMap extends Int2LongFunction, Map<Integer, Long>
{
    ObjectSet<Map.Entry<Integer, Long>> entrySet();
    
    ObjectSet<Entry> int2LongEntrySet();
    
    IntSet keySet();
    
    LongCollection values();
    
    boolean containsValue(final long p0);
    
    public interface Entry extends Map.Entry<Integer, Long>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
