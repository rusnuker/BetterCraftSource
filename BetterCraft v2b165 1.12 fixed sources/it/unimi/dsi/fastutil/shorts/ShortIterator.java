// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;

public interface ShortIterator extends Iterator<Short>
{
    short nextShort();
    
    int skip(final int p0);
}
