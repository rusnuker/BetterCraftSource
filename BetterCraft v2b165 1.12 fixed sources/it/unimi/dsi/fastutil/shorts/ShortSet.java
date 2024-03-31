// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Set;

public interface ShortSet extends ShortCollection, Set<Short>
{
    ShortIterator iterator();
    
    boolean remove(final short p0);
}
