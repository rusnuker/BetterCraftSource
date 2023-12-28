// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2ByteMap extends Int2ByteFunction, Map<Integer, Byte>
{
    ObjectSet<Map.Entry<Integer, Byte>> entrySet();
    
    ObjectSet<Entry> int2ByteEntrySet();
    
    IntSet keySet();
    
    ByteCollection values();
    
    boolean containsValue(final byte p0);
    
    public interface Entry extends Map.Entry<Integer, Byte>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
