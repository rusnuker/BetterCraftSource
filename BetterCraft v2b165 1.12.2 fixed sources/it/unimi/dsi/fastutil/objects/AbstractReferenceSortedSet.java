// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public abstract class AbstractReferenceSortedSet<K> extends AbstractReferenceSet<K> implements ReferenceSortedSet<K>
{
    protected AbstractReferenceSortedSet() {
    }
    
    @Deprecated
    @Override
    public ObjectBidirectionalIterator<K> objectIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ObjectBidirectionalIterator<K> iterator();
}
