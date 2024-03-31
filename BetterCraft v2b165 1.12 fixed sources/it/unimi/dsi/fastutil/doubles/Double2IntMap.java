// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2IntMap extends Double2IntFunction, Map<Double, Integer>
{
    ObjectSet<Map.Entry<Double, Integer>> entrySet();
    
    ObjectSet<Entry> double2IntEntrySet();
    
    DoubleSet keySet();
    
    IntCollection values();
    
    boolean containsValue(final int p0);
    
    public interface Entry extends Map.Entry<Double, Integer>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
        @Deprecated
        Integer getValue();
        
        int setValue(final int p0);
        
        int getIntValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
