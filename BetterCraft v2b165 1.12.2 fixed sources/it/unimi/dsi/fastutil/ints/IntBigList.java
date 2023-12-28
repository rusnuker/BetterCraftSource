// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigList;

public interface IntBigList extends BigList<Integer>, IntCollection, Comparable<BigList<? extends Integer>>
{
    IntBigListIterator iterator();
    
    IntBigListIterator listIterator();
    
    IntBigListIterator listIterator(final long p0);
    
    IntBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final int[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final int[][] p1);
    
    void addElements(final long p0, final int[][] p1, final long p2, final long p3);
    
    void add(final long p0, final int p1);
    
    boolean addAll(final long p0, final IntCollection p1);
    
    boolean addAll(final long p0, final IntBigList p1);
    
    boolean addAll(final IntBigList p0);
    
    int getInt(final long p0);
    
    long indexOf(final int p0);
    
    long lastIndexOf(final int p0);
    
    int removeInt(final long p0);
    
    int set(final long p0, final int p1);
}
