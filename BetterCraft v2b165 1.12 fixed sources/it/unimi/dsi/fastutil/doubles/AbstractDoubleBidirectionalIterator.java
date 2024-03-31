// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

public abstract class AbstractDoubleBidirectionalIterator extends AbstractDoubleIterator implements DoubleBidirectionalIterator
{
    protected AbstractDoubleBidirectionalIterator() {
    }
    
    @Override
    public double previousDouble() {
        return this.previous();
    }
    
    @Override
    public Double previous() {
        return this.previousDouble();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousDouble();
        }
        return n - i - 1;
    }
}
