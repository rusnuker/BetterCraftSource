// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractShortPriorityQueue extends AbstractPriorityQueue<Short> implements Serializable, ShortPriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Short x) {
        this.enqueue((short)x);
    }
    
    @Deprecated
    @Override
    public Short dequeue() {
        return this.dequeueShort();
    }
    
    @Deprecated
    @Override
    public Short first() {
        return this.firstShort();
    }
    
    @Deprecated
    @Override
    public Short last() {
        return this.lastShort();
    }
    
    @Override
    public short lastShort() {
        throw new UnsupportedOperationException();
    }
}
