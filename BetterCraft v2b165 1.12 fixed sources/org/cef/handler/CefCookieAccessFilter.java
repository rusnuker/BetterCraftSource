// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.network.CefResponse;
import org.cef.network.CefCookie;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefCookieAccessFilter
{
    boolean canSendCookie(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final CefCookie p3);
    
    boolean canSaveCookie(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final CefResponse p3, final CefCookie p4);
}
