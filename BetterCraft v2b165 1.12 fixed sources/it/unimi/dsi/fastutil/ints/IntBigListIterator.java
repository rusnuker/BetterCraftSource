// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigListIterator;

public interface IntBigListIterator extends IntBidirectionalIterator, BigListIterator<Integer>
{
    void set(final int p0);
    
    void add(final int p0);
    
    void set(final Integer p0);
    
    void add(final Integer p0);
}
