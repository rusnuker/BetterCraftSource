// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Char2DoubleMap extends Char2DoubleFunction, Map<Character, Double>
{
    ObjectSet<Map.Entry<Character, Double>> entrySet();
    
    ObjectSet<Entry> char2DoubleEntrySet();
    
    CharSet keySet();
    
    DoubleCollection values();
    
    boolean containsValue(final double p0);
    
    public interface Entry extends Map.Entry<Character, Double>
    {
        @Deprecated
        Character getKey();
        
        char getCharKey();
        
        @Deprecated
        Double getValue();
        
        double setValue(final double p0);
        
        double getDoubleValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
