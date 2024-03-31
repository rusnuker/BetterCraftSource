// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import java.util.Iterator;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;

public abstract class ConcurrentCircularArrayQueue<E> extends ConcurrentCircularArrayQueueL0Pad<E>
{
    protected final long mask;
    protected final E[] buffer;
    
    public ConcurrentCircularArrayQueue(final int capacity) {
        final int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
        this.mask = actualCapacity - 1;
        this.buffer = CircularArrayOffsetCalculator.allocate(actualCapacity);
    }
    
    protected final long calcElementOffset(final long index) {
        return calcElementOffset(index, this.mask);
    }
    
    protected static long calcElementOffset(final long index, final long mask) {
        return CircularArrayOffsetCalculator.calcElementOffset(index, mask);
    }
    
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return this.getClass().getName();
    }
    
    @Override
    public void clear() {
        while (true) {
            if (this.poll() == null) {
                if (!this.isEmpty()) {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public int capacity() {
        return (int)(this.mask + 1L);
    }
    
    @Override
    public final int size() {
        return IndexedQueueSizeUtil.size(this);
    }
    
    @Override
    public final boolean isEmpty() {
        return IndexedQueueSizeUtil.isEmpty(this);
    }
    
    @Override
    public final long currentProducerIndex() {
        return this.lvProducerIndex();
    }
    
    @Override
    public final long currentConsumerIndex() {
        return this.lvConsumerIndex();
    }
}
