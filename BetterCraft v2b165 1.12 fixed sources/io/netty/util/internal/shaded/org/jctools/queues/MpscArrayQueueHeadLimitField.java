// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class MpscArrayQueueHeadLimitField<E> extends MpscArrayQueueMidPad<E>
{
    private static final long P_LIMIT_OFFSET;
    private volatile long producerLimit;
    
    public MpscArrayQueueHeadLimitField(final int capacity) {
        super(capacity);
        this.producerLimit = capacity;
    }
    
    protected final long lvProducerLimit() {
        return this.producerLimit;
    }
    
    protected final void soProducerLimit(final long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, MpscArrayQueueHeadLimitField.P_LIMIT_OFFSET, v);
    }
    
    static {
        try {
            P_LIMIT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueHeadLimitField.class.getDeclaredField("producerLimit"));
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
