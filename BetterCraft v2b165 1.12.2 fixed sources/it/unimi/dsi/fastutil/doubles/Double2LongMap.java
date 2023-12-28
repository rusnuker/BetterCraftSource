// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2LongMap extends Double2LongFunction, Map<Double, Long>
{
    ObjectSet<Map.Entry<Double, Long>> entrySet();
    
    ObjectSet<Entry> double2LongEntrySet();
    
    DoubleSet keySet();
    
    LongCollection values();
    
    boolean containsValue(final long p0);
    
    public interface Entry extends Map.Entry<Double, Long>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
        @Deprecated
        Long getValue();
        
        long setValue(final long p0);
        
        long getLongValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
