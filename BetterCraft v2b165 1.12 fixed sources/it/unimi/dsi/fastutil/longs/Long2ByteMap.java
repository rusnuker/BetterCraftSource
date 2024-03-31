// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2ByteMap extends Long2ByteFunction, Map<Long, Byte>
{
    ObjectSet<Map.Entry<Long, Byte>> entrySet();
    
    ObjectSet<Entry> long2ByteEntrySet();
    
    LongSet keySet();
    
    ByteCollection values();
    
    boolean containsValue(final byte p0);
    
    public interface Entry extends Map.Entry<Long, Byte>
    {
        @Deprecated
        Long getKey();
        
        long getLongKey();
        
        @Deprecated
        Byte getValue();
        
        byte setValue(final byte p0);
        
        byte getByteValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
