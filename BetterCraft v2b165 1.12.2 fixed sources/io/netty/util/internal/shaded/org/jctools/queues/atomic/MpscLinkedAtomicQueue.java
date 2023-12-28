// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues.atomic;

public final class MpscLinkedAtomicQueue<E> extends BaseLinkedAtomicQueue<E>
{
    public MpscLinkedAtomicQueue() {
        final LinkedQueueAtomicNode<E> node = new LinkedQueueAtomicNode<E>();
        this.spConsumerNode(node);
        this.xchgProducerNode(node);
    }
    
    @Override
    public final boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final LinkedQueueAtomicNode<E> nextNode = new LinkedQueueAtomicNode<E>(e);
        final LinkedQueueAtomicNode<E> prevProducerNode = this.xchgProducerNode(nextNode);
        prevProducerNode.soNext(nextNode);
        return true;
    }
    
    @Override
    public final E poll() {
        final LinkedQueueAtomicNode<E> currConsumerNode = this.lpConsumerNode();
        LinkedQueueAtomicNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return this.getSingleConsumerNodeValue(currConsumerNode, nextNode);
        }
        if (currConsumerNode != this.lvProducerNode()) {
            while ((nextNode = currConsumerNode.lvNext()) == null) {}
            return this.getSingleConsumerNodeValue(currConsumerNode, nextNode);
        }
        return null;
    }
    
    @Override
    public final E peek() {
        final LinkedQueueAtomicNode<E> currConsumerNode = this.lpConsumerNode();
        LinkedQueueAtomicNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return nextNode.lpValue();
        }
        if (currConsumerNode != this.lvProducerNode()) {
            while ((nextNode = currConsumerNode.lvNext()) == null) {}
            return nextNode.lpValue();
        }
        return null;
    }
}
