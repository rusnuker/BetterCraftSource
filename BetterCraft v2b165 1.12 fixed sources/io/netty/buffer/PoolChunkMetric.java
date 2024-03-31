// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

public interface PoolChunkMetric
{
    int usage();
    
    int chunkSize();
    
    int freeBytes();
}
