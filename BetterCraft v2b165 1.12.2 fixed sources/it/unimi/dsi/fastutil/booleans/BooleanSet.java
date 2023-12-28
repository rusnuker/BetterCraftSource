// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.Set;

public interface BooleanSet extends BooleanCollection, Set<Boolean>
{
    BooleanIterator iterator();
    
    boolean remove(final boolean p0);
}
