// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import org.cef.network.CefWebPluginInfo;

public interface CefRequestContextHandler
{
    boolean onBeforePluginLoad(final String p0, final String p1, final boolean p2, final String p3, final CefWebPluginInfo p4);
    
    CefResourceRequestHandler getResourceRequestHandler(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final boolean p3, final boolean p4, final String p5, final BoolRef p6);
}
