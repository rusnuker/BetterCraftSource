// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;

public interface Reference2ByteMap<K> extends Reference2ByteFunction<K>, Map<K, Byte>
{
    ObjectSet<Map.Entry<K, Byte>> entrySet();
    
    ObjectSet<Entry<K>> reference2ByteEntrySet();
    
    ReferenceSet<K> keySet();
    
    ByteCollection values();
    
    boolean containsValue(final byte p0);
    
    public interface Entry<K> extends Map.Entry<K, Byte>
    {
        @Deprecated
        Byte getValue();
        
        byte setValue(final byte p0);
        
        byte getByteValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
