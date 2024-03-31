// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Stack;

public interface LongStack extends Stack<Long>
{
    void push(final long p0);
    
    long popLong();
    
    long topLong();
    
    long peekLong(final int p0);
}
