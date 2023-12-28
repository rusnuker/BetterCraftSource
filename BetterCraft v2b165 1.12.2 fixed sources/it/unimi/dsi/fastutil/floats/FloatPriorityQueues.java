// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;

public class FloatPriorityQueues
{
    private FloatPriorityQueues() {
    }
    
    public static FloatPriorityQueue synchronize(final FloatPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static FloatPriorityQueue synchronize(final FloatPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements FloatPriorityQueue
    {
        protected final FloatPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final FloatPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final FloatPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final float x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public float dequeueFloat() {
            synchronized (this.sync) {
                return this.q.dequeueFloat();
            }
        }
        
        @Override
        public float firstFloat() {
            synchronized (this.sync) {
                return this.q.firstFloat();
            }
        }
        
        @Override
        public float lastFloat() {
            synchronized (this.sync) {
                return this.q.lastFloat();
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
        public FloatComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Float x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Float dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Float first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Float last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
