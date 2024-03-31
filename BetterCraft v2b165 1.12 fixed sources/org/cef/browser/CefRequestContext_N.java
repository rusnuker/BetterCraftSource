// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.handler.CefRequestContextHandler;
import org.cef.callback.CefNative;

class CefRequestContext_N extends CefRequestContext implements CefNative
{
    private long N_CefHandle;
    private static CefRequestContext_N globalInstance;
    private CefRequestContextHandler handler;
    
    static {
        CefRequestContext_N.globalInstance = null;
    }
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefRequestContext_N() {
        this.N_CefHandle = 0L;
        this.handler = null;
    }
    
    static final CefRequestContext_N getGlobalContextNative() {
        CefRequestContext_N result = null;
        try {
            result = N_GetGlobalContext();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        if (CefRequestContext_N.globalInstance == null) {
            CefRequestContext_N.globalInstance = result;
        }
        else if (CefRequestContext_N.globalInstance.N_CefHandle == result.N_CefHandle) {
            result.N_CefRequestContext_DTOR();
        }
        return CefRequestContext_N.globalInstance;
    }
    
    static final CefRequestContext_N createNative(final CefRequestContextHandler handler) {
        CefRequestContext_N result = null;
        try {
            result = N_CreateContext(handler);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        if (result != null) {
            result.handler = handler;
        }
        return result;
    }
    
    @Override
    public void dispose() {
        try {
            this.N_CefRequestContext_DTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean isGlobal() {
        try {
            return this.N_IsGlobal();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CefRequestContextHandler getHandler() {
        return this.handler;
    }
    
    private static final native CefRequestContext_N N_GetGlobalContext();
    
    private static final native CefRequestContext_N N_CreateContext(final CefRequestContextHandler p0);
    
    private final native boolean N_IsGlobal();
    
    private final native void N_CefRequestContext_DTOR();
}
