// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Reference2DoubleFunction<K> extends Function<K, Double>
{
    double put(final K p0, final double p1);
    
    double getDouble(final Object p0);
    
    double removeDouble(final Object p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
