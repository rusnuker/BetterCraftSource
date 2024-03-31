// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

public abstract class AbstractLongIterator implements LongIterator
{
    protected AbstractLongIterator() {
    }
    
    @Override
    public long nextLong() {
        return this.next();
    }
    
    @Deprecated
    @Override
    public Long next() {
        return this.nextLong();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int skip(final int n) {
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextLong();
        }
        return n - i - 1;
    }
}
