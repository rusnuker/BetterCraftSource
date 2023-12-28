// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import org.cef.network.CefWebPluginInfo;

public abstract class CefRequestContextHandlerAdapter implements CefRequestContextHandler
{
    @Override
    public boolean onBeforePluginLoad(final String mime_type, final String plugin_url, final boolean is_main_frame, final String top_origin_url, final CefWebPluginInfo plugin_info) {
        return false;
    }
    
    @Override
    public CefResourceRequestHandler getResourceRequestHandler(final CefBrowser browser, final CefFrame frame, final CefRequest request, final boolean isNavigation, final boolean isDownload, final String requestInitiator, final BoolRef disableDefaultHandling) {
        return null;
    }
}
