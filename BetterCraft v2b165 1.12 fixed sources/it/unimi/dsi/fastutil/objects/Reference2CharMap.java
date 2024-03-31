// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;

public interface Reference2CharMap<K> extends Reference2CharFunction<K>, Map<K, Character>
{
    ObjectSet<Map.Entry<K, Character>> entrySet();
    
    ObjectSet<Entry<K>> reference2CharEntrySet();
    
    ReferenceSet<K> keySet();
    
    CharCollection values();
    
    boolean containsValue(final char p0);
    
    public interface Entry<K> extends Map.Entry<K, Character>
    {
        @Deprecated
        Character getValue();
        
        char setValue(final char p0);
        
        char getCharValue();
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>>
    {
        ObjectIterator<Entry<K>> fastIterator();
    }
}
