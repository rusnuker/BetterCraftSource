// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractBytePriorityQueue extends AbstractPriorityQueue<Byte> implements Serializable, BytePriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Byte x) {
        this.enqueue((byte)x);
    }
    
    @Deprecated
    @Override
    public Byte dequeue() {
        return this.dequeueByte();
    }
    
    @Deprecated
    @Override
    public Byte first() {
        return this.firstByte();
    }
    
    @Deprecated
    @Override
    public Byte last() {
        return this.lastByte();
    }
    
    @Override
    public byte lastByte() {
        throw new UnsupportedOperationException();
    }
}
