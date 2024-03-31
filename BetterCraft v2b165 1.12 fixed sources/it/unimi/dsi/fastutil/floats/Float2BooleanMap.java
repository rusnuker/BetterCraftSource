// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2BooleanMap extends Float2BooleanFunction, Map<Float, Boolean>
{
    ObjectSet<Map.Entry<Float, Boolean>> entrySet();
    
    ObjectSet<Entry> float2BooleanEntrySet();
    
    FloatSet keySet();
    
    BooleanCollection values();
    
    boolean containsValue(final boolean p0);
    
    public interface Entry extends Map.Entry<Float, Boolean>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
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
