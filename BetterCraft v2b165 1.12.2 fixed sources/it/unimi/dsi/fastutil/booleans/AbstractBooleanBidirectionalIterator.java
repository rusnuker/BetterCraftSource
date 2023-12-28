// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

public abstract class AbstractBooleanBidirectionalIterator extends AbstractBooleanIterator implements BooleanBidirectionalIterator
{
    protected AbstractBooleanBidirectionalIterator() {
    }
    
    @Override
    public boolean previousBoolean() {
        return this.previous();
    }
    
    @Override
    public Boolean previous() {
        return this.previousBoolean();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousBoolean();
        }
        return n - i - 1;
    }
}
