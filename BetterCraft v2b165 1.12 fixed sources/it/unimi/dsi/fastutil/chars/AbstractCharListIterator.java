// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

public abstract class AbstractCharListIterator extends AbstractCharBidirectionalIterator implements CharListIterator
{
    protected AbstractCharListIterator() {
    }
    
    @Override
    public void set(final Character ok) {
        this.set((char)ok);
    }
    
    @Override
    public void add(final Character ok) {
        this.add((char)ok);
    }
    
    @Override
    public void set(final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final char k) {
        throw new UnsupportedOperationException();
    }
}
