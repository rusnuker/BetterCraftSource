// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntBidirectionalIterator extends AbstractIntIterator implements IntBidirectionalIterator
{
    protected AbstractIntBidirectionalIterator() {
    }
    
    @Override
    public int previousInt() {
        return this.previous();
    }
    
    @Override
    public Integer previous() {
        return this.previousInt();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousInt();
        }
        return n - i - 1;
    }
}
