// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Char2CharMap extends Char2CharFunction, Map<Character, Character>
{
    ObjectSet<Map.Entry<Character, Character>> entrySet();
    
    ObjectSet<Entry> char2CharEntrySet();
    
    CharSet keySet();
    
    CharCollection values();
    
    boolean containsValue(final char p0);
    
    public interface Entry extends Map.Entry<Character, Character>
    {
        @Deprecated
        Character getKey();
        
        char getCharKey();
        
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
