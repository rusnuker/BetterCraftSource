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

public abstract class CefResourceRequestHandlerAdapter implements CefResourceRequestHandler
{
    @Override
    public CefCookieAccessFilter getCookieAccessFilter(final CefBrowser browser, final CefFrame frame, final CefRequest request) {
        return null;
    }
    
    @Override
    public boolean onBeforeResourceLoad(final CefBrowser browser, final CefFrame frame, final CefRequest request) {
        return false;
    }
    
    @Override
    public CefResourceHandler getResourceHandler(final CefBrowser browser, final CefFrame frame, final CefRequest request) {
        return null;
    }
    
    @Override
    public void onResourceRedirect(final CefBrowser browser, final CefFrame frame, final CefRequest request, final CefResponse response, final StringRef new_url) {
    }
    
    @Override
    public boolean onResourceResponse(final CefBrowser browser, final CefFrame frame, final CefRequest request, final CefResponse response) {
        return false;
    }
    
    @Override
    public void onResourceLoadComplete(final CefBrowser browser, final CefFrame frame, final CefRequest request, final CefResponse response, final CefURLRequest.Status status, final long receivedContentLength) {
    }
    
    @Override
    public void onProtocolExecution(final CefBrowser browser, final CefFrame frame, final CefRequest request, final BoolRef allowOsExecution) {
    }
}
