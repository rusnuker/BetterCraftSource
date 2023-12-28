// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;

public class LongPriorityQueues
{
    private LongPriorityQueues() {
    }
    
    public static LongPriorityQueue synchronize(final LongPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static LongPriorityQueue synchronize(final LongPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements LongPriorityQueue
    {
        protected final LongPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final LongPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final LongPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final long x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public long dequeueLong() {
            synchronized (this.sync) {
                return this.q.dequeueLong();
            }
        }
        
        @Override
        public long firstLong() {
            synchronized (this.sync) {
                return this.q.firstLong();
            }
        }
        
        @Override
        public long lastLong() {
            synchronized (this.sync) {
                return this.q.lastLong();
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
        public LongComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Long x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Long dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Long first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Long last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
