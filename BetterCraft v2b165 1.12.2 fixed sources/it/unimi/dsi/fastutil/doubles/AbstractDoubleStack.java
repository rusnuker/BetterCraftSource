// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractDoubleStack extends AbstractStack<Double> implements DoubleStack
{
    protected AbstractDoubleStack() {
    }
    
    @Override
    public void push(final Double o) {
        this.push((double)o);
    }
    
    @Override
    public Double pop() {
        return this.popDouble();
    }
    
    @Override
    public Double top() {
        return this.topDouble();
    }
    
    @Override
    public Double peek(final int i) {
        return this.peekDouble(i);
    }
    
    @Override
    public void push(final double k) {
        this.push(Double.valueOf(k));
    }
    
    @Override
    public double popDouble() {
        return this.pop();
    }
    
    @Override
    public double topDouble() {
        return this.top();
    }
    
    @Override
    public double peekDouble(final int i) {
        return this.peek(i);
    }
}
