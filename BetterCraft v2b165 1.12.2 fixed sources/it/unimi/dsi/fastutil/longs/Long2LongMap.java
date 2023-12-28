// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2LongMap extends Long2LongFunction, Map<Long, Long>
{
    ObjectSet<Map.Entry<Long, Long>> entrySet();
    
    ObjectSet<Entry> long2LongEntrySet();
    
    LongSet keySet();
    
    LongCollection values();
    
    boolean containsValue(final long p0);
    
    public interface Entry extends Map.Entry<Long, Long>
    {
        @Deprecated
        Long getKey();
        
        long getLongKey();
        
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
