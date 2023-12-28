// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.proxy;

import java.net.ConnectException;

public class ProxyConnectException extends ConnectException
{
    private static final long serialVersionUID = 5211364632246265538L;
    
    public ProxyConnectException() {
    }
    
    public ProxyConnectException(final String msg) {
        super(msg);
    }
    
    public ProxyConnectException(final Throwable cause) {
        this.initCause(cause);
    }
    
    public ProxyConnectException(final String msg, final Throwable cause) {
        super(msg);
        this.initCause(cause);
    }
}
