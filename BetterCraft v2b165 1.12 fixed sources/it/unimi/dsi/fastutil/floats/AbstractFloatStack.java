// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractFloatStack extends AbstractStack<Float> implements FloatStack
{
    protected AbstractFloatStack() {
    }
    
    @Override
    public void push(final Float o) {
        this.push((float)o);
    }
    
    @Override
    public Float pop() {
        return this.popFloat();
    }
    
    @Override
    public Float top() {
        return this.topFloat();
    }
    
    @Override
    public Float peek(final int i) {
        return this.peekFloat(i);
    }
    
    @Override
    public void push(final float k) {
        this.push(Float.valueOf(k));
    }
    
    @Override
    public float popFloat() {
        return this.pop();
    }
    
    @Override
    public float topFloat() {
        return this.top();
    }
    
    @Override
    public float peekFloat(final int i) {
        return this.peek(i);
    }
}
