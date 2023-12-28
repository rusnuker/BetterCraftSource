// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Reference2ByteFunction<K> extends Function<K, Byte>
{
    byte put(final K p0, final byte p1);
    
    byte getByte(final Object p0);
    
    byte removeByte(final Object p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
