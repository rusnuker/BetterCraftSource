// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.AbstractQueue;

public final class DefaultPriorityQueue<T extends PriorityQueueNode> extends AbstractQueue<T> implements PriorityQueue<T>
{
    private static final PriorityQueueNode[] EMPTY_ARRAY;
    private final Comparator<T> comparator;
    private T[] queue;
    private int size;
    
    public DefaultPriorityQueue(final Comparator<T> comparator, final int initialSize) {
        this.comparator = ObjectUtil.checkNotNull(comparator, "comparator");
        this.queue = (T[])((initialSize != 0) ? new PriorityQueueNode[initialSize] : DefaultPriorityQueue.EMPTY_ARRAY);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public boolean contains(final Object o) {
        if (!(o instanceof PriorityQueueNode)) {
            return false;
        }
        final PriorityQueueNode node = (PriorityQueueNode)o;
        return this.contains(node, node.priorityQueueIndex(this));
    }
    
    @Override
    public boolean containsTyped(final T node) {
        return this.contains(node, node.priorityQueueIndex(this));
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.size; ++i) {
            final T node = this.queue[i];
            if (node != null) {
                node.priorityQueueIndex(this, -1);
                this.queue[i] = null;
            }
        }
        this.size = 0;
    }
    
    @Override
    public boolean offer(final T e) {
        if (e.priorityQueueIndex(this) != -1) {
            throw new IllegalArgumentException("e.priorityQueueIndex(): " + e.priorityQueueIndex(this) + " (expected: " + -1 + ") + e: " + e);
        }
        if (this.size >= this.queue.length) {
            this.queue = Arrays.copyOf(this.queue, this.queue.length + ((this.queue.length < 64) ? (this.queue.length + 2) : (this.queue.length >>> 1)));
        }
        this.bubbleUp(this.size++, e);
        return true;
    }
    
    @Override
    public T poll() {
        if (this.size == 0) {
            return null;
        }
        final T result = this.queue[0];
        result.priorityQueueIndex(this, -1);
        final T[] queue = this.queue;
        final int size = this.size - 1;
        this.size = size;
        final T last = queue[size];
        this.queue[this.size] = null;
        if (this.size != 0) {
            this.bubbleDown(0, last);
        }
        return result;
    }
    
    @Override
    public T peek() {
        return (T)((this.size == 0) ? null : this.queue[0]);
    }
    
    @Override
    public boolean remove(final Object o) {
        T node;
        try {
            node = (T)o;
        }
        catch (final ClassCastException e) {
            return false;
        }
        return this.removeTyped(node);
    }
    
    @Override
    public boolean removeTyped(final T node) {
        final int i = node.priorityQueueIndex(this);
        if (!this.contains(node, i)) {
            return false;
        }
        node.priorityQueueIndex(this, -1);
        final int size = this.size - 1;
        this.size = size;
        if (size == 0 || this.size == i) {
            this.queue[i] = null;
            return true;
        }
        final T[] queue = this.queue;
        final int n = i;
        final PriorityQueueNode priorityQueueNode = this.queue[this.size];
        queue[n] = (T)priorityQueueNode;
        final T moved = (T)priorityQueueNode;
        this.queue[this.size] = null;
        if (this.comparator.compare(node, moved) < 0) {
            this.bubbleDown(i, moved);
        }
        else {
            this.bubbleUp(i, moved);
        }
        return true;
    }
    
    @Override
    public void priorityChanged(final T node) {
        final int i = node.priorityQueueIndex(this);
        if (!this.contains(node, i)) {
            return;
        }
        if (i == 0) {
            this.bubbleDown(i, node);
        }
        else {
            final int iParent = i - 1 >>> 1;
            final T parent = this.queue[iParent];
            if (this.comparator.compare(node, parent) < 0) {
                this.bubbleUp(i, node);
            }
            else {
                this.bubbleDown(i, node);
            }
        }
    }
    
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.queue, this.size);
    }
    
    @Override
    public <X> X[] toArray(final X[] a) {
        if (a.length < this.size) {
            return Arrays.copyOf(this.queue, this.size, (Class<? extends X[]>)a.getClass());
        }
        System.arraycopy(this.queue, 0, a, 0, this.size);
        if (a.length > this.size) {
            a[this.size] = null;
        }
        return a;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new PriorityQueueIterator();
    }
    
    private boolean contains(final PriorityQueueNode node, final int i) {
        return i >= 0 && i < this.size && node.equals(this.queue[i]);
    }
    
    private void bubbleDown(int k, final T node) {
        int iChild;
        for (int half = this.size >>> 1; k < half; k = iChild) {
            iChild = (k << 1) + 1;
            T child = this.queue[iChild];
            final int rightChild = iChild + 1;
            if (rightChild < this.size && this.comparator.compare(child, this.queue[rightChild]) > 0) {
                child = this.queue[iChild = rightChild];
            }
            if (this.comparator.compare(node, child) <= 0) {
                break;
            }
            (this.queue[k] = child).priorityQueueIndex(this, k);
        }
        (this.queue[k] = node).priorityQueueIndex(this, k);
    }
    
    private void bubbleUp(int k, final T node) {
        while (k > 0) {
            final int iParent = k - 1 >>> 1;
            final T parent = this.queue[iParent];
            if (this.comparator.compare(node, parent) >= 0) {
                break;
            }
            (this.queue[k] = parent).priorityQueueIndex(this, k);
            k = iParent;
        }
        (this.queue[k] = node).priorityQueueIndex(this, k);
    }
    
    static {
        EMPTY_ARRAY = new PriorityQueueNode[0];
    }
    
    private final class PriorityQueueIterator implements Iterator<T>
    {
        private int index;
        
        @Override
        public boolean hasNext() {
            return this.index < DefaultPriorityQueue.this.size;
        }
        
        @Override
        public T next() {
            if (this.index >= DefaultPriorityQueue.this.size) {
                throw new NoSuchElementException();
            }
            return DefaultPriorityQueue.this.queue[this.index++];
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
