// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;

public class MpscArrayQueue<E> extends MpscArrayQueueConsumerField<E> implements QueueProgressIndicators
{
    long p01;
    long p02;
    long p03;
    long p04;
    long p05;
    long p06;
    long p07;
    long p10;
    long p11;
    long p12;
    long p13;
    long p14;
    long p15;
    long p16;
    long p17;
    
    public MpscArrayQueue(final int capacity) {
        super(capacity);
    }
    
    public boolean offerIfBelowThreshold(final E e, final int threshold) {
        if (null == e) {
            throw new NullPointerException();
        }
        final long mask = this.mask;
        final long capacity = mask + 1L;
        long producerLimit = this.lvProducerLimit();
        long pIndex;
        do {
            pIndex = this.lvProducerIndex();
            final long available = producerLimit - pIndex;
            long size = capacity - available;
            if (size >= threshold) {
                final long cIndex = this.lvConsumerIndex();
                size = pIndex - cIndex;
                if (size >= threshold) {
                    return false;
                }
                producerLimit = cIndex + capacity;
                this.soProducerLimit(producerLimit);
            }
        } while (!this.casProducerIndex(pIndex, pIndex + 1L));
        final long offset = ConcurrentCircularArrayQueue.calcElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soElement(this.buffer, offset, e);
        return true;
    }
    
    @Override
    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final long mask = this.mask;
        long producerLimit = this.lvProducerLimit();
        long pIndex;
        do {
            pIndex = this.lvProducerIndex();
            if (pIndex >= producerLimit) {
                final long cIndex = this.lvConsumerIndex();
                producerLimit = cIndex + mask + 1L;
                if (pIndex >= producerLimit) {
                    return false;
                }
                this.soProducerLimit(producerLimit);
            }
        } while (!this.casProducerIndex(pIndex, pIndex + 1L));
        final long offset = ConcurrentCircularArrayQueue.calcElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soElement(this.buffer, offset, e);
        return true;
    }
    
    public final int failFastOffer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final long mask = this.mask;
        final long capacity = mask + 1L;
        final long pIndex = this.lvProducerIndex();
        long producerLimit = this.lvProducerLimit();
        if (pIndex >= producerLimit) {
            final long cIndex = this.lvConsumerIndex();
            producerLimit = cIndex + capacity;
            if (pIndex >= producerLimit) {
                return 1;
            }
            this.soProducerLimit(producerLimit);
        }
        if (!this.casProducerIndex(pIndex, pIndex + 1L)) {
            return -1;
        }
        final long offset = ConcurrentCircularArrayQueue.calcElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soElement(this.buffer, offset, e);
        return 0;
    }
    
    @Override
    public E poll() {
        final long cIndex = this.lpConsumerIndex();
        final long offset = this.calcElementOffset(cIndex);
        final E[] buffer = this.buffer;
        E e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (null == e) {
            if (cIndex == this.lvProducerIndex()) {
                return null;
            }
            do {
                e = UnsafeRefArrayAccess.lvElement(buffer, offset);
            } while (e == null);
        }
        UnsafeRefArrayAccess.spElement(buffer, offset, (E)null);
        this.soConsumerIndex(cIndex + 1L);
        return e;
    }
    
    @Override
    public E peek() {
        final E[] buffer = this.buffer;
        final long cIndex = this.lpConsumerIndex();
        final long offset = this.calcElementOffset(cIndex);
        E e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (null == e) {
            if (cIndex == this.lvProducerIndex()) {
                return null;
            }
            do {
                e = UnsafeRefArrayAccess.lvElement(buffer, offset);
            } while (e == null);
        }
        return e;
    }
    
    @Override
    public boolean relaxedOffer(final E e) {
        return this.offer(e);
    }
    
    @Override
    public E relaxedPoll() {
        final E[] buffer = this.buffer;
        final long cIndex = this.lpConsumerIndex();
        final long offset = this.calcElementOffset(cIndex);
        final E e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (null == e) {
            return null;
        }
        UnsafeRefArrayAccess.spElement(buffer, offset, (E)null);
        this.soConsumerIndex(cIndex + 1L);
        return e;
    }
    
    @Override
    public E relaxedPeek() {
        final E[] buffer = this.buffer;
        final long mask = this.mask;
        final long cIndex = this.lpConsumerIndex();
        return UnsafeRefArrayAccess.lvElement(buffer, ConcurrentCircularArrayQueue.calcElementOffset(cIndex, mask));
    }
    
    @Override
    public int drain(final MessagePassingQueue.Consumer<E> c) {
        return this.drain(c, this.capacity());
    }
    
    @Override
    public int fill(final MessagePassingQueue.Supplier<E> s) {
        long result = 0L;
        final int capacity = this.capacity();
        do {
            final int filled = this.fill(s, MpmcArrayQueue.RECOMENDED_OFFER_BATCH);
            if (filled == 0) {
                return (int)result;
            }
            result += filled;
        } while (result <= capacity);
        return (int)result;
    }
    
    @Override
    public int drain(final MessagePassingQueue.Consumer<E> c, final int limit) {
        final E[] buffer = this.buffer;
        final long mask = this.mask;
        final long cIndex = this.lpConsumerIndex();
        for (int i = 0; i < limit; ++i) {
            final long index = cIndex + i;
            final long offset = ConcurrentCircularArrayQueue.calcElementOffset(index, mask);
            final E e = UnsafeRefArrayAccess.lvElement(buffer, offset);
            if (null == e) {
                return i;
            }
            UnsafeRefArrayAccess.spElement(buffer, offset, (E)null);
            this.soConsumerIndex(index + 1L);
            c.accept(e);
        }
        return limit;
    }
    
    @Override
    public int fill(final MessagePassingQueue.Supplier<E> s, final int limit) {
        final long mask = this.mask;
        final long capacity = mask + 1L;
        long producerLimit = this.lvProducerLimit();
        int actualLimit = 0;
        long pIndex;
        do {
            pIndex = this.lvProducerIndex();
            long available = producerLimit - pIndex;
            if (available <= 0L) {
                final long cIndex = this.lvConsumerIndex();
                producerLimit = cIndex + capacity;
                available = producerLimit - pIndex;
                if (available <= 0L) {
                    return 0;
                }
                this.soProducerLimit(producerLimit);
            }
            actualLimit = Math.min((int)available, limit);
        } while (!this.casProducerIndex(pIndex, pIndex + actualLimit));
        final E[] buffer = this.buffer;
        for (int i = 0; i < actualLimit; ++i) {
            final long offset = ConcurrentCircularArrayQueue.calcElementOffset(pIndex + i, mask);
            UnsafeRefArrayAccess.soElement(buffer, offset, s.get());
        }
        return actualLimit;
    }
    
    @Override
    public void drain(final MessagePassingQueue.Consumer<E> c, final MessagePassingQueue.WaitStrategy w, final MessagePassingQueue.ExitCondition exit) {
        final E[] buffer = this.buffer;
        final long mask = this.mask;
        long cIndex = this.lpConsumerIndex();
        int counter = 0;
        while (exit.keepRunning()) {
            for (int i = 0; i < 4096; ++i) {
                final long offset = ConcurrentCircularArrayQueue.calcElementOffset(cIndex, mask);
                final E e = UnsafeRefArrayAccess.lvElement(buffer, offset);
                if (null == e) {
                    counter = w.idle(counter);
                }
                else {
                    ++cIndex;
                    counter = 0;
                    UnsafeRefArrayAccess.spElement(buffer, offset, (E)null);
                    this.soConsumerIndex(cIndex);
                    c.accept(e);
                }
            }
        }
    }
    
    @Override
    public void fill(final MessagePassingQueue.Supplier<E> s, final MessagePassingQueue.WaitStrategy w, final MessagePassingQueue.ExitCondition exit) {
        int idleCounter = 0;
        while (exit.keepRunning()) {
            if (this.fill(s, MpmcArrayQueue.RECOMENDED_OFFER_BATCH) == 0) {
                idleCounter = w.idle(idleCounter);
            }
            else {
                idleCounter = 0;
            }
        }
    }
}
