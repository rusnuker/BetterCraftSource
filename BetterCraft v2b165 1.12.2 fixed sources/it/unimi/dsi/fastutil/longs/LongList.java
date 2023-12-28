// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.List;

public interface LongList extends List<Long>, Comparable<List<? extends Long>>, LongCollection
{
    LongListIterator iterator();
    
    @Deprecated
    LongListIterator longListIterator();
    
    @Deprecated
    LongListIterator longListIterator(final int p0);
    
    LongListIterator listIterator();
    
    LongListIterator listIterator(final int p0);
    
    @Deprecated
    LongList longSubList(final int p0, final int p1);
    
    LongList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final long[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final long[] p1);
    
    void addElements(final int p0, final long[] p1, final int p2, final int p3);
    
    boolean add(final long p0);
    
    void add(final int p0, final long p1);
    
    boolean addAll(final int p0, final LongCollection p1);
    
    boolean addAll(final int p0, final LongList p1);
    
    boolean addAll(final LongList p0);
    
    long getLong(final int p0);
    
    int indexOf(final long p0);
    
    int lastIndexOf(final long p0);
    
    long removeLong(final int p0);
    
    long set(final int p0, final long p1);
}
