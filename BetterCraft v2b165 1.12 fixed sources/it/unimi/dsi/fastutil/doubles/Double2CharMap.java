// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2CharMap extends Double2CharFunction, Map<Double, Character>
{
    ObjectSet<Map.Entry<Double, Character>> entrySet();
    
    ObjectSet<Entry> double2CharEntrySet();
    
    DoubleSet keySet();
    
    CharCollection values();
    
    boolean containsValue(final char p0);
    
    public interface Entry extends Map.Entry<Double, Character>
    {
        @Deprecated
        Double getKey();
        
        double getDoubleKey();
        
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
