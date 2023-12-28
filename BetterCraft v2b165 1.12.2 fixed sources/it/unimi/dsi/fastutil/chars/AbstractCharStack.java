// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.AbstractStack;

public abstract class AbstractCharStack extends AbstractStack<Character> implements CharStack
{
    protected AbstractCharStack() {
    }
    
    @Override
    public void push(final Character o) {
        this.push((char)o);
    }
    
    @Override
    public Character pop() {
        return this.popChar();
    }
    
    @Override
    public Character top() {
        return this.topChar();
    }
    
    @Override
    public Character peek(final int i) {
        return this.peekChar(i);
    }
    
    @Override
    public void push(final char k) {
        this.push(Character.valueOf(k));
    }
    
    @Override
    public char popChar() {
        return this.pop();
    }
    
    @Override
    public char topChar() {
        return this.top();
    }
    
    @Override
    public char peekChar(final int i) {
        return this.peek(i);
    }
}
