// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractFloatSortedSet extends AbstractFloatSet implements FloatSortedSet
{
    protected AbstractFloatSortedSet() {
    }
    
    @Deprecated
    @Override
    public FloatSortedSet headSet(final Float to) {
        return this.headSet((float)to);
    }
    
    @Deprecated
    @Override
    public FloatSortedSet tailSet(final Float from) {
        return this.tailSet((float)from);
    }
    
    @Deprecated
    @Override
    public FloatSortedSet subSet(final Float from, final Float to) {
        return this.subSet((float)from, (float)to);
    }
    
    @Deprecated
    @Override
    public Float first() {
        return this.firstFloat();
    }
    
    @Deprecated
    @Override
    public Float last() {
        return this.lastFloat();
    }
    
    @Deprecated
    @Override
    public FloatBidirectionalIterator floatIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract FloatBidirectionalIterator iterator();
}
