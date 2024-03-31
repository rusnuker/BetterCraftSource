// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractShortStack extends AbstractStack<Short> implements ShortStack
{
    protected AbstractShortStack() {
    }
    
    @Override
    public void push(final Short o) {
        this.push((short)o);
    }
    
    @Override
    public Short pop() {
        return this.popShort();
    }
    
    @Override
    public Short top() {
        return this.topShort();
    }
    
    @Override
    public Short peek(final int i) {
        return this.peekShort(i);
    }
    
    @Override
    public void push(final short k) {
        this.push(Short.valueOf(k));
    }
    
    @Override
    public short popShort() {
        return this.pop();
    }
    
    @Override
    public short topShort() {
        return this.top();
    }
    
    @Override
    public short peekShort(final int i) {
        return this.peek(i);
    }
}
