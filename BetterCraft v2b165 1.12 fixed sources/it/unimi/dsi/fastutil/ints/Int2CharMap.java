// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2CharMap extends Int2CharFunction, Map<Integer, Character>
{
    ObjectSet<Map.Entry<Integer, Character>> entrySet();
    
    ObjectSet<Entry> int2CharEntrySet();
    
    IntSet keySet();
    
    CharCollection values();
    
    boolean containsValue(final char p0);
    
    public interface Entry extends Map.Entry<Integer, Character>
    {
        @Deprecated
        Integer getKey();
        
        int getIntKey();
        
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
