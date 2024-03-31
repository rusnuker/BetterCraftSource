// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.Pow2;

public class MpscChunkedArrayQueue<E> extends MpscChunkedArrayQueueColdProducerFields<E>
{
    long p0;
    long p1;
    long p2;
    long p3;
    long p4;
    long p5;
    long p6;
    long p7;
    long p10;
    long p11;
    long p12;
    long p13;
    long p14;
    long p15;
    long p16;
    long p17;
    
    public MpscChunkedArrayQueue(final int maxCapacity) {
        super(Math.max(2, Math.min(1024, Pow2.roundToPowerOfTwo(maxCapacity / 8))), maxCapacity);
    }
    
    public MpscChunkedArrayQueue(final int initialCapacity, final int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }
    
    @Override
    protected long availableInQueue(final long pIndex, final long cIndex) {
        return this.maxQueueCapacity - (pIndex - cIndex);
    }
    
    @Override
    public int capacity() {
        return (int)(this.maxQueueCapacity / 2L);
    }
    
    @Override
    protected int getNextBufferSize(final E[] buffer) {
        return buffer.length;
    }
    
    @Override
    protected long getCurrentBufferCapacity(final long mask) {
        return mask;
    }
}
