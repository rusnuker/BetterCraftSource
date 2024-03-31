// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.BigList;

public interface CharBigList extends BigList<Character>, CharCollection, Comparable<BigList<? extends Character>>
{
    CharBigListIterator iterator();
    
    CharBigListIterator listIterator();
    
    CharBigListIterator listIterator(final long p0);
    
    CharBigList subList(final long p0, final long p1);
    
    void getElements(final long p0, final char[][] p1, final long p2, final long p3);
    
    void removeElements(final long p0, final long p1);
    
    void addElements(final long p0, final char[][] p1);
    
    void addElements(final long p0, final char[][] p1, final long p2, final long p3);
    
    void add(final long p0, final char p1);
    
    boolean addAll(final long p0, final CharCollection p1);
    
    boolean addAll(final long p0, final CharBigList p1);
    
    boolean addAll(final CharBigList p0);
    
    char getChar(final long p0);
    
    long indexOf(final char p0);
    
    long lastIndexOf(final char p0);
    
    char removeChar(final long p0);
    
    char set(final long p0, final char p1);
}
