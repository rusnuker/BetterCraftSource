// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefSchemeHandlerFactory
{
    CefResourceHandler create(final CefBrowser p0, final CefFrame p1, final String p2, final CefRequest p3);
}
