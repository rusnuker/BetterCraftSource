// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Set;

public interface ReferenceSet<K> extends ReferenceCollection<K>, Set<K>
{
    ObjectIterator<K> iterator();
    
    boolean remove(final Object p0);
}
