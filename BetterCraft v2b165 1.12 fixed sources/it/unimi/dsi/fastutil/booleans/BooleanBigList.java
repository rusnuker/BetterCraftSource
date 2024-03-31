// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.BigList;

public interface BooleanBigList extends BigList<Boolean>, BooleanCollection, Comparable<BigList<? extends Boolean>>
{
    BooleanBigListIterator iterator();
    
    BooleanBigListIterator listIterator();
    
    BooleanBigListIterator listIterator(final long p0);
    
    BooleanBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final boolean[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final boolean[][] p1);
    
    void addElements(final long p0, final boolean[][] p1, final long p2, final long p3);
    
    void add(final long p0, final boolean p1);
    
    boolean addAll(final long p0, final BooleanCollection p1);
    
    boolean addAll(final long p0, final BooleanBigList p1);
    
    boolean addAll(final BooleanBigList p0);
    
    boolean getBoolean(final long p0);
    
    long indexOf(final boolean p0);
    
    long lastIndexOf(final boolean p0);
    
    boolean removeBoolean(final long p0);
    
    boolean set(final long p0, final boolean p1);
}
