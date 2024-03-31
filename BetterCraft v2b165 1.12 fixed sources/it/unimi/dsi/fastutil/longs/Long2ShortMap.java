// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2ShortMap extends Long2ShortFunction, Map<Long, Short>
{
    ObjectSet<Map.Entry<Long, Short>> entrySet();
    
    ObjectSet<Entry> long2ShortEntrySet();
    
    LongSet keySet();
    
    ShortCollection values();
    
    boolean containsValue(final short p0);
    
    public interface Entry extends Map.Entry<Long, Short>
    {
        @Deprecated
        Long getKey();
        
        long getLongKey();
        
        @Deprecated
        Short getValue();
        
        short setValue(final short p0);
        
        short getShortValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
