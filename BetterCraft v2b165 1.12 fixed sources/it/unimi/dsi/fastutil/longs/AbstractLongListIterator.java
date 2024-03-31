// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

public abstract class AbstractLongListIterator extends AbstractLongBidirectionalIterator implements LongListIterator
{
    protected AbstractLongListIterator() {
    }
    
    @Override
    public void set(final Long ok) {
        this.set((long)ok);
    }
    
    @Override
    public void add(final Long ok) {
        this.add((long)ok);
    }
    
    @Override
    public void set(final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final long k) {
        throw new UnsupportedOperationException();
    }
}
