// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;

public interface IntIterator extends Iterator<Integer>
{
    int nextInt();
    
    int skip(final int p0);
}
