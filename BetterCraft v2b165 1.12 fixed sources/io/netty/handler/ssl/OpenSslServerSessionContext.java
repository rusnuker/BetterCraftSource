// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;

public final class OpenSslServerSessionContext extends OpenSslSessionContext
{
    OpenSslServerSessionContext(final ReferenceCountedOpenSslContext context) {
        super(context);
    }
    
    @Override
    public void setSessionTimeout(final int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException();
        }
        SSLContext.setSessionCacheTimeout(this.context.ctx, seconds);
    }
    
    @Override
    public int getSessionTimeout() {
        return (int)SSLContext.getSessionCacheTimeout(this.context.ctx);
    }
    
    @Override
    public void setSessionCacheSize(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        SSLContext.setSessionCacheSize(this.context.ctx, size);
    }
    
    @Override
    public int getSessionCacheSize() {
        return (int)SSLContext.getSessionCacheSize(this.context.ctx);
    }
    
    @Override
    public void setSessionCacheEnabled(final boolean enabled) {
        final long mode = enabled ? SSL.SSL_SESS_CACHE_SERVER : SSL.SSL_SESS_CACHE_OFF;
        SSLContext.setSessionCacheMode(this.context.ctx, mode);
    }
    
    @Override
    public boolean isSessionCacheEnabled() {
        return SSLContext.getSessionCacheMode(this.context.ctx) == SSL.SSL_SESS_CACHE_SERVER;
    }
    
    public boolean setSessionIdContext(final byte[] sidCtx) {
        return SSLContext.setSessionIdContext(this.context.ctx, sidCtx);
    }
}
