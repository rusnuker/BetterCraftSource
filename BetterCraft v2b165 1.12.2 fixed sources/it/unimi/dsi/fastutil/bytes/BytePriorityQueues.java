// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;

public class BytePriorityQueues
{
    private BytePriorityQueues() {
    }
    
    public static BytePriorityQueue synchronize(final BytePriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static BytePriorityQueue synchronize(final BytePriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements BytePriorityQueue
    {
        protected final BytePriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final BytePriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final BytePriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final byte x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public byte dequeueByte() {
            synchronized (this.sync) {
                return this.q.dequeueByte();
            }
        }
        
        @Override
        public byte firstByte() {
            synchronized (this.sync) {
                return this.q.firstByte();
            }
        }
        
        @Override
        public byte lastByte() {
            synchronized (this.sync) {
                return this.q.lastByte();
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
        public ByteComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Byte x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Byte dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Byte first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Byte last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
