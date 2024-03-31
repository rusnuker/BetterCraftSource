// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

public interface PoolSubpageMetric
{
    int maxNumElements();
    
    int numAvailable();
    
    int elementSize();
    
    int pageSize();
}
