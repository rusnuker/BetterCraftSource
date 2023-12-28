// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public abstract class CefLoadHandlerAdapter implements CefLoadHandler
{
    @Override
    public void onLoadingStateChange(final CefBrowser browser, final boolean isLoading, final boolean canGoBack, final boolean canGoForward) {
    }
    
    @Override
    public void onLoadStart(final CefBrowser browser, final CefFrame frame, final CefRequest.TransitionType transitionType) {
    }
    
    @Override
    public void onLoadEnd(final CefBrowser browser, final CefFrame frame, final int httpStatusCode) {
    }
    
    @Override
    public void onLoadError(final CefBrowser browser, final CefFrame frame, final ErrorCode errorCode, final String errorText, final String failedUrl) {
    }
}
