// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

public abstract class AbstractFloatBidirectionalIterator extends AbstractFloatIterator implements FloatBidirectionalIterator
{
    protected AbstractFloatBidirectionalIterator() {
    }
    
    @Override
    public float previousFloat() {
        return this.previous();
    }
    
    @Override
    public Float previous() {
        return this.previousFloat();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousFloat();
        }
        return n - i - 1;
    }
}
