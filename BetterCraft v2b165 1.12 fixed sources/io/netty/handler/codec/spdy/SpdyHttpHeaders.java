// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.util.AsciiString;

public final class SpdyHttpHeaders
{
    private SpdyHttpHeaders() {
    }
    
    public static final class Names
    {
        public static final AsciiString STREAM_ID;
        public static final AsciiString ASSOCIATED_TO_STREAM_ID;
        public static final AsciiString PRIORITY;
        public static final AsciiString SCHEME;
        
        private Names() {
        }
        
        static {
            STREAM_ID = new AsciiString("x-spdy-stream-id");
            ASSOCIATED_TO_STREAM_ID = new AsciiString("x-spdy-associated-to-stream-id");
            PRIORITY = new AsciiString("x-spdy-priority");
            SCHEME = new AsciiString("x-spdy-scheme");
        }
    }
}
