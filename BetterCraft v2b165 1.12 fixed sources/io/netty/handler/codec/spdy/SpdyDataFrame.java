// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface SpdyDataFrame extends ByteBufHolder, SpdyStreamFrame
{
    SpdyDataFrame setStreamId(final int p0);
    
    SpdyDataFrame setLast(final boolean p0);
    
    ByteBuf content();
    
    SpdyDataFrame copy();
    
    SpdyDataFrame duplicate();
    
    SpdyDataFrame retainedDuplicate();
    
    SpdyDataFrame replace(final ByteBuf p0);
    
    SpdyDataFrame retain();
    
    SpdyDataFrame retain(final int p0);
    
    SpdyDataFrame touch();
    
    SpdyDataFrame touch(final Object p0);
}
