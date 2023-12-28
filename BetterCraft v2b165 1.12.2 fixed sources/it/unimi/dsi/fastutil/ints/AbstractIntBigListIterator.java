// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntBigListIterator extends AbstractIntBidirectionalIterator implements IntBigListIterator
{
    protected AbstractIntBigListIterator() {
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
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextInt();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousInt();
        }
        return n - i - 1L;
    }
}
