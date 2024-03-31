// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.AsciiString;
import java.util.Map;
import java.util.Iterator;
import io.netty.handler.codec.Headers;

public interface Http2Headers extends Headers<CharSequence, CharSequence, Http2Headers>
{
    Iterator<Map.Entry<CharSequence, CharSequence>> iterator();
    
    Http2Headers method(final CharSequence p0);
    
    Http2Headers scheme(final CharSequence p0);
    
    Http2Headers authority(final CharSequence p0);
    
    Http2Headers path(final CharSequence p0);
    
    Http2Headers status(final CharSequence p0);
    
    CharSequence method();
    
    CharSequence scheme();
    
    CharSequence authority();
    
    CharSequence path();
    
    CharSequence status();
    
    public enum PseudoHeaderName
    {
        METHOD(":method"), 
        SCHEME(":scheme"), 
        AUTHORITY(":authority"), 
        PATH(":path"), 
        STATUS(":status");
        
        private final AsciiString value;
        private static final CharSequenceMap<AsciiString> PSEUDO_HEADERS;
        
        private PseudoHeaderName(final String value) {
            this.value = new AsciiString(value);
        }
        
        public AsciiString value() {
            return this.value;
        }
        
        public static boolean isPseudoHeader(final CharSequence header) {
            return PseudoHeaderName.PSEUDO_HEADERS.contains(header);
        }
        
        static {
            PSEUDO_HEADERS = new CharSequenceMap<AsciiString>();
            for (final PseudoHeaderName pseudoHeader : values()) {
                PseudoHeaderName.PSEUDO_HEADERS.add(pseudoHeader.value(), AsciiString.EMPTY_STRING);
            }
        }
    }
}
