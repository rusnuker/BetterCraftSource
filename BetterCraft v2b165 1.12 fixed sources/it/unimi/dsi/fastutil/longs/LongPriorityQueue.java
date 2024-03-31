// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface LongPriorityQueue extends PriorityQueue<Long>
{
    void enqueue(final long p0);
    
    long dequeueLong();
    
    long firstLong();
    
    long lastLong();
    
    LongComparator comparator();
}
