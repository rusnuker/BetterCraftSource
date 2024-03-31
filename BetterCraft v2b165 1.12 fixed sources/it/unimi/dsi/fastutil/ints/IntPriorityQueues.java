// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;

public class IntPriorityQueues
{
    private IntPriorityQueues() {
    }
    
    public static IntPriorityQueue synchronize(final IntPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static IntPriorityQueue synchronize(final IntPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements IntPriorityQueue
    {
        protected final IntPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final IntPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final IntPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final int x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public int dequeueInt() {
            synchronized (this.sync) {
                return this.q.dequeueInt();
            }
        }
        
        @Override
        public int firstInt() {
            synchronized (this.sync) {
                return this.q.firstInt();
            }
        }
        
        @Override
        public int lastInt() {
            synchronized (this.sync) {
                return this.q.lastInt();
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
        public IntComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Integer x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Integer dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Integer first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Integer last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
