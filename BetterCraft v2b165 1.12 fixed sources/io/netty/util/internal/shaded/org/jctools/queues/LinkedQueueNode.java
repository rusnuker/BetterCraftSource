// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

final class LinkedQueueNode<E>
{
    private static final long NEXT_OFFSET;
    private E value;
    private volatile LinkedQueueNode<E> next;
    
    LinkedQueueNode() {
        this(null);
    }
    
    LinkedQueueNode(final E val) {
        this.spValue(val);
    }
    
    public E getAndNullValue() {
        final E temp = this.lpValue();
        this.spValue(null);
        return temp;
    }
    
    public E lpValue() {
        return this.value;
    }
    
    public void spValue(final E newValue) {
        this.value = newValue;
    }
    
    public void soNext(final LinkedQueueNode<E> n) {
        UnsafeAccess.UNSAFE.putOrderedObject(this, LinkedQueueNode.NEXT_OFFSET, n);
    }
    
    public LinkedQueueNode<E> lvNext() {
        return this.next;
    }
    
    static {
        try {
            NEXT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(LinkedQueueNode.class.getDeclaredField("next"));
        }
        catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
