// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2BooleanMap extends Int2BooleanFunction, Map<Integer, Boolean>
{
    ObjectSet<Map.Entry<Integer, Boolean>> entrySet();
    
    ObjectSet<Entry> int2BooleanEntrySet();
    
    IntSet keySet();
    
    BooleanCollection values();
    
    boolean containsValue(final boolean p0);
    
    public interface Entry extends Map.Entry<Integer, Boolean>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
