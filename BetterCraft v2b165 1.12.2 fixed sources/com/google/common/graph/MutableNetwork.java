// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CompatibleWith;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
public interface MutableNetwork<N, E> extends Network<N, E>
{
    @CanIgnoreReturnValue
    boolean addNode(final N p0);
    
    @CanIgnoreReturnValue
    boolean addEdge(final N p0, final N p1, final E p2);
    
    @CanIgnoreReturnValue
    boolean removeNode(@CompatibleWith("N") final Object p0);
    
    @CanIgnoreReturnValue
    boolean removeEdge(@CompatibleWith("E") final Object p0);
}
