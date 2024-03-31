// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

public abstract class AbstractBooleanIterator implements BooleanIterator
{
    protected AbstractBooleanIterator() {
    }
    
    @Override
    public boolean nextBoolean() {
        return this.next();
    }
    
    @Deprecated
    @Override
    public Boolean next() {
        return this.nextBoolean();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int skip(final int n) {
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextBoolean();
        }
        return n - i - 1;
    }
}
