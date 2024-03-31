// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

public abstract class AbstractDoubleListIterator extends AbstractDoubleBidirectionalIterator implements DoubleListIterator
{
    protected AbstractDoubleListIterator() {
    }
    
    @Override
    public void set(final Double ok) {
        this.set((double)ok);
    }
    
    @Override
    public void add(final Double ok) {
        this.add((double)ok);
    }
    
    @Override
    public void set(final double k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final double k) {
        throw new UnsupportedOperationException();
    }
}
