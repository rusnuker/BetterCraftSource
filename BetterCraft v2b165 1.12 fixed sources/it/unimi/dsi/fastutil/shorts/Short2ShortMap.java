// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2ShortMap extends Short2ShortFunction, Map<Short, Short>
{
    ObjectSet<Map.Entry<Short, Short>> entrySet();
    
    ObjectSet<Entry> short2ShortEntrySet();
    
    ShortSet keySet();
    
    ShortCollection values();
    
    boolean containsValue(final short p0);
    
    public interface Entry extends Map.Entry<Short, Short>
    {
        @Deprecated
        Short getKey();
        
        short getShortKey();
        
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
