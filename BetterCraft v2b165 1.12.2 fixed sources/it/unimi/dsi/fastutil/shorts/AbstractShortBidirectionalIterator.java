// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

public abstract class AbstractShortBidirectionalIterator extends AbstractShortIterator implements ShortBidirectionalIterator
{
    protected AbstractShortBidirectionalIterator() {
    }
    
    @Override
    public short previousShort() {
        return this.previous();
    }
    
    @Override
    public Short previous() {
        return this.previousShort();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousShort();
        }
        return n - i - 1;
    }
}
