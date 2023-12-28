// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal.shaded.org.jctools.queues;

public class SpscLinkedQueue<E> extends BaseLinkedQueue<E>
{
    public SpscLinkedQueue() {
        this.spProducerNode(new LinkedQueueNode<E>());
        this.spConsumerNode(this.producerNode);
        this.consumerNode.soNext(null);
    }
    
    @Override
    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final LinkedQueueNode<E> nextNode = new LinkedQueueNode<E>(e);
        final LinkedQueueNode<E> producerNode = this.lpProducerNode();
        producerNode.soNext(nextNode);
        this.spProducerNode(nextNode);
        return true;
    }
    
    @Override
    public E poll() {
        return (E)this.relaxedPoll();
    }
    
    @Override
    public E peek() {
        return (E)this.relaxedPeek();
    }
    
    @Override
    public int fill(final MessagePassingQueue.Supplier<E> s) {
        long result = 0L;
        do {
            this.fill(s, 4096);
            result += 4096L;
        } while (result <= 2147479551L);
        return (int)result;
    }
    
    @Override
    public int fill(final MessagePassingQueue.Supplier<E> s, final int limit) {
        if (limit == 0) {
            return 0;
        }
        final LinkedQueueNode<E> head;
        LinkedQueueNode<E> tail = head = new LinkedQueueNode<E>(s.get());
        for (int i = 1; i < limit; ++i) {
            final LinkedQueueNode<E> temp = new LinkedQueueNode<E>(s.get());
            tail.soNext(temp);
            tail = temp;
        }
        final LinkedQueueNode<E> oldPNode = this.lpProducerNode();
        oldPNode.soNext(head);
        this.spProducerNode(tail);
        return limit;
    }
    
    @Override
    public void fill(final MessagePassingQueue.Supplier<E> s, final MessagePassingQueue.WaitStrategy wait, final MessagePassingQueue.ExitCondition exit) {
        LinkedQueueNode<E> chaserNode = this.producerNode;
        while (exit.keepRunning()) {
            for (int i = 0; i < 4096; ++i) {
                final LinkedQueueNode<E> nextNode = new LinkedQueueNode<E>(s.get());
                chaserNode.soNext(nextNode);
                chaserNode = nextNode;
                this.producerNode = chaserNode;
            }
        }
    }
}
