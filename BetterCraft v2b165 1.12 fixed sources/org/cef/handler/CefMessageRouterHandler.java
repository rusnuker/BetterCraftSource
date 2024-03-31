// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefQueryCallback;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefNative;

public interface CefMessageRouterHandler extends CefNative
{
    boolean onQuery(final CefBrowser p0, final CefFrame p1, final long p2, final String p3, final boolean p4, final CefQueryCallback p5);
    
    void onQueryCanceled(final CefBrowser p0, final CefFrame p1, final long p2);
}
