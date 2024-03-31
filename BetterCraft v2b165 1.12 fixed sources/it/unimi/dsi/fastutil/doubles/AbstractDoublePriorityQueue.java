// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractDoublePriorityQueue extends AbstractPriorityQueue<Double> implements Serializable, DoublePriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Double x) {
        this.enqueue((double)x);
    }
    
    @Deprecated
    @Override
    public Double dequeue() {
        return this.dequeueDouble();
    }
    
    @Deprecated
    @Override
    public Double first() {
        return this.firstDouble();
    }
    
    @Deprecated
    @Override
    public Double last() {
        return this.lastDouble();
    }
    
    @Override
    public double lastDouble() {
        throw new UnsupportedOperationException();
    }
}
