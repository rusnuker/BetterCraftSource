// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Collection;

public interface CharCollection extends Collection<Character>, CharIterable
{
    CharIterator iterator();
    
    @Deprecated
    CharIterator charIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final char p0);
    
    char[] toCharArray();
    
    char[] toCharArray(final char[] p0);
    
    char[] toArray(final char[] p0);
    
    boolean add(final char p0);
    
    boolean rem(final char p0);
    
    boolean addAll(final CharCollection p0);
    
    boolean containsAll(final CharCollection p0);
    
    boolean removeAll(final CharCollection p0);
    
    boolean retainAll(final CharCollection p0);
}
