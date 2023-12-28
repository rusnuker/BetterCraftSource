// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractLongPriorityQueue extends AbstractPriorityQueue<Long> implements Serializable, LongPriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Long x) {
        this.enqueue((long)x);
    }
    
    @Deprecated
    @Override
    public Long dequeue() {
        return this.dequeueLong();
    }
    
    @Deprecated
    @Override
    public Long first() {
        return this.firstLong();
    }
    
    @Deprecated
    @Override
    public Long last() {
        return this.lastLong();
    }
    
    @Override
    public long lastLong() {
        throw new UnsupportedOperationException();
    }
}
