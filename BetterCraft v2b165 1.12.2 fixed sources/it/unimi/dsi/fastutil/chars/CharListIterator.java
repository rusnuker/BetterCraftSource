// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.ListIterator;

public interface CharListIterator extends ListIterator<Character>, CharBidirectionalIterator
{
    void set(final char p0);
    
    void add(final char p0);
}
