// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

public final class SslCloseCompletionEvent extends SslCompletionEvent
{
    public static final SslCloseCompletionEvent SUCCESS;
    
    private SslCloseCompletionEvent() {
    }
    
    public SslCloseCompletionEvent(final Throwable cause) {
        super(cause);
    }
    
    static {
        SUCCESS = new SslCloseCompletionEvent();
    }
}
