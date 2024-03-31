// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.AsciiString;
import io.netty.handler.codec.EmptyHeaders;

public final class EmptyHttp2Headers extends EmptyHeaders<CharSequence, CharSequence, Http2Headers> implements Http2Headers
{
    public static final EmptyHttp2Headers INSTANCE;
    
    private EmptyHttp2Headers() {
    }
    
    @Override
    public EmptyHttp2Headers method(final CharSequence method) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers scheme(final CharSequence status) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers authority(final CharSequence authority) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers path(final CharSequence path) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers status(final CharSequence status) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public CharSequence method() {
        return ((EmptyHeaders<AsciiString, CharSequence, T>)this).get(PseudoHeaderName.METHOD.value());
    }
    
    @Override
    public CharSequence scheme() {
        return ((EmptyHeaders<AsciiString, CharSequence, T>)this).get(PseudoHeaderName.SCHEME.value());
    }
    
    @Override
    public CharSequence authority() {
        return ((EmptyHeaders<AsciiString, CharSequence, T>)this).get(PseudoHeaderName.AUTHORITY.value());
    }
    
    @Override
    public CharSequence path() {
        return ((EmptyHeaders<AsciiString, CharSequence, T>)this).get(PseudoHeaderName.PATH.value());
    }
    
    @Override
    public CharSequence status() {
        return ((EmptyHeaders<AsciiString, CharSequence, T>)this).get(PseudoHeaderName.STATUS.value());
    }
    
    static {
        INSTANCE = new EmptyHttp2Headers();
    }
}
