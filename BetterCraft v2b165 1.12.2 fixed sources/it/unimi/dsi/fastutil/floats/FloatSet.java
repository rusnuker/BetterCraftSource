// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Set;

public interface FloatSet extends FloatCollection, Set<Float>
{
    FloatIterator iterator();
    
    boolean remove(final float p0);
}
