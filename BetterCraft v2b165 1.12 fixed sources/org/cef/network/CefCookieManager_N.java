// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.callback.CefCompletionCallback;
import org.cef.callback.CefCookieVisitor;
import java.util.Vector;
import org.cef.callback.CefNative;

class CefCookieManager_N extends CefCookieManager implements CefNative
{
    private long N_CefHandle;
    private static CefCookieManager_N globalInstance;
    
    static {
        CefCookieManager_N.globalInstance = null;
    }
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefCookieManager_N() {
        this.N_CefHandle = 0L;
    }
    
    static final synchronized CefCookieManager_N getGlobalManagerNative() {
        if (CefCookieManager_N.globalInstance != null && CefCookieManager_N.globalInstance.N_CefHandle != 0L) {
            return CefCookieManager_N.globalInstance;
        }
        CefCookieManager_N result = null;
        try {
            result = N_GetGlobalManager();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        return CefCookieManager_N.globalInstance = result;
    }
    
    @Override
    public void dispose() {
        try {
            this.N_Dispose(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setSupportedSchemes(final Vector<String> schemes, final boolean includeDefaults) {
        try {
            this.N_SetSupportedSchemes(this.N_CefHandle, schemes, includeDefaults);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean visitAllCookies(final CefCookieVisitor visitor) {
        try {
            return this.N_VisitAllCookies(this.N_CefHandle, visitor);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean visitUrlCookies(final String url, final boolean includeHttpOnly, final CefCookieVisitor visitor) {
        try {
            return this.N_VisitUrlCookies(this.N_CefHandle, url, includeHttpOnly, visitor);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean setCookie(final String url, final CefCookie cookie) {
        try {
            return this.N_SetCookie(this.N_CefHandle, url, cookie);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteCookies(final String url, final String cookieName) {
        try {
            return this.N_DeleteCookies(this.N_CefHandle, url, cookieName);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean flushStore(final CefCompletionCallback handler) {
        try {
            return this.N_FlushStore(this.N_CefHandle, handler);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    private static final native CefCookieManager_N N_GetGlobalManager();
    
    private final native void N_Dispose(final long p0);
    
    private final native void N_SetSupportedSchemes(final long p0, final Vector<String> p1, final boolean p2);
    
    private final native boolean N_VisitAllCookies(final long p0, final CefCookieVisitor p1);
    
    private final native boolean N_VisitUrlCookies(final long p0, final String p1, final boolean p2, final CefCookieVisitor p3);
    
    private final native boolean N_SetCookie(final long p0, final String p1, final CefCookie p2);
    
    private final native boolean N_DeleteCookies(final long p0, final String p1, final String p2);
    
    private final native boolean N_FlushStore(final long p0, final CefCompletionCallback p1);
}
