// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2CharMap extends Long2CharFunction, Map<Long, Character>
{
    ObjectSet<Map.Entry<Long, Character>> entrySet();
    
    ObjectSet<Entry> long2CharEntrySet();
    
    LongSet keySet();
    
    CharCollection values();
    
    boolean containsValue(final char p0);
    
    public interface Entry extends Map.Entry<Long, Character>
    {
        @Deprecated
        Long getKey();
        
        long getLongKey();
        
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
