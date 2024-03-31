// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.AbstractQueue;

abstract class BaseLinkedAtomicQueue<E> extends AbstractQueue<E>
{
    private final AtomicReference<LinkedQueueAtomicNode<E>> producerNode;
    private final AtomicReference<LinkedQueueAtomicNode<E>> consumerNode;
    
    public BaseLinkedAtomicQueue() {
        this.producerNode = new AtomicReference<LinkedQueueAtomicNode<E>>();
        this.consumerNode = new AtomicReference<LinkedQueueAtomicNode<E>>();
    }
    
    protected final LinkedQueueAtomicNode<E> lvProducerNode() {
        return this.producerNode.get();
    }
    
    protected final LinkedQueueAtomicNode<E> lpProducerNode() {
        return this.producerNode.get();
    }
    
    protected final void spProducerNode(final LinkedQueueAtomicNode<E> node) {
        this.producerNode.lazySet(node);
    }
    
    protected final LinkedQueueAtomicNode<E> xchgProducerNode(final LinkedQueueAtomicNode<E> node) {
        return this.producerNode.getAndSet(node);
    }
    
    protected final LinkedQueueAtomicNode<E> lvConsumerNode() {
        return this.consumerNode.get();
    }
    
    protected final LinkedQueueAtomicNode<E> lpConsumerNode() {
        return this.consumerNode.get();
    }
    
    protected final void spConsumerNode(final LinkedQueueAtomicNode<E> node) {
        this.consumerNode.lazySet(node);
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
    public final int size() {
        LinkedQueueAtomicNode<E> chaserNode;
        LinkedQueueAtomicNode<E> producerNode;
        int size;
        LinkedQueueAtomicNode<E> next;
        for (chaserNode = this.lvConsumerNode(), producerNode = this.lvProducerNode(), size = 0; chaserNode != producerNode && chaserNode != null && size < Integer.MAX_VALUE; chaserNode = next, ++size) {
            next = chaserNode.lvNext();
            if (next == chaserNode) {
                return size;
            }
        }
        return size;
    }
    
    @Override
    public final boolean isEmpty() {
        return this.lvConsumerNode() == this.lvProducerNode();
    }
    
    protected E getSingleConsumerNodeValue(final LinkedQueueAtomicNode<E> currConsumerNode, final LinkedQueueAtomicNode<E> nextNode) {
        final E nextValue = nextNode.getAndNullValue();
        currConsumerNode.soNext(currConsumerNode);
        this.spConsumerNode(nextNode);
        return nextValue;
    }
}
