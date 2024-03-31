// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.errorprone.annotations.CompatibleWith;
import com.google.common.annotations.Beta;

@Beta
public interface ValueGraph<N, V> extends Graph<N>
{
    V edgeValue(@CompatibleWith("N") final Object p0, @CompatibleWith("N") final Object p1);
    
    V edgeValueOrDefault(@CompatibleWith("N") final Object p0, @CompatibleWith("N") final Object p1, @Nullable final V p2);
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
