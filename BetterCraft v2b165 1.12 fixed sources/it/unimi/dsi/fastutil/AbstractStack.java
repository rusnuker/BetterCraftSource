// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

public abstract class AbstractStack<K> implements Stack<K>
{
    @Override
    public K top() {
        return this.peek(0);
    }
    
    @Override
    public K peek(final int i) {
        throw new UnsupportedOperationException();
    }
}
