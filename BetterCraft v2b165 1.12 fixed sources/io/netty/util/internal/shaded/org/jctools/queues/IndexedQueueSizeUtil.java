// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

final class IndexedQueueSizeUtil
{
    private IndexedQueueSizeUtil() {
    }
    
    static int size(final IndexedQueue iq) {
        long after = iq.lvConsumerIndex();
        long before;
        long currentProducerIndex;
        do {
            before = after;
            currentProducerIndex = iq.lvProducerIndex();
            after = iq.lvConsumerIndex();
        } while (before != after);
        final long size = currentProducerIndex - after;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)size;
    }
    
    static boolean isEmpty(final IndexedQueue iq) {
        return iq.lvConsumerIndex() == iq.lvProducerIndex();
    }
    
    protected interface IndexedQueue
    {
        long lvConsumerIndex();
        
        long lvProducerIndex();
    }
}
