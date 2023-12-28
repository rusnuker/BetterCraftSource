// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.BigList;

public interface DoubleBigList extends BigList<Double>, DoubleCollection, Comparable<BigList<? extends Double>>
{
    DoubleBigListIterator iterator();
    
    DoubleBigListIterator listIterator();
    
    DoubleBigListIterator listIterator(final long p0);
    
    DoubleBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final double[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final double[][] p1);
    
    void addElements(final long p0, final double[][] p1, final long p2, final long p3);
    
    void add(final long p0, final double p1);
    
    boolean addAll(final long p0, final DoubleCollection p1);
    
    boolean addAll(final long p0, final DoubleBigList p1);
    
    boolean addAll(final DoubleBigList p0);
    
    double getDouble(final long p0);
    
    long indexOf(final double p0);
    
    long lastIndexOf(final double p0);
    
    double removeDouble(final long p0);
    
    double set(final long p0, final double p1);
}
