// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.handler.CefMessageRouterHandler;

public abstract class CefMessageRouter
{
    private final CefMessageRouterConfig routerConfig_;
    
    CefMessageRouter(final CefMessageRouterConfig routerConfig) {
        this.routerConfig_ = routerConfig;
    }
    
    public static final CefMessageRouter create() {
        return create(null, null);
    }
    
    public static final CefMessageRouter create(final CefMessageRouterConfig config) {
        return create(config, null);
    }
    
    public static final CefMessageRouter create(final CefMessageRouterHandler handler) {
        return create(null, handler);
    }
    
    public static final CefMessageRouter create(final CefMessageRouterConfig config, final CefMessageRouterHandler handler) {
        final CefMessageRouter router = CefMessageRouter_N.createNative(config);
        if (router != null && handler != null) {
            router.addHandler(handler, true);
        }
        return router;
    }
    
    public abstract void dispose();
    
    public final CefMessageRouterConfig getMessageRouterConfig() {
        return this.routerConfig_;
    }
    
    public abstract boolean addHandler(final CefMessageRouterHandler p0, final boolean p1);
    
    public abstract boolean removeHandler(final CefMessageRouterHandler p0);
    
    public abstract void cancelPending(final CefBrowser p0, final CefMessageRouterHandler p1);
    
    public abstract int getPendingCount(final CefBrowser p0, final CefMessageRouterHandler p1);
    
    public static class CefMessageRouterConfig
    {
        public String jsQueryFunction;
        public String jsCancelFunction;
        
        public CefMessageRouterConfig() {
            this("cefQuery", "cefQueryCancel");
        }
        
        public CefMessageRouterConfig(final String queryFunction, final String cancelFunction) {
            this.jsQueryFunction = queryFunction;
            this.jsCancelFunction = cancelFunction;
        }
    }
}
