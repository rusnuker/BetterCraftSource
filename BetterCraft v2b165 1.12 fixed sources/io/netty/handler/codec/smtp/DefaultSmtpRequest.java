// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

public final class DefaultSmtpRequest implements SmtpRequest
{
    private final SmtpCommand command;
    private final List<CharSequence> parameters;
    
    public DefaultSmtpRequest(final SmtpCommand command) {
        this.command = ObjectUtil.checkNotNull(command, "command");
        this.parameters = Collections.emptyList();
    }
    
    public DefaultSmtpRequest(final SmtpCommand command, final CharSequence... parameters) {
        this.command = ObjectUtil.checkNotNull(command, "command");
        this.parameters = SmtpUtils.toUnmodifiableList(parameters);
    }
    
    public DefaultSmtpRequest(final CharSequence command, final CharSequence... parameters) {
        this(SmtpCommand.valueOf(command), parameters);
    }
    
    DefaultSmtpRequest(final SmtpCommand command, final List<CharSequence> parameters) {
        this.command = ObjectUtil.checkNotNull(command, "command");
        this.parameters = ((parameters != null) ? Collections.unmodifiableList((List<? extends CharSequence>)parameters) : Collections.emptyList());
    }
    
    @Override
    public SmtpCommand command() {
        return this.command;
    }
    
    @Override
    public List<CharSequence> parameters() {
        return this.parameters;
    }
    
    @Override
    public int hashCode() {
        return this.command.hashCode() * 31 + this.parameters.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultSmtpRequest)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final DefaultSmtpRequest other = (DefaultSmtpRequest)o;
        return this.command().equals(other.command()) && this.parameters().equals(other.parameters());
    }
    
    @Override
    public String toString() {
        return "DefaultSmtpRequest{command=" + this.command + ", parameters=" + this.parameters + '}';
    }
}
