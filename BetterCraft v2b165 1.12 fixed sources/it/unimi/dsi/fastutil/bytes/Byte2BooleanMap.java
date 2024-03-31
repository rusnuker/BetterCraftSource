// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2BooleanMap extends Byte2BooleanFunction, Map<Byte, Boolean>
{
    ObjectSet<Map.Entry<Byte, Boolean>> entrySet();
    
    ObjectSet<Entry> byte2BooleanEntrySet();
    
    ByteSet keySet();
    
    BooleanCollection values();
    
    boolean containsValue(final boolean p0);
    
    public interface Entry extends Map.Entry<Byte, Boolean>
    {
        @Deprecated
        Byte getKey();
        
        byte getByteKey();
        
        @Deprecated
        Boolean getValue();
        
        boolean setValue(final boolean p0);
        
        boolean getBooleanValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
