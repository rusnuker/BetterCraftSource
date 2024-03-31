// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

public abstract class AbstractByteBidirectionalIterator extends AbstractByteIterator implements ByteBidirectionalIterator
{
    protected AbstractByteBidirectionalIterator() {
    }
    
    @Override
    public byte previousByte() {
        return this.previous();
    }
    
    @Override
    public Byte previous() {
        return this.previousByte();
    }
    
    @Override
    public int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousByte();
        }
        return n - i - 1;
    }
}
