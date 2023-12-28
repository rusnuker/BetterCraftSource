// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Set;

public interface IntSet extends IntCollection, Set<Integer>
{
    IntIterator iterator();
    
    boolean remove(final int p0);
}
