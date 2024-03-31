// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import java.util.Collections;
import java.util.List;

public final class DefaultSmtpResponse implements SmtpResponse
{
    private final int code;
    private final List<CharSequence> details;
    
    public DefaultSmtpResponse(final int code) {
        this(code, (List<CharSequence>)null);
    }
    
    public DefaultSmtpResponse(final int code, final CharSequence... details) {
        this(code, SmtpUtils.toUnmodifiableList(details));
    }
    
    DefaultSmtpResponse(final int code, final List<CharSequence> details) {
        if (code < 100 || code > 599) {
            throw new IllegalArgumentException("code must be 100 <= code <= 599");
        }
        this.code = code;
        if (details == null) {
            this.details = Collections.emptyList();
        }
        else {
            this.details = Collections.unmodifiableList((List<? extends CharSequence>)details);
        }
    }
    
    @Override
    public int code() {
        return this.code;
    }
    
    @Override
    public List<CharSequence> details() {
        return this.details;
    }
    
    @Override
    public int hashCode() {
        return this.code * 31 + this.details.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultSmtpResponse)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final DefaultSmtpResponse other = (DefaultSmtpResponse)o;
        return this.code() == other.code() && this.details().equals(other.details());
    }
    
    @Override
    public String toString() {
        return "DefaultSmtpResponse{code=" + this.code + ", details=" + this.details + '}';
    }
}
