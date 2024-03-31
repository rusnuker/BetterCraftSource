// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigListIterator;

public interface LongBigListIterator extends LongBidirectionalIterator, BigListIterator<Long>
{
    void set(final long p0);
    
    void add(final long p0);
    
    void set(final Long p0);
    
    void add(final Long p0);
}
