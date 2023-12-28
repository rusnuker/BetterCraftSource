// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractByteStack extends AbstractStack<Byte> implements ByteStack
{
    protected AbstractByteStack() {
    }
    
    @Override
    public void push(final Byte o) {
        this.push((byte)o);
    }
    
    @Override
    public Byte pop() {
        return this.popByte();
    }
    
    @Override
    public Byte top() {
        return this.topByte();
    }
    
    @Override
    public Byte peek(final int i) {
        return this.peekByte(i);
    }
    
    @Override
    public void push(final byte k) {
        this.push(Byte.valueOf(k));
    }
    
    @Override
    public byte popByte() {
        return this.pop();
    }
    
    @Override
    public byte topByte() {
        return this.top();
    }
    
    @Override
    public byte peekByte(final int i) {
        return this.peek(i);
    }
}
