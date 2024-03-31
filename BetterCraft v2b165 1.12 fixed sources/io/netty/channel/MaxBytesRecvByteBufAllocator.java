// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import java.util.Map;

public interface MaxBytesRecvByteBufAllocator extends RecvByteBufAllocator
{
    int maxBytesPerRead();
    
    MaxBytesRecvByteBufAllocator maxBytesPerRead(final int p0);
    
    int maxBytesPerIndividualRead();
    
    MaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(final int p0);
    
    Map.Entry<Integer, Integer> maxBytesPerReadPair();
    
    MaxBytesRecvByteBufAllocator maxBytesPerReadPair(final int p0, final int p1);
}
