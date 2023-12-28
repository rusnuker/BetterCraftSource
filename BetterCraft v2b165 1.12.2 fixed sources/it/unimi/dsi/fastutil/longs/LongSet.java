// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Set;

public interface LongSet extends LongCollection, Set<Long>
{
    LongIterator iterator();
    
    boolean remove(final long p0);
}
