// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

public abstract class AbstractBooleanBigListIterator extends AbstractBooleanBidirectionalIterator implements BooleanBigListIterator
{
    protected AbstractBooleanBigListIterator() {
    }
    
    @Override
    public void set(final Boolean ok) {
        this.set((boolean)ok);
    }
    
    @Override
    public void add(final Boolean ok) {
        this.add((boolean)ok);
    }
    
    @Override
    public void set(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextBoolean();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousBoolean();
        }
        return n - i - 1L;
    }
}
