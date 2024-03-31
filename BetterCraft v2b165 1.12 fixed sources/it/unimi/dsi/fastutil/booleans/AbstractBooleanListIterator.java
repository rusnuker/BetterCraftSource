// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

public abstract class AbstractBooleanListIterator extends AbstractBooleanBidirectionalIterator implements BooleanListIterator
{
    protected AbstractBooleanListIterator() {
    }
    
    @Override
    public void set(final Boolean ok) {
        this.set((boolean)ok);
    }
    
    @Override
    public void add(final Boolean ok) {
        this.add((boolean)ok);
    }
    
    @Override
    public void set(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final boolean k) {
        throw new UnsupportedOperationException();
    }
}
