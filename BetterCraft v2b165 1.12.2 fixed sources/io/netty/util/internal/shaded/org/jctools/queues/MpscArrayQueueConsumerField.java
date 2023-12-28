// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class MpscArrayQueueConsumerField<E> extends MpscArrayQueueL2Pad<E>
{
    private static final long C_INDEX_OFFSET;
    protected long consumerIndex;
    
    public MpscArrayQueueConsumerField(final int capacity) {
        super(capacity);
    }
    
    protected final long lpConsumerIndex() {
        return this.consumerIndex;
    }
    
    @Override
    public final long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, MpscArrayQueueConsumerField.C_INDEX_OFFSET);
    }
    
    protected void soConsumerIndex(final long l) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, MpscArrayQueueConsumerField.C_INDEX_OFFSET, l);
    }
    
    static {
        try {
            C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueConsumerField.class.getDeclaredField("consumerIndex"));
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
