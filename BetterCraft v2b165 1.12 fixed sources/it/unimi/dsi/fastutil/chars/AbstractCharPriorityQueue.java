// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public abstract class AbstractCharPriorityQueue extends AbstractPriorityQueue<Character> implements Serializable, CharPriorityQueue
{
    private static final long serialVersionUID = 1L;
    
    @Deprecated
    @Override
    public void enqueue(final Character x) {
        this.enqueue((char)x);
    }
    
    @Deprecated
    @Override
    public Character dequeue() {
        return this.dequeueChar();
    }
    
    @Deprecated
    @Override
    public Character first() {
        return this.firstChar();
    }
    
    @Deprecated
    @Override
    public Character last() {
        return this.lastChar();
    }
    
    @Override
    public char lastChar() {
        throw new UnsupportedOperationException();
    }
}
