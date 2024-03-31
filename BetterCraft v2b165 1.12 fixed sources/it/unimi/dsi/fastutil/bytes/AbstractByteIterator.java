// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

public abstract class AbstractByteIterator implements ByteIterator
{
    protected AbstractByteIterator() {
    }
    
    @Override
    public byte nextByte() {
        return this.next();
    }
    
    @Deprecated
    @Override
    public Byte next() {
        return this.nextByte();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int skip(final int n) {
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextByte();
        }
        return n - i - 1;
    }
}
