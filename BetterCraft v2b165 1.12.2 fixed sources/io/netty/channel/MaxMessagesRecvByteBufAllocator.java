// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public interface MaxMessagesRecvByteBufAllocator extends RecvByteBufAllocator
{
    int maxMessagesPerRead();
    
    MaxMessagesRecvByteBufAllocator maxMessagesPerRead(final int p0);
}
