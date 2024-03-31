// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface CharPriorityQueue extends PriorityQueue<Character>
{
    void enqueue(final char p0);
    
    char dequeueChar();
    
    char firstChar();
    
    char lastChar();
    
    CharComparator comparator();
}
