// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2ObjectMap<V> extends Short2ObjectFunction<V>, Map<Short, V>
{
    ObjectSet<Map.Entry<Short, V>> entrySet();
    
    ObjectSet<Entry<V>> short2ObjectEntrySet();
    
    ShortSet keySet();
    
    ObjectCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Short, V>
    {
        @Deprecated
        Short getKey();
        
        short getShortKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
