// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

public interface PriorityQueueNode
{
    public static final int INDEX_NOT_IN_QUEUE = -1;
    
    int priorityQueueIndex(final DefaultPriorityQueue<?> p0);
    
    void priorityQueueIndex(final DefaultPriorityQueue<?> p0, final int p1);
}
