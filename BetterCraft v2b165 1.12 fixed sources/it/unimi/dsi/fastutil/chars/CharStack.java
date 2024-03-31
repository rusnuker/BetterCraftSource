// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Stack;

public interface CharStack extends Stack<Character>
{
    void push(final char p0);
    
    char popChar();
    
    char topChar();
    
    char peekChar(final int p0);
}
