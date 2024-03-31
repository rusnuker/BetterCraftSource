// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.List;

public interface DoubleList extends List<Double>, Comparable<List<? extends Double>>, DoubleCollection
{
    DoubleListIterator iterator();
    
    @Deprecated
    DoubleListIterator doubleListIterator();
    
    @Deprecated
    DoubleListIterator doubleListIterator(final int p0);
    
    DoubleListIterator listIterator();
    
    DoubleListIterator listIterator(final int p0);
    
    @Deprecated
    DoubleList doubleSubList(final int p0, final int p1);
    
    DoubleList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final double[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final double[] p1);
    
    void addElements(final int p0, final double[] p1, final int p2, final int p3);
    
    boolean add(final double p0);
    
    void add(final int p0, final double p1);
    
    boolean addAll(final int p0, final DoubleCollection p1);
    
    boolean addAll(final int p0, final DoubleList p1);
    
    boolean addAll(final DoubleList p0);
    
    double getDouble(final int p0);
    
    int indexOf(final double p0);
    
    int lastIndexOf(final double p0);
    
    double removeDouble(final int p0);
    
    double set(final int p0, final double p1);
}
