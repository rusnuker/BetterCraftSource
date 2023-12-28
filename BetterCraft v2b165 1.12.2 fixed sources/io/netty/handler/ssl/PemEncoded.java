// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

interface PemEncoded extends ByteBufHolder
{
    boolean isSensitive();
    
    PemEncoded copy();
    
    PemEncoded duplicate();
    
    PemEncoded retainedDuplicate();
    
    PemEncoded replace(final ByteBuf p0);
    
    PemEncoded retain();
    
    PemEncoded retain(final int p0);
    
    PemEncoded touch();
    
    PemEncoded touch(final Object p0);
}
