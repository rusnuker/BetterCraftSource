// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2ShortMap extends Double2ShortFunction, Map<Double, Short>
{
    ObjectSet<Map.Entry<Double, Short>> entrySet();
    
    ObjectSet<Entry> double2ShortEntrySet();
    
    DoubleSet keySet();
    
    ShortCollection values();
    
    boolean containsValue(final short p0);
    
    public interface Entry extends Map.Entry<Double, Short>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
        @Deprecated
        Short getValue();
        
        short setValue(final short p0);
        
        short getShortValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
