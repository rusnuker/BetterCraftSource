// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.handler.CefMessageRouterHandler;
import org.cef.callback.CefNative;

class CefMessageRouter_N extends CefMessageRouter implements CefNative
{
    private long N_CefHandle;
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    private CefMessageRouter_N(final CefMessageRouterConfig config) {
        super(config);
        this.N_CefHandle = 0L;
    }
    
    public static final CefMessageRouter createNative(final CefMessageRouterConfig config) {
        final CefMessageRouter_N result = new CefMessageRouter_N(config);
        try {
            result.N_Create(config);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        if (result.N_CefHandle == 0L) {
            return null;
        }
        return result;
    }
    
    @Override
    public void dispose() {
        try {
            this.N_Dispose();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean addHandler(final CefMessageRouterHandler handler, final boolean first) {
        try {
            return this.N_AddHandler(handler, first);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeHandler(final CefMessageRouterHandler handler) {
        try {
            return this.N_RemoveHandler(handler);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void cancelPending(final CefBrowser browser, final CefMessageRouterHandler handler) {
        try {
            this.N_CancelPending(browser, handler);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getPendingCount(final CefBrowser browser, final CefMessageRouterHandler handler) {
        try {
            return this.N_GetPendingCount(browser, handler);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    private final native void N_Create(final CefMessageRouterConfig p0);
    
    private final native void N_Dispose();
    
    private final native boolean N_AddHandler(final CefMessageRouterHandler p0, final boolean p1);
    
    private final native boolean N_RemoveHandler(final CefMessageRouterHandler p0);
    
    private final native void N_CancelPending(final CefBrowser p0, final CefMessageRouterHandler p1);
    
    private final native int N_GetPendingCount(final CefBrowser p0, final CefMessageRouterHandler p1);
}
