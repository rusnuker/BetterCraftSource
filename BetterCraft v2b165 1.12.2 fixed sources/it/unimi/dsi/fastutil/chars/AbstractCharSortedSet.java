// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.SortedSet;
import java.util.Iterator;

public abstract class AbstractCharSortedSet extends AbstractCharSet implements CharSortedSet
{
    protected AbstractCharSortedSet() {
    }
    
    @Deprecated
    @Override
    public CharSortedSet headSet(final Character to) {
        return this.headSet((char)to);
    }
    
    @Deprecated
    @Override
    public CharSortedSet tailSet(final Character from) {
        return this.tailSet((char)from);
    }
    
    @Deprecated
    @Override
    public CharSortedSet subSet(final Character from, final Character to) {
        return this.subSet((char)from, (char)to);
    }
    
    @Deprecated
    @Override
    public Character first() {
        return this.firstChar();
    }
    
    @Deprecated
    @Override
    public Character last() {
        return this.lastChar();
    }
    
    @Deprecated
    @Override
    public CharBidirectionalIterator charIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract CharBidirectionalIterator iterator();
}
