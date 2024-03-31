// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2BooleanFunction extends Function<Short, Boolean>
{
    boolean put(final short p0, final boolean p1);
    
    boolean get(final short p0);
    
    boolean remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
