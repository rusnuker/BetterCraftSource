// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class IndirectDoublePriorityQueues
{
    public static final EmptyIndirectDoublePriorityQueue EMPTY_QUEUE;
    
    private IndirectDoublePriorityQueues() {
    }
    
    public static <K> IndirectDoublePriorityQueue<K> synchronize(final IndirectDoublePriorityQueue<K> q) {
        return new SynchronizedIndirectDoublePriorityQueue<K>(q);
    }
    
    public static <K> IndirectDoublePriorityQueue<K> synchronize(final IndirectDoublePriorityQueue<K> q, final Object sync) {
        return new SynchronizedIndirectDoublePriorityQueue<K>(q, sync);
    }
    
    static {
        EMPTY_QUEUE = new EmptyIndirectDoublePriorityQueue();
    }
    
    public static class EmptyIndirectDoublePriorityQueue extends IndirectPriorityQueues.EmptyIndirectPriorityQueue
    {
        protected EmptyIndirectDoublePriorityQueue() {
        }
        
        public int secondaryFirst() {
            throw new NoSuchElementException();
        }
        
        public int secondaryLast() {
            throw new NoSuchElementException();
        }
        
        public Comparator<?> secondaryComparator() {
            return null;
        }
    }
    
    public static class SynchronizedIndirectDoublePriorityQueue<K> implements IndirectDoublePriorityQueue<K>
    {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final IndirectDoublePriorityQueue<K> q;
        protected final Object sync;
        
        protected SynchronizedIndirectDoublePriorityQueue(final IndirectDoublePriorityQueue<K> q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedIndirectDoublePriorityQueue(final IndirectDoublePriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final int index) {
            synchronized (this.sync) {
                this.q.enqueue(index);
            }
        }
        
        @Override
        public int dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public int first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public int last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
        
        @Override
        public boolean contains(final int index) {
            synchronized (this.sync) {
                return this.q.contains(index);
            }
        }
        
        @Override
        public int secondaryFirst() {
            synchronized (this.sync) {
                return this.q.secondaryFirst();
            }
        }
        
        @Override
        public int secondaryLast() {
            synchronized (this.sync) {
                return this.q.secondaryLast();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.q.isEmpty();
            }
        }
        
        @Override
        public int size() {
            synchronized (this.sync) {
                return this.q.size();
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.sync) {
                this.q.clear();
            }
        }
        
        @Override
        public void changed() {
            synchronized (this.sync) {
                this.q.changed();
            }
        }
        
        @Override
        public void allChanged() {
            synchronized (this.sync) {
                this.q.allChanged();
            }
        }
        
        @Override
        public void changed(final int i) {
            synchronized (this.sync) {
                this.q.changed(i);
            }
        }
        
        @Override
        public boolean remove(final int i) {
            synchronized (this.sync) {
                return this.q.remove(i);
            }
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public Comparator<? super K> secondaryComparator() {
            synchronized (this.sync) {
                return this.q.secondaryComparator();
            }
        }
        
        @Override
        public int secondaryFront(final int[] a) {
            return this.q.secondaryFront(a);
        }
        
        @Override
        public int front(final int[] a) {
            return this.q.front(a);
        }
    }
}
