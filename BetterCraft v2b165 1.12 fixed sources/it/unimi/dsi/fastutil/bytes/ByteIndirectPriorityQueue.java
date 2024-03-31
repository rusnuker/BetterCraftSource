// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public interface ByteIndirectPriorityQueue extends IndirectPriorityQueue<Byte>
{
    ByteComparator comparator();
}
