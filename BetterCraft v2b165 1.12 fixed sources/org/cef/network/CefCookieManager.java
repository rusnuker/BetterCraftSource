// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.callback.CefCompletionCallback;
import org.cef.callback.CefCookieVisitor;
import java.util.Vector;

public abstract class CefCookieManager
{
    CefCookieManager() {
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.dispose();
        super.finalize();
    }
    
    public static final CefCookieManager getGlobalManager() {
        return CefCookieManager_N.getGlobalManagerNative();
    }
    
    public abstract void dispose();
    
    public abstract void setSupportedSchemes(final Vector<String> p0, final boolean p1);
    
    public abstract boolean visitAllCookies(final CefCookieVisitor p0);
    
    public abstract boolean visitUrlCookies(final String p0, final boolean p1, final CefCookieVisitor p2);
    
    public abstract boolean setCookie(final String p0, final CefCookie p1);
    
    public abstract boolean deleteCookies(final String p0, final String p1);
    
    public abstract boolean flushStore(final CefCompletionCallback p0);
}
