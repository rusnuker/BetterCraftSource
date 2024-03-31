// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class MpmcArrayQueueConsumerField<E> extends MpmcArrayQueueL2Pad<E>
{
    private static final long C_INDEX_OFFSET;
    private volatile long consumerIndex;
    
    public MpmcArrayQueueConsumerField(final int capacity) {
        super(capacity);
    }
    
    @Override
    public final long lvConsumerIndex() {
        return this.consumerIndex;
    }
    
    protected final boolean casConsumerIndex(final long expect, final long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, MpmcArrayQueueConsumerField.C_INDEX_OFFSET, expect, newValue);
    }
    
    static {
        try {
            C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpmcArrayQueueConsumerField.class.getDeclaredField("consumerIndex"));
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
