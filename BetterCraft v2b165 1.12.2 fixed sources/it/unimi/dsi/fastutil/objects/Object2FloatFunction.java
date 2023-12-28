// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Object2FloatFunction<K> extends Function<K, Float>
{
    float put(final K p0, final float p1);
    
    float getFloat(final Object p0);
    
    float removeFloat(final Object p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
