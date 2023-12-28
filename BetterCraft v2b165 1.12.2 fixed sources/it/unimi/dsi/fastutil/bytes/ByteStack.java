// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Stack;

public interface ByteStack extends Stack<Byte>
{
    void push(final byte p0);
    
    byte popByte();
    
    byte topByte();
    
    byte peekByte(final int p0);
}
