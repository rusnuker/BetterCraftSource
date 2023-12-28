// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;

public interface BooleanIterator extends Iterator<Boolean>
{
    boolean nextBoolean();
    
    int skip(final int p0);
}
