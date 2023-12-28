// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.SortedSet;

public interface ByteSortedSet extends ByteSet, SortedSet<Byte>
{
    ByteBidirectionalIterator iterator(final byte p0);
    
    @Deprecated
    ByteBidirectionalIterator byteIterator();
    
    ByteBidirectionalIterator iterator();
    
    ByteSortedSet subSet(final Byte p0, final Byte p1);
    
    ByteSortedSet headSet(final Byte p0);
    
    ByteSortedSet tailSet(final Byte p0);
    
    ByteComparator comparator();
    
    ByteSortedSet subSet(final byte p0, final byte p1);
    
    ByteSortedSet headSet(final byte p0);
    
    ByteSortedSet tailSet(final byte p0);
    
    byte firstByte();
    
    byte lastByte();
}
