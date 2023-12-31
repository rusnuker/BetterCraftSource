// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import com.google.common.annotations.Beta;

@Beta
public interface Graph<N>
{
    Set<N> nodes();
    
    Set<EndpointPair<N>> edges();
    
    boolean isDirected();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    Set<N> adjacentNodes(@CompatibleWith("N") final Object p0);
    
    Set<N> predecessors(@CompatibleWith("N") final Object p0);
    
    Set<N> successors(@CompatibleWith("N") final Object p0);
    
    int degree(@CompatibleWith("N") final Object p0);
    
    int inDegree(@CompatibleWith("N") final Object p0);
    
    int outDegree(@CompatibleWith("N") final Object p0);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
