// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Collection;

public interface ObjectCollection<K> extends Collection<K>, ObjectIterable<K>
{
    ObjectIterator<K> iterator();
    
    @Deprecated
    ObjectIterator<K> objectIterator();
    
     <T> T[] toArray(final T[] p0);
}
