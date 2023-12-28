// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.SortedSet;

public interface FloatSortedSet extends FloatSet, SortedSet<Float>
{
    FloatBidirectionalIterator iterator(final float p0);
    
    @Deprecated
    FloatBidirectionalIterator floatIterator();
    
    FloatBidirectionalIterator iterator();
    
    FloatSortedSet subSet(final Float p0, final Float p1);
    
    FloatSortedSet headSet(final Float p0);
    
    FloatSortedSet tailSet(final Float p0);
    
    FloatComparator comparator();
    
    FloatSortedSet subSet(final float p0, final float p1);
    
    FloatSortedSet headSet(final float p0);
    
    FloatSortedSet tailSet(final float p0);
    
    float firstFloat();
    
    float lastFloat();
}
