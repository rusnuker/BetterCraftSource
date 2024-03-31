// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

public abstract class AbstractLongBigListIterator extends AbstractLongBidirectionalIterator implements LongBigListIterator
{
    protected AbstractLongBigListIterator() {
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
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextLong();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousLong();
        }
        return n - i - 1L;
    }
}
