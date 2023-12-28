// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractFloatPriorityQueue extends AbstractPriorityQueue<Float> implements Serializable, FloatPriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Float x) {
        this.enqueue((float)x);
    }
    
    @Deprecated
    @Override
    public Float dequeue() {
        return this.dequeueFloat();
    }
    
    @Deprecated
    @Override
    public Float first() {
        return this.firstFloat();
    }
    
    @Deprecated
    @Override
    public Float last() {
        return this.lastFloat();
    }
    
    @Override
    public float lastFloat() {
        throw new UnsupportedOperationException();
    }
}
