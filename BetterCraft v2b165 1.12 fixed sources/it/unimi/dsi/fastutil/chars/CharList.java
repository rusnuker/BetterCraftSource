// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.List;

public interface CharList extends List<Character>, Comparable<List<? extends Character>>, CharCollection
{
    CharListIterator iterator();
    
    @Deprecated
    CharListIterator charListIterator();
    
    @Deprecated
    CharListIterator charListIterator(final int p0);
    
    CharListIterator listIterator();
    
    CharListIterator listIterator(final int p0);
    
    @Deprecated
    CharList charSubList(final int p0, final int p1);
    
    CharList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final char[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final char[] p1);
    
    void addElements(final int p0, final char[] p1, final int p2, final int p3);
    
    boolean add(final char p0);
    
    void add(final int p0, final char p1);
    
    boolean addAll(final int p0, final CharCollection p1);
    
    boolean addAll(final int p0, final CharList p1);
    
    boolean addAll(final CharList p0);
    
    char getChar(final int p0);
    
    int indexOf(final char p0);
    
    int lastIndexOf(final char p0);
    
    char removeChar(final int p0);
    
    char set(final int p0, final char p1);
}
