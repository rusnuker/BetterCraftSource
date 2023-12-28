// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractBooleanStack extends AbstractStack<Boolean> implements BooleanStack
{
    protected AbstractBooleanStack() {
    }
    
    @Override
    public void push(final Boolean o) {
        this.push((boolean)o);
    }
    
    @Override
    public Boolean pop() {
        return this.popBoolean();
    }
    
    @Override
    public Boolean top() {
        return this.topBoolean();
    }
    
    @Override
    public Boolean peek(final int i) {
        return this.peekBoolean(i);
    }
    
    @Override
    public void push(final boolean k) {
        this.push(Boolean.valueOf(k));
    }
    
    @Override
    public boolean popBoolean() {
        return this.pop();
    }
    
    @Override
    public boolean topBoolean() {
        return this.top();
    }
    
    @Override
    public boolean peekBoolean(final int i) {
        return this.peek(i);
    }
}
