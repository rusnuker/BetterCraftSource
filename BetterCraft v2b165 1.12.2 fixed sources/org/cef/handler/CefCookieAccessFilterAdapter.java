// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.network.CefResponse;
import org.cef.network.CefCookie;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public abstract class CefCookieAccessFilterAdapter implements CefCookieAccessFilter
{
    @Override
    public boolean canSendCookie(final CefBrowser browser, final CefFrame frame, final CefRequest request, final CefCookie cookie) {
        return true;
    }
    
    @Override
    public boolean canSaveCookie(final CefBrowser browser, final CefFrame frame, final CefRequest request, final CefResponse response, final CefCookie cookie) {
        return true;
    }
}
