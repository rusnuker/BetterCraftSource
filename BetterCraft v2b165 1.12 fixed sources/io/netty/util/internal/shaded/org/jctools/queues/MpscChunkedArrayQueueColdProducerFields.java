// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.Pow2;

abstract class MpscChunkedArrayQueueColdProducerFields<E> extends BaseMpscLinkedArrayQueue<E>
{
    protected final long maxQueueCapacity;
    
    public MpscChunkedArrayQueueColdProducerFields(final int initialCapacity, final int maxCapacity) {
        super(initialCapacity);
        if (maxCapacity < 4) {
            throw new IllegalArgumentException("Max capacity must be 4 or more");
        }
        if (Pow2.roundToPowerOfTwo(initialCapacity) >= Pow2.roundToPowerOfTwo(maxCapacity)) {
            throw new IllegalArgumentException("Initial capacity cannot exceed maximum capacity(both rounded up to a power of 2)");
        }
        this.maxQueueCapacity = (long)Pow2.roundToPowerOfTwo(maxCapacity) << 1;
    }
}
