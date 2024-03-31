// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public abstract class AbstractObjectSortedSet<K> extends AbstractObjectSet<K> implements ObjectSortedSet<K>
{
    protected AbstractObjectSortedSet() {
    }
    
    @Deprecated
    @Override
    public ObjectBidirectionalIterator<K> objectIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ObjectBidirectionalIterator<K> iterator();
}
