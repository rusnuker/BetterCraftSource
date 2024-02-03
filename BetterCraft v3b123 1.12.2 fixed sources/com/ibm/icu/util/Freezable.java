// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.util;

public interface Freezable<T> extends Cloneable
{
    boolean isFrozen();
    
    T freeze();
    
    T cloneAsThawed();
}
