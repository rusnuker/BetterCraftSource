// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2HeadersEncoder
{
    public static final SensitivityDetector NEVER_SENSITIVE = new SensitivityDetector() {
        @Override
        public boolean isSensitive(final CharSequence name, final CharSequence value) {
            return false;
        }
    };
    public static final SensitivityDetector ALWAYS_SENSITIVE = new SensitivityDetector() {
        @Override
        public boolean isSensitive(final CharSequence name, final CharSequence value) {
            return true;
        }
    };
    
    void encodeHeaders(final int p0, final Http2Headers p1, final ByteBuf p2) throws Http2Exception;
    
    Configuration configuration();
    
    public interface SensitivityDetector
    {
        boolean isSensitive(final CharSequence p0, final CharSequence p1);
    }
    
    public interface Configuration
    {
        void maxHeaderTableSize(final long p0) throws Http2Exception;
        
        long maxHeaderTableSize();
        
        void maxHeaderListSize(final long p0) throws Http2Exception;
        
        long maxHeaderListSize();
    }
}
