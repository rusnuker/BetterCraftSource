// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2HeadersDecoder
{
    Http2Headers decodeHeaders(final int p0, final ByteBuf p1) throws Http2Exception;
    
    Configuration configuration();
    
    public interface Configuration
    {
        void maxHeaderTableSize(final long p0) throws Http2Exception;
        
        long maxHeaderTableSize();
        
        void maxHeaderListSize(final long p0, final long p1) throws Http2Exception;
        
        long maxHeaderListSize();
        
        long maxHeaderListSizeGoAway();
    }
}
