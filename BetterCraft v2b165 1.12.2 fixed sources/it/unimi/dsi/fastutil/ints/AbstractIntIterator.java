// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntIterator implements IntIterator
{
    protected AbstractIntIterator() {
    }
    
    @Override
    public int nextInt() {
        return this.next();
    }
    
    @Deprecated
    @Override
    public Integer next() {
        return this.nextInt();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int skip(final int n) {
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextInt();
        }
        return n - i - 1;
    }
}
