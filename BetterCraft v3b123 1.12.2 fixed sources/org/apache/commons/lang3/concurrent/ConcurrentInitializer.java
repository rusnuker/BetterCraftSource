// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.lang3.concurrent;

public interface ConcurrentInitializer<T>
{
    T get() throws ConcurrentException;
}
