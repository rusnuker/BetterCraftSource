// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2BooleanFunction extends Function<Long, Boolean>
{
    boolean put(final long p0, final boolean p1);
    
    boolean get(final long p0);
    
    boolean remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
