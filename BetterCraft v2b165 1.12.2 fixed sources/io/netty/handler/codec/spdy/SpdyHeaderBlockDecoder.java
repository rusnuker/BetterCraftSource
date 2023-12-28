// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

abstract class SpdyHeaderBlockDecoder
{
    static SpdyHeaderBlockDecoder newInstance(final SpdyVersion spdyVersion, final int maxHeaderSize) {
        return new SpdyHeaderBlockZlibDecoder(spdyVersion, maxHeaderSize);
    }
    
    abstract void decode(final ByteBufAllocator p0, final ByteBuf p1, final SpdyHeadersFrame p2) throws Exception;
    
    abstract void endHeaderBlock(final SpdyHeadersFrame p0) throws Exception;
    
    abstract void end();
}
