// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

public interface LongCounter
{
    void add(final long p0);
    
    void increment();
    
    void decrement();
    
    long value();
}
