// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2FloatMap extends Byte2FloatFunction, Map<Byte, Float>
{
    ObjectSet<Map.Entry<Byte, Float>> entrySet();
    
    ObjectSet<Entry> byte2FloatEntrySet();
    
    ByteSet keySet();
    
    FloatCollection values();
    
    boolean containsValue(final float p0);
    
    public interface Entry extends Map.Entry<Byte, Float>
    {
        @Deprecated
        Byte getKey();
        
        byte getByteKey();
        
        @Deprecated
        Float getValue();
        
        float setValue(final float p0);
        
        float getFloatValue();
    }
    
    public interface FastEntrySet extends ObjectSet<Entry>
    {
        ObjectIterator<Entry> fastIterator();
    }
}
