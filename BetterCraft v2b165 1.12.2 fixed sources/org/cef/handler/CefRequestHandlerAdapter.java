// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefRequestCallback;
import org.cef.callback.CefAuthCallback;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public abstract class CefRequestHandlerAdapter implements CefRequestHandler
{
    @Override
    public boolean onBeforeBrowse(final CefBrowser browser, final CefFrame frame, final CefRequest request, final boolean user_gesture, final boolean is_redirect) {
        return false;
    }
    
    @Override
    public CefResourceRequestHandler getResourceRequestHandler(final CefBrowser browser, final CefFrame frame, final CefRequest request, final boolean isNavigation, final boolean isDownload, final String requestInitiator, final BoolRef disableDefaultHandling) {
        return null;
    }
    
    @Override
    public boolean getAuthCredentials(final CefBrowser browser, final CefFrame frame, final boolean isProxy, final String host, final int port, final String realm, final String scheme, final CefAuthCallback callback) {
        return false;
    }
    
    @Override
    public boolean onQuotaRequest(final CefBrowser browser, final String origin_url, final long new_size, final CefRequestCallback callback) {
        return false;
    }
    
    @Override
    public boolean onCertificateError(final CefBrowser browser, final CefLoadHandler.ErrorCode cert_error, final String request_url, final CefRequestCallback callback) {
        return false;
    }
    
    @Override
    public void onPluginCrashed(final CefBrowser browser, final String pluginPath) {
    }
    
    @Override
    public void onRenderProcessTerminated(final CefBrowser browser, final TerminationStatus status) {
    }
}
