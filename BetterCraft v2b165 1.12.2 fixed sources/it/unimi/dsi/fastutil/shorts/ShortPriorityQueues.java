// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;

public class ShortPriorityQueues
{
    private ShortPriorityQueues() {
    }
    
    public static ShortPriorityQueue synchronize(final ShortPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static ShortPriorityQueue synchronize(final ShortPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements ShortPriorityQueue
    {
        protected final ShortPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final ShortPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final ShortPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final short x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public short dequeueShort() {
            synchronized (this.sync) {
                return this.q.dequeueShort();
            }
        }
        
        @Override
        public short firstShort() {
            synchronized (this.sync) {
                return this.q.firstShort();
            }
        }
        
        @Override
        public short lastShort() {
            synchronized (this.sync) {
                return this.q.lastShort();
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
        public ShortComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Short x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Short dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Short first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Short last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
