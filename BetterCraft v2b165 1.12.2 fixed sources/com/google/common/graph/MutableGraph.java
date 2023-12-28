// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CompatibleWith;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
public interface MutableGraph<N> extends Graph<N>
{
    @CanIgnoreReturnValue
    boolean addNode(final N p0);
    
    @CanIgnoreReturnValue
    boolean putEdge(final N p0, final N p1);
    
    @CanIgnoreReturnValue
    boolean removeNode(@CompatibleWith("N") final Object p0);
    
    @CanIgnoreReturnValue
    boolean removeEdge(@CompatibleWith("N") final Object p0, @CompatibleWith("N") final Object p1);
}
