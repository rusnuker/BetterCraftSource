// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.Queue;

public interface PriorityQueue<T> extends Queue<T>
{
    boolean removeTyped(final T p0);
    
    boolean containsTyped(final T p0);
    
    void priorityChanged(final T p0);
}
