// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.ListIterator;

public interface BooleanListIterator extends ListIterator<Boolean>, BooleanBidirectionalIterator
{
    void set(final boolean p0);
    
    void add(final boolean p0);
}
