// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2DoubleMap extends Byte2DoubleFunction, Map<Byte, Double>
{
    ObjectSet<Map.Entry<Byte, Double>> entrySet();
    
    ObjectSet<Entry> byte2DoubleEntrySet();
    
    ByteSet keySet();
    
    DoubleCollection values();
    
    boolean containsValue(final double p0);
    
    public interface Entry extends Map.Entry<Byte, Double>
    {
        @Deprecated
        Byte getKey();
        
        byte getByteKey();
        
        @Deprecated
        Double getValue();
        
        double setValue(final double p0);
        
        double getDoubleValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
