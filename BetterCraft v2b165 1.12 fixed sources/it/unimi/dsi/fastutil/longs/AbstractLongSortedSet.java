// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractLongSortedSet extends AbstractLongSet implements LongSortedSet
{
    protected AbstractLongSortedSet() {
    }
    
    @Deprecated
    @Override
    public LongSortedSet headSet(final Long to) {
        return this.headSet((long)to);
    }
    
    @Deprecated
    @Override
    public LongSortedSet tailSet(final Long from) {
        return this.tailSet((long)from);
    }
    
    @Deprecated
    @Override
    public LongSortedSet subSet(final Long from, final Long to) {
        return this.subSet((long)from, (long)to);
    }
    
    @Deprecated
    @Override
    public Long first() {
        return this.firstLong();
    }
    
    @Deprecated
    @Override
    public Long last() {
        return this.lastLong();
    }
    
    @Deprecated
    @Override
    public LongBidirectionalIterator longIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract LongBidirectionalIterator iterator();
}
