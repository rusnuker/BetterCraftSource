// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractShortSortedSet extends AbstractShortSet implements ShortSortedSet
{
    protected AbstractShortSortedSet() {
    }
    
    @Deprecated
    @Override
    public ShortSortedSet headSet(final Short to) {
        return this.headSet((short)to);
    }
    
    @Deprecated
    @Override
    public ShortSortedSet tailSet(final Short from) {
        return this.tailSet((short)from);
    }
    
    @Deprecated
    @Override
    public ShortSortedSet subSet(final Short from, final Short to) {
        return this.subSet((short)from, (short)to);
    }
    
    @Deprecated
    @Override
    public Short first() {
        return this.firstShort();
    }
    
    @Deprecated
    @Override
    public Short last() {
        return this.lastShort();
    }
    
    @Deprecated
    @Override
    public ShortBidirectionalIterator shortIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ShortBidirectionalIterator iterator();
}
