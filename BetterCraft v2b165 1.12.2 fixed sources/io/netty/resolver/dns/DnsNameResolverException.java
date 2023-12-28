// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.dns.DnsQuestion;
import java.net.InetSocketAddress;

public final class DnsNameResolverException extends RuntimeException
{
    private static final long serialVersionUID = -8826717909627131850L;
    private final InetSocketAddress remoteAddress;
    private final DnsQuestion question;
    
    public DnsNameResolverException(final InetSocketAddress remoteAddress, final DnsQuestion question, final String message) {
        super(message);
        this.remoteAddress = validateRemoteAddress(remoteAddress);
        this.question = validateQuestion(question);
    }
    
    public DnsNameResolverException(final InetSocketAddress remoteAddress, final DnsQuestion question, final String message, final Throwable cause) {
        super(message, cause);
        this.remoteAddress = validateRemoteAddress(remoteAddress);
        this.question = validateQuestion(question);
    }
    
    private static InetSocketAddress validateRemoteAddress(final InetSocketAddress remoteAddress) {
        return ObjectUtil.checkNotNull(remoteAddress, "remoteAddress");
    }
    
    private static DnsQuestion validateQuestion(final DnsQuestion question) {
        return ObjectUtil.checkNotNull(question, "question");
    }
    
    public InetSocketAddress remoteAddress() {
        return this.remoteAddress;
    }
    
    public DnsQuestion question() {
        return this.question;
    }
    
    @Override
    public Throwable fillInStackTrace() {
        this.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        return this;
    }
}
