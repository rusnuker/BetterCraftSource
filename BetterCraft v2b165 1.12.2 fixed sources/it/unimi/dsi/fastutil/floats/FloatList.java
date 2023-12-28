// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.List;

public interface FloatList extends List<Float>, Comparable<List<? extends Float>>, FloatCollection
{
    FloatListIterator iterator();
    
    @Deprecated
    FloatListIterator floatListIterator();
    
    @Deprecated
    FloatListIterator floatListIterator(final int p0);
    
    FloatListIterator listIterator();
    
    FloatListIterator listIterator(final int p0);
    
    @Deprecated
    FloatList floatSubList(final int p0, final int p1);
    
    FloatList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final float[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final float[] p1);
    
    void addElements(final int p0, final float[] p1, final int p2, final int p3);
    
    boolean add(final float p0);
    
    void add(final int p0, final float p1);
    
    boolean addAll(final int p0, final FloatCollection p1);
    
    boolean addAll(final int p0, final FloatList p1);
    
    boolean addAll(final FloatList p0);
    
    float getFloat(final int p0);
    
    int indexOf(final float p0);
    
    int lastIndexOf(final float p0);
    
    float removeFloat(final int p0);
    
    float set(final int p0, final float p1);
}
