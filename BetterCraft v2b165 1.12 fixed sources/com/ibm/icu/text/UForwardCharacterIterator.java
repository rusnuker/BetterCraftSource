// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.text;

public interface UForwardCharacterIterator
{
    public static final int DONE = -1;
    
    int next();
    
    int nextCodePoint();
}
