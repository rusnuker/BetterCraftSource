// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractIntSortedSet extends AbstractIntSet implements IntSortedSet
{
    protected AbstractIntSortedSet() {
    }
    
    @Deprecated
    @Override
    public IntSortedSet headSet(final Integer to) {
        return this.headSet((int)to);
    }
    
    @Deprecated
    @Override
    public IntSortedSet tailSet(final Integer from) {
        return this.tailSet((int)from);
    }
    
    @Deprecated
    @Override
    public IntSortedSet subSet(final Integer from, final Integer to) {
        return this.subSet((int)from, (int)to);
    }
    
    @Deprecated
    @Override
    public Integer first() {
        return this.firstInt();
    }
    
    @Deprecated
    @Override
    public Integer last() {
        return this.lastInt();
    }
    
    @Deprecated
    @Override
    public IntBidirectionalIterator intIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract IntBidirectionalIterator iterator();
}
