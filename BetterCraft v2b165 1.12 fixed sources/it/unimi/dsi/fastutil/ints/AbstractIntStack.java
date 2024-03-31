// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractIntStack extends AbstractStack<Integer> implements IntStack
{
    protected AbstractIntStack() {
    }
    
    @Override
    public void push(final Integer o) {
        this.push((int)o);
    }
    
    @Override
    public Integer pop() {
        return this.popInt();
    }
    
    @Override
    public Integer top() {
        return this.topInt();
    }
    
    @Override
    public Integer peek(final int i) {
        return this.peekInt(i);
    }
    
    @Override
    public void push(final int k) {
        this.push(Integer.valueOf(k));
    }
    
    @Override
    public int popInt() {
        return this.pop();
    }
    
    @Override
    public int topInt() {
        return this.top();
    }
    
    @Override
    public int peekInt(final int i) {
        return this.peek(i);
    }
}
