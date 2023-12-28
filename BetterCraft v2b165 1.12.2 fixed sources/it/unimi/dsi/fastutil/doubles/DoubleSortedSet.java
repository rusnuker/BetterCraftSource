// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.SortedSet;

public interface DoubleSortedSet extends DoubleSet, SortedSet<Double>
{
    DoubleBidirectionalIterator iterator(final double p0);
    
    @Deprecated
    DoubleBidirectionalIterator doubleIterator();
    
    DoubleBidirectionalIterator iterator();
    
    DoubleSortedSet subSet(final Double p0, final Double p1);
    
    DoubleSortedSet headSet(final Double p0);
    
    DoubleSortedSet tailSet(final Double p0);
    
    DoubleComparator comparator();
    
    DoubleSortedSet subSet(final double p0, final double p1);
    
    DoubleSortedSet headSet(final double p0);
    
    DoubleSortedSet tailSet(final double p0);
    
    double firstDouble();
    
    double lastDouble();
}
