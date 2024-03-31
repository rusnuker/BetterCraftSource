// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.BigList;

public interface ByteBigList extends BigList<Byte>, ByteCollection, Comparable<BigList<? extends Byte>>
{
    ByteBigListIterator iterator();
    
    ByteBigListIterator listIterator();
    
    ByteBigListIterator listIterator(final long p0);
    
    ByteBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final byte[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final byte[][] p1);
    
    void addElements(final long p0, final byte[][] p1, final long p2, final long p3);
    
    void add(final long p0, final byte p1);
    
    boolean addAll(final long p0, final ByteCollection p1);
    
    boolean addAll(final long p0, final ByteBigList p1);
    
    boolean addAll(final ByteBigList p0);
    
    byte getByte(final long p0);
    
    long indexOf(final byte p0);
    
    long lastIndexOf(final byte p0);
    
    byte removeByte(final long p0);
    
    byte set(final long p0, final byte p1);
}
