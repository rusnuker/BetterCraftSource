// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

public abstract class AbstractByteListIterator extends AbstractByteBidirectionalIterator implements ByteListIterator
{
    protected AbstractByteListIterator() {
    }
    
    @Override
    public void set(final Byte ok) {
        this.set((byte)ok);
    }
    
    @Override
    public void add(final Byte ok) {
        this.add((byte)ok);
    }
    
    @Override
    public void set(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final byte k) {
        throw new UnsupportedOperationException();
    }
}
