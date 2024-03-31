// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Set;

public interface CharSet extends CharCollection, Set<Character>
{
    CharIterator iterator();
    
    boolean remove(final char p0);
}
