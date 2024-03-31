// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractIntPriorityQueue extends AbstractPriorityQueue<Integer> implements Serializable, IntPriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Integer x) {
        this.enqueue((int)x);
    }
    
    @Deprecated
    @Override
    public Integer dequeue() {
        return this.dequeueInt();
    }
    
    @Deprecated
    @Override
    public Integer first() {
        return this.firstInt();
    }
    
    @Deprecated
    @Override
    public Integer last() {
        return this.lastInt();
    }
    
    @Override
    public int lastInt() {
        throw new UnsupportedOperationException();
    }
}
