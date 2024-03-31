// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

public abstract class AbstractByteBigListIterator extends AbstractByteBidirectionalIterator implements ByteBigListIterator
{
    protected AbstractByteBigListIterator() {
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
    
    @Override
    public long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextByte();
        }
        return n - i - 1L;
    }
    
    public long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousByte();
        }
        return n - i - 1L;
    }
}
