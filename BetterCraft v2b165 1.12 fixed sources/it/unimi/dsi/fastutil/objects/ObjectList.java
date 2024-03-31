// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.List;

public interface ObjectList<K> extends List<K>, Comparable<List<? extends K>>, ObjectCollection<K>
{
    ObjectListIterator<K> iterator();
    
    @Deprecated
    ObjectListIterator<K> objectListIterator();
    
    @Deprecated
    ObjectListIterator<K> objectListIterator(final int p0);
    
    ObjectListIterator<K> listIterator();
    
    ObjectListIterator<K> listIterator(final int p0);
    
    @Deprecated
    ObjectList<K> objectSubList(final int p0, final int p1);
    
    ObjectList<K> subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final Object[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final K[] p1);
    
    void addElements(final int p0, final K[] p1, final int p2, final int p3);
}
