// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2ByteMap extends Double2ByteFunction, Map<Double, Byte>
{
    ObjectSet<Map.Entry<Double, Byte>> entrySet();
    
    ObjectSet<Entry> double2ByteEntrySet();
    
    DoubleSet keySet();
    
    ByteCollection values();
    
    boolean containsValue(final byte p0);
    
    public interface Entry extends Map.Entry<Double, Byte>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
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
