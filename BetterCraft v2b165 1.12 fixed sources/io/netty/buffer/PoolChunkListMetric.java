// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

public interface PoolChunkListMetric extends Iterable<PoolChunkMetric>
{
    int minUsage();
    
    int maxUsage();
}
