// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.StringUtil;

public class DefaultSocks5PasswordAuthResponse extends AbstractSocks5Message implements Socks5PasswordAuthResponse
{
    private final Socks5PasswordAuthStatus status;
    
    public DefaultSocks5PasswordAuthResponse(final Socks5PasswordAuthStatus status) {
        if (status == null) {
            throw new NullPointerException("status");
        }
        this.status = status;
    }
    
    @Override
    public Socks5PasswordAuthStatus status() {
        return this.status;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
        final DecoderResult decoderResult = this.decoderResult();
        if (!decoderResult.isSuccess()) {
            buf.append("(decoderResult: ");
            buf.append(decoderResult);
            buf.append(", status: ");
        }
        else {
            buf.append("(status: ");
        }
        buf.append(this.status());
        buf.append(')');
        return buf.toString();
    }
}
