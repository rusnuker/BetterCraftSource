// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

public abstract class AbstractCharBidirectionalIterator extends AbstractCharIterator implements CharBidirectionalIterator
{
    protected AbstractCharBidirectionalIterator() {
    }
    
    @Override
    public char previousChar() {
        return this.previous();
    }
    
    @Override
    public Character previous() {
        return this.previousChar();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousChar();
        }
        return n - i - 1;
    }
}
