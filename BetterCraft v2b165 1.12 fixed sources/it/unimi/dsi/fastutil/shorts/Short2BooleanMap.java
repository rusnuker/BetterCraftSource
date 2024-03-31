// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2BooleanMap extends Short2BooleanFunction, Map<Short, Boolean>
{
    ObjectSet<Map.Entry<Short, Boolean>> entrySet();
    
    ObjectSet<Entry> short2BooleanEntrySet();
    
    ShortSet keySet();
    
    BooleanCollection values();
    
    boolean containsValue(final boolean p0);
    
    public interface Entry extends Map.Entry<Short, Boolean>
    {
        @Deprecated
        Short getKey();
        
        short getShortKey();
        
        @Deprecated
        Boolean getValue();
        
        boolean setValue(final boolean p0);
        
        boolean getBooleanValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
