// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface ShortPriorityQueue extends PriorityQueue<Short>
{
    void enqueue(final short p0);
    
    short dequeueShort();
    
    short firstShort();
    
    short lastShort();
    
    ShortComparator comparator();
}
