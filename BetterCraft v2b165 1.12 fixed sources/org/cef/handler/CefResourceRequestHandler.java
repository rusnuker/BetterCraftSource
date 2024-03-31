// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.network.CefURLRequest;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefResourceRequestHandler
{
    CefCookieAccessFilter getCookieAccessFilter(final CefBrowser p0, final CefFrame p1, final CefRequest p2);
    
    boolean onBeforeResourceLoad(final CefBrowser p0, final CefFrame p1, final CefRequest p2);
    
    CefResourceHandler getResourceHandler(final CefBrowser p0, final CefFrame p1, final CefRequest p2);
    
    void onResourceRedirect(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final CefResponse p3, final StringRef p4);
    
    boolean onResourceResponse(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final CefResponse p3);
    
    void onResourceLoadComplete(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final CefResponse p3, final CefURLRequest.Status p4, final long p5);
    
    void onProtocolExecution(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final BoolRef p3);
}
