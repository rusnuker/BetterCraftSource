// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

public abstract class AbstractShortBigListIterator extends AbstractShortBidirectionalIterator implements ShortBigListIterator
{
    protected AbstractShortBigListIterator() {
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
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextShort();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousShort();
        }
        return n - i - 1L;
    }
}
