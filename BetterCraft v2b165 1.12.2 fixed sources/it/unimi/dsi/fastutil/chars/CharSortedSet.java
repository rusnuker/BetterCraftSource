// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.SortedSet;

public interface CharSortedSet extends CharSet, SortedSet<Character>
{
    CharBidirectionalIterator iterator(final char p0);
    
    @Deprecated
    CharBidirectionalIterator charIterator();
    
    CharBidirectionalIterator iterator();
    
    CharSortedSet subSet(final Character p0, final Character p1);
    
    CharSortedSet headSet(final Character p0);
    
    CharSortedSet tailSet(final Character p0);
    
    CharComparator comparator();
    
    CharSortedSet subSet(final char p0, final char p1);
    
    CharSortedSet headSet(final char p0);
    
    CharSortedSet tailSet(final char p0);
    
    char firstChar();
    
    char lastChar();
}
