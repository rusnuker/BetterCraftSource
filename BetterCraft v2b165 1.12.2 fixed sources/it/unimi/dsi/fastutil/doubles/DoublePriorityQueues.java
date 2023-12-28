// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;

public class DoublePriorityQueues
{
    private DoublePriorityQueues() {
    }
    
    public static DoublePriorityQueue synchronize(final DoublePriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static DoublePriorityQueue synchronize(final DoublePriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements DoublePriorityQueue
    {
        protected final DoublePriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final DoublePriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final DoublePriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final double x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public double dequeueDouble() {
            synchronized (this.sync) {
                return this.q.dequeueDouble();
            }
        }
        
        @Override
        public double firstDouble() {
            synchronized (this.sync) {
                return this.q.firstDouble();
            }
        }
        
        @Override
        public double lastDouble() {
            synchronized (this.sync) {
                return this.q.lastDouble();
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
        public DoubleComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Double x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Double dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Double first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Double last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
