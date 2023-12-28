// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.BigListIterator;

public interface BooleanBigListIterator extends BooleanBidirectionalIterator, BigListIterator<Boolean>
{
    void set(final boolean p0);
    
    void add(final boolean p0);
    
    void set(final Boolean p0);
    
    void add(final Boolean p0);
}
