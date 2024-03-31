// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefLifeSpanHandler
{
    boolean onBeforePopup(final CefBrowser p0, final CefFrame p1, final String p2, final String p3);
    
    void onAfterCreated(final CefBrowser p0);
    
    void onAfterParentChanged(final CefBrowser p0);
    
    boolean doClose(final CefBrowser p0);
    
    void onBeforeClose(final CefBrowser p0);
}
