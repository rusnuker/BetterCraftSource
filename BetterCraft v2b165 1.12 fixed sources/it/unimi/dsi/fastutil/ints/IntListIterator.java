// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.ListIterator;

public interface IntListIterator extends ListIterator<Integer>, IntBidirectionalIterator
{
    void set(final int p0);
    
    void add(final int p0);
}
