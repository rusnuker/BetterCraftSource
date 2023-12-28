// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;

public interface CharIterator extends Iterator<Character>
{
    char nextChar();
    
    int skip(final int p0);
}
