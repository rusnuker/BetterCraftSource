// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.handler.CefRequestContextHandler;

public abstract class CefRequestContext
{
    CefRequestContext() {
    }
    
    public static final CefRequestContext getGlobalContext() {
        return CefRequestContext_N.getGlobalContextNative();
    }
    
    public static final CefRequestContext createContext(final CefRequestContextHandler handler) {
        return CefRequestContext_N.createNative(handler);
    }
    
    public abstract void dispose();
    
    public abstract boolean isGlobal();
    
    public abstract CefRequestContextHandler getHandler();
}
