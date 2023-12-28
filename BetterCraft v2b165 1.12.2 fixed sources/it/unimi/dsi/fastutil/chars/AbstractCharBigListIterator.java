// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

public abstract class AbstractCharBigListIterator extends AbstractCharBidirectionalIterator implements CharBigListIterator
{
    protected AbstractCharBigListIterator() {
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
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextChar();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousChar();
        }
        return n - i - 1L;
    }
}
