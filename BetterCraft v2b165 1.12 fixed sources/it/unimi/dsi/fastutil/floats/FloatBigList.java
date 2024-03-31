// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.BigList;

public interface FloatBigList extends BigList<Float>, FloatCollection, Comparable<BigList<? extends Float>>
{
    FloatBigListIterator iterator();
    
    FloatBigListIterator listIterator();
    
    FloatBigListIterator listIterator(final long p0);
    
    FloatBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final float[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final float[][] p1);
    
    void addElements(final long p0, final float[][] p1, final long p2, final long p3);
    
    void add(final long p0, final float p1);
    
    boolean addAll(final long p0, final FloatCollection p1);
    
    boolean addAll(final long p0, final FloatBigList p1);
    
    boolean addAll(final FloatBigList p0);
    
    float getFloat(final long p0);
    
    long indexOf(final float p0);
    
    long lastIndexOf(final float p0);
    
    float removeFloat(final long p0);
    
    float set(final long p0, final float p1);
}
