// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface BytePriorityQueue extends PriorityQueue<Byte>
{
    void enqueue(final byte p0);
    
    byte dequeueByte();
    
    byte firstByte();
    
    byte lastByte();
    
    ByteComparator comparator();
}
