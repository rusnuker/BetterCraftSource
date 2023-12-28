// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.ListIterator;

public interface ShortListIterator extends ListIterator<Short>, ShortBidirectionalIterator
{
    void set(final short p0);
    
    void add(final short p0);
}
