// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.StringUtil;

public class DefaultSocks5PasswordAuthRequest extends AbstractSocks5Message implements Socks5PasswordAuthRequest
{
    private final String username;
    private final String password;
    
    public DefaultSocks5PasswordAuthRequest(final String username, final String password) {
        if (username == null) {
            throw new NullPointerException("username");
        }
        if (password == null) {
            throw new NullPointerException("password");
        }
        if (username.length() > 255) {
            throw new IllegalArgumentException("username: **** (expected: less than 256 chars)");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException("password: **** (expected: less than 256 chars)");
        }
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String username() {
        return this.username;
    }
    
    @Override
    public String password() {
        return this.password;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
        final DecoderResult decoderResult = this.decoderResult();
        if (!decoderResult.isSuccess()) {
            buf.append("(decoderResult: ");
            buf.append(decoderResult);
            buf.append(", username: ");
        }
        else {
            buf.append("(username: ");
        }
        buf.append(this.username());
        buf.append(", password: ****)");
        return buf.toString();
    }
}
