// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface IntPriorityQueue extends PriorityQueue<Integer>
{
    void enqueue(final int p0);
    
    int dequeueInt();
    
    int firstInt();
    
    int lastInt();
    
    IntComparator comparator();
}
