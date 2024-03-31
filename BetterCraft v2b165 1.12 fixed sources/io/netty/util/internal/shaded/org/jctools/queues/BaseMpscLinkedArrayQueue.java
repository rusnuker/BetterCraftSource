// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import java.lang.reflect.Field;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
import java.util.Iterator;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;

public abstract class BaseMpscLinkedArrayQueue<E> extends BaseMpscLinkedArrayQueueColdProducerFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators
{
    private static final long P_INDEX_OFFSET;
    private static final long C_INDEX_OFFSET;
    private static final long P_LIMIT_OFFSET;
    private static final Object JUMP;
    
    public BaseMpscLinkedArrayQueue(final int initialCapacity) {
        if (initialCapacity < 2) {
            throw new IllegalArgumentException("Initial capacity must be 2 or more");
        }
        final int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
        final long mask = p2capacity - 1 << 1;
        final E[] buffer = CircularArrayOffsetCalculator.allocate(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        this.consumerBuffer = buffer;
        this.soProducerLimit(this.consumerMask = mask);
    }
    
    @Override
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return this.getClass().getName();
    }
    
    @Override
    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        while (true) {
            final long producerLimit = this.lvProducerLimit();
            final long pIndex = this.lvProducerIndex();
            if ((pIndex & 0x1L) == 0x1L) {
                continue;
            }
            final long mask = this.producerMask;
            final E[] buffer = this.producerBuffer;
            if (producerLimit <= pIndex) {
                final int result = this.offerSlowPath(mask, pIndex, producerLimit);
                switch (result) {
                    case 1: {
                        continue;
                    }
                    case 2: {
                        return false;
                    }
                    case 3: {
                        this.resize(mask, buffer, pIndex, e);
                        return true;
                    }
                }
            }
            if (this.casProducerIndex(pIndex, pIndex + 2L)) {
                final long offset = modifiedCalcElementOffset(pIndex, mask);
                UnsafeRefArrayAccess.soElement(buffer, offset, e);
                return true;
            }
        }
    }
    
    private int offerSlowPath(final long mask, final long pIndex, final long producerLimit) {
        final long cIndex = this.lvConsumerIndex();
        final long bufferCapacity = this.getCurrentBufferCapacity(mask);
        int result = 0;
        if (cIndex + bufferCapacity > pIndex) {
            if (!this.casProducerLimit(producerLimit, cIndex + bufferCapacity)) {
                result = 1;
            }
        }
        else if (this.availableInQueue(pIndex, cIndex) <= 0L) {
            result = 2;
        }
        else if (this.casProducerIndex(pIndex, pIndex + 1L)) {
            result = 3;
        }
        else {
            result = 1;
        }
        return result;
    }
    
    protected abstract long availableInQueue(final long p0, final long p1);
    
    private static long modifiedCalcElementOffset(final long index, final long mask) {
        return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((index & mask) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT - 1);
    }
    
    @Override
    public E poll() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == null) {
            if (index == this.lvProducerIndex()) {
                return null;
            }
            do {
                e = UnsafeRefArrayAccess.lvElement(buffer, offset);
            } while (e == null);
        }
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            final E[] nextBuffer = this.getNextBuffer(buffer, mask);
            return this.newBufferPoll(nextBuffer, index);
        }
        UnsafeRefArrayAccess.soElement(buffer, offset, (E)null);
        this.soConsumerIndex(index + 2L);
        return (E)e;
    }
    
    @Override
    public E peek() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == null && index != this.lvProducerIndex()) {
            while ((e = UnsafeRefArrayAccess.lvElement(buffer, offset)) == null) {}
        }
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            return this.newBufferPeek(this.getNextBuffer(buffer, mask), index);
        }
        return (E)e;
    }
    
    private E[] getNextBuffer(final E[] buffer, final long mask) {
        final long nextArrayOffset = this.nextArrayOffset(mask);
        final E[] nextBuffer = UnsafeRefArrayAccess.lvElement((E[][])(Object)buffer, nextArrayOffset);
        UnsafeRefArrayAccess.soElement(buffer, nextArrayOffset, (E)null);
        return nextBuffer;
    }
    
    private long nextArrayOffset(final long mask) {
        return modifiedCalcElementOffset(mask + 2L, Long.MAX_VALUE);
    }
    
    private E newBufferPoll(final E[] nextBuffer, final long index) {
        final long offsetInNew = this.newBufferAndOffset(nextBuffer, index);
        final E n = UnsafeRefArrayAccess.lvElement(nextBuffer, offsetInNew);
        if (n == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        UnsafeRefArrayAccess.soElement(nextBuffer, offsetInNew, (E)null);
        this.soConsumerIndex(index + 2L);
        return n;
    }
    
    private E newBufferPeek(final E[] nextBuffer, final long index) {
        final long offsetInNew = this.newBufferAndOffset(nextBuffer, index);
        final E n = UnsafeRefArrayAccess.lvElement(nextBuffer, offsetInNew);
        if (null == n) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return n;
    }
    
    private long newBufferAndOffset(final E[] nextBuffer, final long index) {
        this.consumerBuffer = nextBuffer;
        this.consumerMask = nextBuffer.length - 2 << 1;
        final long offsetInNew = modifiedCalcElementOffset(index, this.consumerMask);
        return offsetInNew;
    }
    
    @Override
    public final int size() {
        long after = this.lvConsumerIndex();
        long before;
        long currentProducerIndex;
        do {
            before = after;
            currentProducerIndex = this.lvProducerIndex();
            after = this.lvConsumerIndex();
        } while (before != after);
        final long size = currentProducerIndex - after >> 1;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)size;
    }
    
    @Override
    public final boolean isEmpty() {
        return this.lvConsumerIndex() == this.lvProducerIndex();
    }
    
    private long lvProducerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, BaseMpscLinkedArrayQueue.P_INDEX_OFFSET);
    }
    
    private long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, BaseMpscLinkedArrayQueue.C_INDEX_OFFSET);
    }
    
    private void soProducerIndex(final long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, BaseMpscLinkedArrayQueue.P_INDEX_OFFSET, v);
    }
    
    private boolean casProducerIndex(final long expect, final long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, BaseMpscLinkedArrayQueue.P_INDEX_OFFSET, expect, newValue);
    }
    
    private void soConsumerIndex(final long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, BaseMpscLinkedArrayQueue.C_INDEX_OFFSET, v);
    }
    
    private long lvProducerLimit() {
        return this.producerLimit;
    }
    
    private boolean casProducerLimit(final long expect, final long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, BaseMpscLinkedArrayQueue.P_LIMIT_OFFSET, expect, newValue);
    }
    
    private void soProducerLimit(final long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, BaseMpscLinkedArrayQueue.P_LIMIT_OFFSET, v);
    }
    
    @Override
    public long currentProducerIndex() {
        return this.lvProducerIndex() / 2L;
    }
    
    @Override
    public long currentConsumerIndex() {
        return this.lvConsumerIndex() / 2L;
    }
    
    @Override
    public abstract int capacity();
    
    @Override
    public boolean relaxedOffer(final E e) {
        return this.offer(e);
    }
    
    @Override
    public E relaxedPoll() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = modifiedCalcElementOffset(index, mask);
        final Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == null) {
            return null;
        }
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            final E[] nextBuffer = this.getNextBuffer(buffer, mask);
            return this.newBufferPoll(nextBuffer, index);
        }
        UnsafeRefArrayAccess.soElement(buffer, offset, (E)null);
        this.soConsumerIndex(index + 2L);
        return (E)e;
    }
    
    @Override
    public E relaxedPeek() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = modifiedCalcElementOffset(index, mask);
        final Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            return this.newBufferPeek(this.getNextBuffer(buffer, mask), index);
        }
        return (E)e;
    }
    
    @Override
    public int fill(final Supplier<E> s, final int batchSize) {
        while (true) {
            final long producerLimit = this.lvProducerLimit();
            final long pIndex = this.lvProducerIndex();
            if ((pIndex & 0x1L) == 0x1L) {
                continue;
            }
            final long mask = this.producerMask;
            final E[] buffer = this.producerBuffer;
            final long batchIndex = Math.min(producerLimit, pIndex + 2 * batchSize);
            if (pIndex == producerLimit || producerLimit < batchIndex) {
                final int result = this.offerSlowPath(mask, pIndex, producerLimit);
                switch (result) {
                    case 1: {
                        continue;
                    }
                    case 2: {
                        return 0;
                    }
                    case 3: {
                        this.resize(mask, buffer, pIndex, s.get());
                        return 1;
                    }
                }
            }
            if (this.casProducerIndex(pIndex, batchIndex)) {
                int claimedSlots;
                int i;
                long offset;
                for (claimedSlots = (int)((batchIndex - pIndex) / 2L), i = 0, i = 0; i < claimedSlots; ++i) {
                    offset = modifiedCalcElementOffset(pIndex + 2 * i, mask);
                    UnsafeRefArrayAccess.soElement(buffer, offset, s.get());
                }
                return claimedSlots;
            }
        }
    }
    
    private void resize(final long oldMask, final E[] oldBuffer, final long pIndex, final E e) {
        final int newBufferLength = this.getNextBufferSize(oldBuffer);
        final E[] newBuffer = CircularArrayOffsetCalculator.allocate(newBufferLength);
        this.producerBuffer = newBuffer;
        final int newMask = newBufferLength - 2 << 1;
        this.producerMask = newMask;
        final long offsetInOld = modifiedCalcElementOffset(pIndex, oldMask);
        final long offsetInNew = modifiedCalcElementOffset(pIndex, newMask);
        UnsafeRefArrayAccess.soElement(newBuffer, offsetInNew, e);
        UnsafeRefArrayAccess.soElement(oldBuffer, this.nextArrayOffset(oldMask), newBuffer);
        final long cIndex = this.lvConsumerIndex();
        final long availableInQueue = this.availableInQueue(pIndex, cIndex);
        if (availableInQueue <= 0L) {
            throw new IllegalStateException();
        }
        this.soProducerLimit(pIndex + Math.min(newMask, availableInQueue));
        this.soProducerIndex(pIndex + 2L);
        UnsafeRefArrayAccess.soElement(oldBuffer, offsetInOld, BaseMpscLinkedArrayQueue.JUMP);
    }
    
    protected abstract int getNextBufferSize(final E[] p0);
    
    protected abstract long getCurrentBufferCapacity(final long p0);
    
    @Override
    public int fill(final Supplier<E> s) {
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
    public void fill(final Supplier<E> s, final WaitStrategy w, final ExitCondition exit) {
        while (exit.keepRunning()) {
            while (this.fill(s, MpmcArrayQueue.RECOMENDED_OFFER_BATCH) != 0 && exit.keepRunning()) {}
            int idleCounter = 0;
            while (exit.keepRunning() && this.fill(s, MpmcArrayQueue.RECOMENDED_OFFER_BATCH) == 0) {
                idleCounter = w.idle(idleCounter);
            }
        }
    }
    
    @Override
    public void drain(final Consumer<E> c, final WaitStrategy w, final ExitCondition exit) {
        int idleCounter = 0;
        while (exit.keepRunning()) {
            final E e = this.relaxedPoll();
            if (e == null) {
                idleCounter = w.idle(idleCounter);
            }
            else {
                idleCounter = 0;
                c.accept(e);
            }
        }
    }
    
    @Override
    public int drain(final Consumer<E> c) {
        return this.drain(c, this.capacity());
    }
    
    @Override
    public int drain(final Consumer<E> c, final int limit) {
        int i;
        E m;
        for (i = 0; i < limit && (m = this.relaxedPoll()) != null; ++i) {
            c.accept(m);
        }
        return i;
    }
    
    static {
        try {
            final Field iField = BaseMpscLinkedArrayQueueProducerFields.class.getDeclaredField("producerIndex");
            P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            final Field iField = BaseMpscLinkedArrayQueueConsumerFields.class.getDeclaredField("consumerIndex");
            C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            final Field iField = BaseMpscLinkedArrayQueueColdProducerFields.class.getDeclaredField("producerLimit");
            P_LIMIT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        JUMP = new Object();
    }
}
