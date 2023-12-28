// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.List;

public interface ShortList extends List<Short>, Comparable<List<? extends Short>>, ShortCollection
{
    ShortListIterator iterator();
    
    @Deprecated
    ShortListIterator shortListIterator();
    
    @Deprecated
    ShortListIterator shortListIterator(final int p0);
    
    ShortListIterator listIterator();
    
    ShortListIterator listIterator(final int p0);
    
    @Deprecated
    ShortList shortSubList(final int p0, final int p1);
    
    ShortList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final short[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final short[] p1);
    
    void addElements(final int p0, final short[] p1, final int p2, final int p3);
    
    boolean add(final short p0);
    
    void add(final int p0, final short p1);
    
    boolean addAll(final int p0, final ShortCollection p1);
    
    boolean addAll(final int p0, final ShortList p1);
    
    boolean addAll(final ShortList p0);
    
    short getShort(final int p0);
    
    int indexOf(final short p0);
    
    int lastIndexOf(final short p0);
    
    short removeShort(final int p0);
    
    short set(final int p0, final short p1);
}
