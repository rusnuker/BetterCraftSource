// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.CefClient;

public class CefBrowserFactory
{
    public static CefBrowser create(final CefClient client, final String url, final boolean isOffscreenRendered, final boolean isTransparent, final CefRequestContext context) {
        if (isOffscreenRendered) {
            return new CefBrowserOsr(client, url, isTransparent, context);
        }
        return new CefBrowserWr(client, url, context);
    }
}
