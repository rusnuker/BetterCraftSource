// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2ByteMap extends Byte2ByteFunction, Map<Byte, Byte>
{
    ObjectSet<Map.Entry<Byte, Byte>> entrySet();
    
    ObjectSet<Entry> byte2ByteEntrySet();
    
    ByteSet keySet();
    
    ByteCollection values();
    
    boolean containsValue(final byte p0);
    
    public interface Entry extends Map.Entry<Byte, Byte>
    {
        @Deprecated
        Byte getKey();
        
        byte getByteKey();
        
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
