// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;

public class CharPriorityQueues
{
    private CharPriorityQueues() {
    }
    
    public static CharPriorityQueue synchronize(final CharPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static CharPriorityQueue synchronize(final CharPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements CharPriorityQueue
    {
        protected final CharPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final CharPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final CharPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        @Override
        public void enqueue(final char x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public char dequeueChar() {
            synchronized (this.sync) {
                return this.q.dequeueChar();
            }
        }
        
        @Override
        public char firstChar() {
            synchronized (this.sync) {
                return this.q.firstChar();
            }
        }
        
        @Override
        public char lastChar() {
            synchronized (this.sync) {
                return this.q.lastChar();
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
        public CharComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Override
        public void enqueue(final Character x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Override
        public Character dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Override
        public Character first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Override
        public Character last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
    }
}
