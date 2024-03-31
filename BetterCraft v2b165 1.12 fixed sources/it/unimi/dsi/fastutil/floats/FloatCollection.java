// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Collection;

public interface FloatCollection extends Collection<Float>, FloatIterable
{
    FloatIterator iterator();
    
    @Deprecated
    FloatIterator floatIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final float p0);
    
    float[] toFloatArray();
    
    float[] toFloatArray(final float[] p0);
    
    float[] toArray(final float[] p0);
    
    boolean add(final float p0);
    
    boolean rem(final float p0);
    
    boolean addAll(final FloatCollection p0);
    
    boolean containsAll(final FloatCollection p0);
    
    boolean removeAll(final FloatCollection p0);
    
    boolean retainAll(final FloatCollection p0);
}
