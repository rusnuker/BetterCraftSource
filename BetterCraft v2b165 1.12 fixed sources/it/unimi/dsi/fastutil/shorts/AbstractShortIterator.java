// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

public abstract class AbstractShortIterator implements ShortIterator
{
    protected AbstractShortIterator() {
    }
    
    @Override
    public short nextShort() {
        return this.next();
    }
    
    @Deprecated
    @Override
    public Short next() {
        return this.nextShort();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int skip(final int n) {
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextShort();
        }
        return n - i - 1;
    }
}
