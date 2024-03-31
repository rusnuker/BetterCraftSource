// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Stack;

public interface ShortStack extends Stack<Short>
{
    void push(final short p0);
    
    short popShort();
    
    short topShort();
    
    short peekShort(final int p0);
}
