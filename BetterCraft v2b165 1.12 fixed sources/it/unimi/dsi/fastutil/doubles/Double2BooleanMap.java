// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2BooleanMap extends Double2BooleanFunction, Map<Double, Boolean>
{
    ObjectSet<Map.Entry<Double, Boolean>> entrySet();
    
    ObjectSet<Entry> double2BooleanEntrySet();
    
    DoubleSet keySet();
    
    BooleanCollection values();
    
    boolean containsValue(final boolean p0);
    
    public interface Entry extends Map.Entry<Double, Boolean>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
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
