// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

public abstract class AbstractFloatBigListIterator extends AbstractFloatBidirectionalIterator implements FloatBigListIterator
{
    protected AbstractFloatBigListIterator() {
    }
    
    @Override
    public void set(final Float ok) {
        this.set((float)ok);
    }
    
    @Override
    public void add(final Float ok) {
        this.add((float)ok);
    }
    
    @Override
    public void set(final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextFloat();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousFloat();
        }
        return n - i - 1L;
    }
}
