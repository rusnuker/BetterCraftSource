// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2ShortMap extends Float2ShortFunction, Map<Float, Short>
{
    ObjectSet<Map.Entry<Float, Short>> entrySet();
    
    ObjectSet<Entry> float2ShortEntrySet();
    
    FloatSet keySet();
    
    ShortCollection values();
    
    boolean containsValue(final short p0);
    
    public interface Entry extends Map.Entry<Float, Short>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
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
