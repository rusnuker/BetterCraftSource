// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

public abstract class AbstractCharIterator implements CharIterator
{
    protected AbstractCharIterator() {
    }
    
    @Override
    public char nextChar() {
        return this.next();
    }
    
    @Deprecated
    @Override
    public Character next() {
        return this.nextChar();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int skip(final int n) {
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextChar();
        }
        return n - i - 1;
    }
}
