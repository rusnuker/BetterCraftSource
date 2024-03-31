// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Stack;

public interface IntStack extends Stack<Integer>
{
    void push(final int p0);
    
    int popInt();
    
    int topInt();
    
    int peekInt(final int p0);
}
