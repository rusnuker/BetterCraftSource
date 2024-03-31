// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

import java.util.Iterator;

abstract class BaseLinkedQueue<E> extends BaseLinkedQueueConsumerNodeRef<E>
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
        LinkedQueueNode<E> chaserNode;
        LinkedQueueNode<E> producerNode;
        int size;
        LinkedQueueNode<E> next;
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
    
    @Override
    public int capacity() {
        return -1;
    }
    
    protected E getSingleConsumerNodeValue(final LinkedQueueNode<E> currConsumerNode, final LinkedQueueNode<E> nextNode) {
        final E nextValue = nextNode.getAndNullValue();
        currConsumerNode.soNext(currConsumerNode);
        this.spConsumerNode(nextNode);
        return nextValue;
    }
    
    @Override
    public E relaxedPoll() {
        final LinkedQueueNode<E> currConsumerNode = this.lpConsumerNode();
        final LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return this.getSingleConsumerNodeValue(currConsumerNode, nextNode);
        }
        return null;
    }
    
    @Override
    public int drain(final MessagePassingQueue.Consumer<E> c) {
        long result = 0L;
        int drained;
        do {
            drained = this.drain(c, 4096);
            result += drained;
        } while (drained == 4096 && result <= 2147479551L);
        return (int)result;
    }
    
    @Override
    public int drain(final MessagePassingQueue.Consumer<E> c, final int limit) {
        LinkedQueueNode<E> chaserNode = this.consumerNode;
        for (int i = 0; i < limit; ++i) {
            final LinkedQueueNode<E> nextNode = chaserNode.lvNext();
            if (nextNode == null) {
                return i;
            }
            final E nextValue = this.getSingleConsumerNodeValue(chaserNode, nextNode);
            chaserNode = nextNode;
            c.accept(nextValue);
        }
        return limit;
    }
    
    @Override
    public void drain(final MessagePassingQueue.Consumer<E> c, final MessagePassingQueue.WaitStrategy wait, final MessagePassingQueue.ExitCondition exit) {
        LinkedQueueNode<E> chaserNode = this.consumerNode;
        int idleCounter = 0;
        while (exit.keepRunning()) {
            for (int i = 0; i < 4096; ++i) {
                final LinkedQueueNode<E> nextNode = chaserNode.lvNext();
                if (nextNode == null) {
                    idleCounter = wait.idle(idleCounter);
                }
                else {
                    idleCounter = 0;
                    final E nextValue = this.getSingleConsumerNodeValue(chaserNode, nextNode);
                    chaserNode = nextNode;
                    c.accept(nextValue);
                }
            }
        }
    }
    
    @Override
    public E relaxedPeek() {
        final LinkedQueueNode<E> currConsumerNode = this.consumerNode;
        final LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return nextNode.lpValue();
        }
        return null;
    }
    
    @Override
    public boolean relaxedOffer(final E e) {
        return this.offer(e);
    }
}
