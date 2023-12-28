// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.haproxy;

import io.netty.handler.codec.DecoderException;

public class HAProxyProtocolException extends DecoderException
{
    private static final long serialVersionUID = 713710864325167351L;
    
    public HAProxyProtocolException() {
    }
    
    public HAProxyProtocolException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public HAProxyProtocolException(final String message) {
        super(message);
    }
    
    public HAProxyProtocolException(final Throwable cause) {
        super(cause);
    }
}
