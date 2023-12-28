// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

public final class SslHandshakeCompletionEvent extends SslCompletionEvent
{
    public static final SslHandshakeCompletionEvent SUCCESS;
    
    private SslHandshakeCompletionEvent() {
    }
    
    public SslHandshakeCompletionEvent(final Throwable cause) {
        super(cause);
    }
    
    static {
        SUCCESS = new SslHandshakeCompletionEvent();
    }
}
