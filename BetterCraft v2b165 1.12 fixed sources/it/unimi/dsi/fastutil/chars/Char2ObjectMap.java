// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Char2ObjectMap<V> extends Char2ObjectFunction<V>, Map<Character, V>
{
    ObjectSet<Map.Entry<Character, V>> entrySet();
    
    ObjectSet<Entry<V>> char2ObjectEntrySet();
    
    CharSet keySet();
    
    ObjectCollection<V> values();
    
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
