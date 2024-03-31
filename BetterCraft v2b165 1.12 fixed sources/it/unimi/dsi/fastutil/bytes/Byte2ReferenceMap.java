// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2ReferenceMap<V> extends Byte2ReferenceFunction<V>, Map<Byte, V>
{
    ObjectSet<Map.Entry<Byte, V>> entrySet();
    
    ObjectSet<Entry<V>> byte2ReferenceEntrySet();
    
    ByteSet keySet();
    
    ReferenceCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Byte, V>
    {
        @Deprecated
        Byte getKey();
        
        byte getByteKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
