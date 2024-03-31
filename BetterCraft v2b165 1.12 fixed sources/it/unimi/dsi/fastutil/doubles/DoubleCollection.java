// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Collection;

public interface DoubleCollection extends Collection<Double>, DoubleIterable
{
    DoubleIterator iterator();
    
    @Deprecated
    DoubleIterator doubleIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final double p0);
    
    double[] toDoubleArray();
    
    double[] toDoubleArray(final double[] p0);
    
    double[] toArray(final double[] p0);
    
    boolean add(final double p0);
    
    boolean rem(final double p0);
    
    boolean addAll(final DoubleCollection p0);
    
    boolean containsAll(final DoubleCollection p0);
    
    boolean removeAll(final DoubleCollection p0);
    
    boolean retainAll(final DoubleCollection p0);
}
