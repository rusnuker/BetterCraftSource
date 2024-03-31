// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Float2CharMap extends Float2CharFunction, Map<Float, Character>
{
    ObjectSet<Map.Entry<Float, Character>> entrySet();
    
    ObjectSet<Entry> float2CharEntrySet();
    
    FloatSet keySet();
    
    CharCollection values();
    
    boolean containsValue(final char p0);
    
    public interface Entry extends Map.Entry<Float, Character>
    {
        @Deprecated
        Float getKey();
        
        float getFloatKey();
        
        @Deprecated
        Character getValue();
        
        char setValue(final char p0);
        
        char getCharValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
