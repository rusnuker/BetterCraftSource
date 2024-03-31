// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.BigList;

public interface ShortBigList extends BigList<Short>, ShortCollection, Comparable<BigList<? extends Short>>
{
    ShortBigListIterator iterator();
    
    ShortBigListIterator listIterator();
    
    ShortBigListIterator listIterator(final long p0);
    
    ShortBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final short[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final short[][] p1);
    
    void addElements(final long p0, final short[][] p1, final long p2, final long p3);
    
    void add(final long p0, final short p1);
    
    boolean addAll(final long p0, final ShortCollection p1);
    
    boolean addAll(final long p0, final ShortBigList p1);
    
    boolean addAll(final ShortBigList p0);
    
    short getShort(final long p0);
    
    long indexOf(final short p0);
    
    long lastIndexOf(final short p0);
    
    short removeShort(final long p0);
    
    short set(final long p0, final short p1);
}
