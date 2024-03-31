// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractByteSortedSet extends AbstractByteSet implements ByteSortedSet
{
    protected AbstractByteSortedSet() {
    }
    
    @Deprecated
    @Override
    public ByteSortedSet headSet(final Byte to) {
        return this.headSet((byte)to);
    }
    
    @Deprecated
    @Override
    public ByteSortedSet tailSet(final Byte from) {
        return this.tailSet((byte)from);
    }
    
    @Deprecated
    @Override
    public ByteSortedSet subSet(final Byte from, final Byte to) {
        return this.subSet((byte)from, (byte)to);
    }
    
    @Deprecated
    @Override
    public Byte first() {
        return this.firstByte();
    }
    
    @Deprecated
    @Override
    public Byte last() {
        return this.lastByte();
    }
    
    @Deprecated
    @Override
    public ByteBidirectionalIterator byteIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ByteBidirectionalIterator iterator();
}
