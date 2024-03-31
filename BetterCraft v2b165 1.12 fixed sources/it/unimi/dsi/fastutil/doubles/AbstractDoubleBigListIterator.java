// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

public abstract class AbstractDoubleBigListIterator extends AbstractDoubleBidirectionalIterator implements DoubleBigListIterator
{
    protected AbstractDoubleBigListIterator() {
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
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextDouble();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousDouble();
        }
        return n - i - 1L;
    }
}
