// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Char2ReferenceMap<V> extends Char2ReferenceFunction<V>, Map<Character, V>
{
    ObjectSet<Map.Entry<Character, V>> entrySet();
    
    ObjectSet<Entry<V>> char2ReferenceEntrySet();
    
    CharSet keySet();
    
    ReferenceCollection<V> values();
    
    public interface Entry<V> extends Map.Entry<Character, V>
    {
        @Deprecated
        Character getKey();
        
        char getCharKey();
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
    }
}
