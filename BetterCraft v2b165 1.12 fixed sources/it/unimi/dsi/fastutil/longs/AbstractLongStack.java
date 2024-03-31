// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractLongStack extends AbstractStack<Long> implements LongStack
{
    protected AbstractLongStack() {
    }
    
    @Override
    public void push(final Long o) {
        this.push((long)o);
    }
    
    @Override
    public Long pop() {
        return this.popLong();
    }
    
    @Override
    public Long top() {
        return this.topLong();
    }
    
    @Override
    public Long peek(final int i) {
        return this.peekLong(i);
    }
    
    @Override
    public void push(final long k) {
        this.push(Long.valueOf(k));
    }
    
    @Override
    public long popLong() {
        return this.pop();
    }
    
    @Override
    public long topLong() {
        return this.top();
    }
    
    @Override
    public long peekLong(final int i) {
        return this.peek(i);
    }
}
