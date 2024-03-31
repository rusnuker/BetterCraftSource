// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2BooleanFunction extends Function<Float, Boolean>
{
    boolean put(final float p0, final boolean p1);
    
    boolean get(final float p0);
    
    boolean remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
