// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.Collection;

public interface BooleanCollection extends Collection<Boolean>, BooleanIterable
{
    BooleanIterator iterator();
    
    @Deprecated
    BooleanIterator booleanIterator();
    
     <T> T[] toArray(final T[] p0);
    
    boolean contains(final boolean p0);
    
    boolean[] toBooleanArray();
    
    boolean[] toBooleanArray(final boolean[] p0);
    
    boolean[] toArray(final boolean[] p0);
    
    boolean add(final boolean p0);
    
    boolean rem(final boolean p0);
    
    boolean addAll(final BooleanCollection p0);
    
    boolean containsAll(final BooleanCollection p0);
    
    boolean removeAll(final BooleanCollection p0);
    
    boolean retainAll(final BooleanCollection p0);
}
