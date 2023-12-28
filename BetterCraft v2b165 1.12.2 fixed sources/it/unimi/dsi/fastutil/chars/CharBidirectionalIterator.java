// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface CharBidirectionalIterator extends CharIterator, ObjectBidirectionalIterator<Character>
{
    char previousChar();
    
    int back(final int p0);
}
