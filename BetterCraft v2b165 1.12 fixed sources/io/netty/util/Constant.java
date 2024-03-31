// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public interface Constant<T extends Constant<T>> extends Comparable<T>
{
    int id();
    
    String name();
}
