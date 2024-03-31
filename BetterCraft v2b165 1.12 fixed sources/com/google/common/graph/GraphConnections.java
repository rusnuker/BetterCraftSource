// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Set;

interface GraphConnections<N, V>
{
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    @Nullable
    V value(final Object p0);
    
    void removePredecessor(final Object p0);
    
    @CanIgnoreReturnValue
    V removeSuccessor(final Object p0);
    
    void addPredecessor(final N p0, final V p1);
    
    @CanIgnoreReturnValue
    V addSuccessor(final N p0, final V p1);
}
