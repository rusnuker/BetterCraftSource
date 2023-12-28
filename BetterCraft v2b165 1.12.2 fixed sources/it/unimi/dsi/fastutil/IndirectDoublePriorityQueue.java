// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

import java.util.Comparator;

public interface IndirectDoublePriorityQueue<K> extends IndirectPriorityQueue<K>
{
    Comparator<? super K> secondaryComparator();
    
    int secondaryFirst();
    
    int secondaryLast();
    
    int secondaryFront(final int[] p0);
}
