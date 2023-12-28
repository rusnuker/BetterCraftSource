// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2ShortMap extends Int2ShortFunction, Map<Integer, Short>
{
    ObjectSet<Map.Entry<Integer, Short>> entrySet();
    
    ObjectSet<Entry> int2ShortEntrySet();
    
    IntSet keySet();
    
    ShortCollection values();
    
    boolean containsValue(final short p0);
    
    public interface Entry extends Map.Entry<Integer, Short>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
