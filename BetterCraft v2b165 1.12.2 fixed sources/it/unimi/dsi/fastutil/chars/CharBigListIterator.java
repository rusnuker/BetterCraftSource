// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.BigListIterator;

public interface CharBigListIterator extends CharBidirectionalIterator, BigListIterator<Character>
{
    void set(final char p0);
    
    void add(final char p0);
    
    void set(final Character p0);
    
    void add(final Character p0);
}
