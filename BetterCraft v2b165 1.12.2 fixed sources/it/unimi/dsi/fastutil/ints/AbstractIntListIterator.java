// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntListIterator extends AbstractIntBidirectionalIterator implements IntListIterator
{
    protected AbstractIntListIterator() {
    }
    
    @Override
    public void set(final Integer ok) {
        this.set((int)ok);
    }
    
    @Override
    public void add(final Integer ok) {
        this.add((int)ok);
    }
    
    @Override
    public void set(final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final int k) {
        throw new UnsupportedOperationException();
    }
}
