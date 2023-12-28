// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2BooleanFunction extends Function<Byte, Boolean>
{
    boolean put(final byte p0, final boolean p1);
    
    boolean get(final byte p0);
    
    boolean remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
