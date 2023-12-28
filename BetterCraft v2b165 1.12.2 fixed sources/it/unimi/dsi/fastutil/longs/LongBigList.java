// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigList;

public interface LongBigList extends BigList<Long>, LongCollection, Comparable<BigList<? extends Long>>
{
    LongBigListIterator iterator();
    
    LongBigListIterator listIterator();
    
    LongBigListIterator listIterator(final long p0);
    
    LongBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final long[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final long[][] p1);
    
    void addElements(final long p0, final long[][] p1, final long p2, final long p3);
    
    void add(final long p0, final long p1);
    
    boolean addAll(final long p0, final LongCollection p1);
    
    boolean addAll(final long p0, final LongBigList p1);
    
    boolean addAll(final LongBigList p0);
    
    long getLong(final long p0);
    
    long indexOf(final long p0);
    
    long lastIndexOf(final long p0);
    
    long removeLong(final long p0);
    
    long set(final long p0, final long p1);
}
