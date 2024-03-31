// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

public interface ByteBufAllocatorMetric
{
    long usedHeapMemory();
    
    long usedDirectMemory();
}
