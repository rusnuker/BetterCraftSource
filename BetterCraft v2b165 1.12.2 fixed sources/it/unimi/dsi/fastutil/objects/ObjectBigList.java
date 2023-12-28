// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BigList;

public interface ObjectBigList<K> extends BigList<K>, ObjectCollection<K>, Comparable<BigList<? extends K>>
{
    ObjectBigListIterator<K> iterator();
    
    ObjectBigListIterator<K> listIterator();
    
    ObjectBigListIterator<K> listIterator(final long p0);
    
    ObjectBigList<K> subList(final long p0, final long p1);
    
    void getElements(final long p0, final Object[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final K[][] p1);
    
    void addElements(final long p0, final K[][] p1, final long p2, final long p3);
}
