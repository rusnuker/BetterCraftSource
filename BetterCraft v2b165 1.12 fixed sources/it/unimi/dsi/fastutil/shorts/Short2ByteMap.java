// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2ByteMap extends Short2ByteFunction, Map<Short, Byte>
{
    ObjectSet<Map.Entry<Short, Byte>> entrySet();
    
    ObjectSet<Entry> short2ByteEntrySet();
    
    ShortSet keySet();
    
    ByteCollection values();
    
    boolean containsValue(final byte p0);
    
    public interface Entry extends Map.Entry<Short, Byte>
    {
        @Deprecated
        Short getKey();
        
        short getShortKey();
        
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
