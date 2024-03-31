// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

interface OpenSslEngineMap
{
    ReferenceCountedOpenSslEngine remove(final long p0);
    
    void add(final ReferenceCountedOpenSslEngine p0);
    
    ReferenceCountedOpenSslEngine get(final long p0);
}
