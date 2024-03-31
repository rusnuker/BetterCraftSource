// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2IntMap extends Short2IntFunction, Map<Short, Integer>
{
    ObjectSet<Map.Entry<Short, Integer>> entrySet();
    
    ObjectSet<Entry> short2IntEntrySet();
    
    ShortSet keySet();
    
    IntCollection values();
    
    boolean containsValue(final int p0);
    
    public interface Entry extends Map.Entry<Short, Integer>
    {
        @Deprecated
        Short getKey();
        
        short getShortKey();
        
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
