// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;

public interface LongIterator extends Iterator<Long>
{
    long nextLong();
    
    int skip(final int p0);
}
