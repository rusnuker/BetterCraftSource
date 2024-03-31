// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.List;

public interface BooleanList extends List<Boolean>, Comparable<List<? extends Boolean>>, BooleanCollection
{
    BooleanListIterator iterator();
    
    @Deprecated
    BooleanListIterator booleanListIterator();
    
    @Deprecated
    BooleanListIterator booleanListIterator(final int p0);
    
    BooleanListIterator listIterator();
    
    BooleanListIterator listIterator(final int p0);
    
    @Deprecated
    BooleanList booleanSubList(final int p0, final int p1);
    
    BooleanList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final boolean[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final boolean[] p1);
    
    void addElements(final int p0, final boolean[] p1, final int p2, final int p3);
    
    boolean add(final boolean p0);
    
    void add(final int p0, final boolean p1);
    
    boolean addAll(final int p0, final BooleanCollection p1);
    
    boolean addAll(final int p0, final BooleanList p1);
    
    boolean addAll(final BooleanList p0);
    
    boolean getBoolean(final int p0);
    
    int indexOf(final boolean p0);
    
    int lastIndexOf(final boolean p0);
    
    boolean removeBoolean(final int p0);
    
    boolean set(final int p0, final boolean p1);
}
