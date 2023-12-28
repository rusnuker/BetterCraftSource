// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

public abstract class AbstractShortListIterator extends AbstractShortBidirectionalIterator implements ShortListIterator
{
    protected AbstractShortListIterator() {
    }
    
    @Override
    public void set(final Short ok) {
        this.set((short)ok);
    }
    
    @Override
    public void add(final Short ok) {
        this.add((short)ok);
    }
    
    @Override
    public void set(final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final short k) {
        throw new UnsupportedOperationException();
    }
}
