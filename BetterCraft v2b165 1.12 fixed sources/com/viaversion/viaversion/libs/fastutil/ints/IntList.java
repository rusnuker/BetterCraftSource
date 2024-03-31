// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.List;

public interface IntList extends List<Integer>, Comparable<List<? extends Integer>>, IntCollection
{
    IntListIterator iterator();
    
    IntListIterator listIterator();
    
    IntListIterator listIterator(final int p0);
    
    IntList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final int[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final int[] p1);
    
    void addElements(final int p0, final int[] p1, final int p2, final int p3);
    
    default void setElements(final int[] a) {
        this.setElements(0, a);
    }
    
    default void setElements(final int index, final int[] a) {
        this.setElements(index, a, 0, a.length);
    }
    
    default void setElements(final int index, final int[] a, final int offset, final int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
        IntArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        final IntListIterator iter = this.listIterator(index);
        int i = 0;
        while (i < length) {
            iter.nextInt();
            iter.set(a[offset + i++]);
        }
    }
    
    boolean add(final int p0);
    
    void add(final int p0, final int p1);
    
    @Deprecated
    default void add(final int index, final Integer key) {
        this.add(index, (int)key);
    }
    
    boolean addAll(final int p0, final IntCollection p1);
    
    boolean addAll(final int p0, final IntList p1);
    
    boolean addAll(final IntList p0);
    
    int set(final int p0, final int p1);
    
    int getInt(final int p0);
    
    int indexOf(final int p0);
    
    int lastIndexOf(final int p0);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Integer get(final int index) {
        return this.getInt(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((int)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((int)o);
    }
    
    @Deprecated
    default boolean add(final Integer k) {
        return this.add((int)k);
    }
    
    int removeInt(final int p0);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Integer remove(final int index) {
        return this.removeInt(index);
    }
    
    @Deprecated
    default Integer set(final int index, final Integer k) {
        return this.set(index, (int)k);
    }
    
    @Deprecated
    default void sort(final Comparator<? super Integer> comparator) {
        this.sort(IntComparators.asIntComparator(comparator));
    }
    
    default void sort(final IntComparator comparator) {
        if (comparator == null) {
            this.unstableSort(comparator);
        }
        else {
            final int[] elements = this.toIntArray();
            IntArrays.stableSort(elements, comparator);
            this.setElements(elements);
        }
    }
    
    @Deprecated
    default void unstableSort(final Comparator<? super Integer> comparator) {
        this.unstableSort(IntComparators.asIntComparator(comparator));
    }
    
    default void unstableSort(final IntComparator comparator) {
        final int[] elements = this.toIntArray();
        if (comparator == null) {
            IntArrays.unstableSort(elements);
        }
        else {
            IntArrays.unstableSort(elements, comparator);
        }
        this.setElements(elements);
    }
}
