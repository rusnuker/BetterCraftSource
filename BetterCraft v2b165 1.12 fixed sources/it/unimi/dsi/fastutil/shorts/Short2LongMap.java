// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2LongMap extends Short2LongFunction, Map<Short, Long>
{
    ObjectSet<Map.Entry<Short, Long>> entrySet();
    
    ObjectSet<Entry> short2LongEntrySet();
    
    ShortSet keySet();
    
    LongCollection values();
    
    boolean containsValue(final long p0);
    
    public interface Entry extends Map.Entry<Short, Long>
    {
        @Deprecated
        Short getKey();
        
        short getShortKey();
        
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
