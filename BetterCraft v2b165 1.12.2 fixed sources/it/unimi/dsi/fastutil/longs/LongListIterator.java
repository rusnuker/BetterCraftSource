// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.ListIterator;

public interface LongListIterator extends ListIterator<Long>, LongBidirectionalIterator
{
    void set(final long p0);
    
    void add(final long p0);
}
