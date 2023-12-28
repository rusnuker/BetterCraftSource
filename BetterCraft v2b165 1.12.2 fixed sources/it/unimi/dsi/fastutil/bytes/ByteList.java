// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.List;

public interface ByteList extends List<Byte>, Comparable<List<? extends Byte>>, ByteCollection
{
    ByteListIterator iterator();
    
    @Deprecated
    ByteListIterator byteListIterator();
    
    @Deprecated
    ByteListIterator byteListIterator(final int p0);
    
    ByteListIterator listIterator();
    
    ByteListIterator listIterator(final int p0);
    
    @Deprecated
    ByteList byteSubList(final int p0, final int p1);
    
    ByteList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final byte[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final byte[] p1);
    
    void addElements(final int p0, final byte[] p1, final int p2, final int p3);
    
    boolean add(final byte p0);
    
    void add(final int p0, final byte p1);
    
    boolean addAll(final int p0, final ByteCollection p1);
    
    boolean addAll(final int p0, final ByteList p1);
    
    boolean addAll(final ByteList p0);
    
    byte getByte(final int p0);
    
    int indexOf(final byte p0);
    
    int lastIndexOf(final byte p0);
    
    byte removeByte(final int p0);
    
    byte set(final int p0, final byte p1);
}
