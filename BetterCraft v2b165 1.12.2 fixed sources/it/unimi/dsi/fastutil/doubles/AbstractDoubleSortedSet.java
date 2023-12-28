// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractDoubleSortedSet extends AbstractDoubleSet implements DoubleSortedSet
{
    protected AbstractDoubleSortedSet() {
    }
    
    @Deprecated
    @Override
    public DoubleSortedSet headSet(final Double to) {
        return this.headSet((double)to);
    }
    
    @Deprecated
    @Override
    public DoubleSortedSet tailSet(final Double from) {
        return this.tailSet((double)from);
    }
    
    @Deprecated
    @Override
    public DoubleSortedSet subSet(final Double from, final Double to) {
        return this.subSet((double)from, (double)to);
    }
    
    @Deprecated
    @Override
    public Double first() {
        return this.firstDouble();
    }
    
    @Deprecated
    @Override
    public Double last() {
        return this.lastDouble();
    }
    
    @Deprecated
    @Override
    public DoubleBidirectionalIterator doubleIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract DoubleBidirectionalIterator iterator();
}
