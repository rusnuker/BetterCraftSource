// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2BooleanFunction extends Function<Integer, Boolean>
{
    boolean put(final int p0, final boolean p1);
    
    boolean get(final int p0);
    
    boolean remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
