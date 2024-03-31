// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.Stack;

public interface BooleanStack extends Stack<Boolean>
{
    void push(final boolean p0);
    
    boolean popBoolean();
    
    boolean topBoolean();
    
    boolean peekBoolean(final int p0);
}
