// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2ShortFunction extends Function<Short, Short>
{
    short put(final short p0, final short p1);
    
    short get(final short p0);
    
    short remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
