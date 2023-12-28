// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.List;

public interface IntList extends List<Integer>, Comparable<List<? extends Integer>>, IntCollection
{
    IntListIterator iterator();
    
    @Deprecated
    IntListIterator intListIterator();
    
    @Deprecated
    IntListIterator intListIterator(final int p0);
    
    IntListIterator listIterator();
    
    IntListIterator listIterator(final int p0);
    
    @Deprecated
    IntList intSubList(final int p0, final int p1);
    
    IntList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final int[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final int[] p1);
    
    void addElements(final int p0, final int[] p1, final int p2, final int p3);
    
    boolean add(final int p0);
    
    void add(final int p0, final int p1);
    
    boolean addAll(final int p0, final IntCollection p1);
    
    boolean addAll(final int p0, final IntList p1);
    
    boolean addAll(final IntList p0);
    
    int getInt(final int p0);
    
    int indexOf(final int p0);
    
    int lastIndexOf(final int p0);
    
    int removeInt(final int p0);
    
    int set(final int p0, final int p1);
}
